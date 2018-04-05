package com.ilikezhibo.ggzb.pull.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jack.utils.PixelDpHelper;
import com.ilikezhibo.ggzb.R;

public class PullToRefreshView extends ListView implements OnScrollListener,
		OnClickListener {

	private Context mCon;
	private LayoutInflater mInflater;
	private int mode;

	public PullToRefreshView(Context context) {
		super(context);
		init(context, null);
	}

	public PullToRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	private final int RELEASE_To_REFRESH = 0;

	private final int PULL_To_REFRESH = 1;

	private final int REFRESHING = 2;
	private final int DONE = 3;

	private final int RATIO = 1;

	private LinearLayout mHeadView;
	private TextView mListStatus;
	// private TextView mListUpdateTime;
	private ImageView mListHeadArrow;
	private ImageView mListHeadLoading;
	private AnimationDrawable mHeaderAnim;
	private int mHeadHeight;

	private RelativeLayout mFootView;
	private LinearLayout mFootBgLayout;
	private ImageView mListFootLoading;
	private AnimationDrawable mFooterAnim;

	private int mListState;
	private boolean isShowBackToTop = false;
	private final int HIDEBACKTOTOP = 0;

	private int mRefreshState;
	private OnRefreshListener mOnRefreshListener;
	private boolean isBack;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	private boolean isRecored;
	private int startY;
	private int mFirstVisibleItem;

	private boolean isRefreshable;
	private boolean mBanRefresh = true;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case HIDEBACKTOTOP:
				if (!isShowBackToTop) {
					if (mBackToTopView != null) {
						mBackToTopView.setVisibility(View.GONE);
					}
				}
				break;
			}
		}
	};
	private int lastTotalItemCount;
	private boolean isFooterEnabled;
	private int ptr_load_more;
	private int mListRefreshing;
	private int mPullToRefreshStr;
	private int mReleaseToRefresh;
	private boolean isSuccess;

	private void init(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.PullToRefresh);
		if (a.hasValue(R.styleable.PullToRefresh_pullMode)) {
			mode = a.getInteger(R.styleable.PullToRefresh_pullMode, 0);
		}
		this.mCon = context;
		setCacheColorHint(mCon.getResources().getColor(R.color.transparent));
		mInflater = LayoutInflater.from(mCon);

		mHeadView = (LinearLayout) mInflater
				.inflate(R.layout.list_header, null);

		mListHeadArrow = (ImageView) mHeadView
				.findViewById(R.id.mListHeadArrow);
		mListHeadArrow.setMinimumHeight(50);
		mListHeadLoading = (ImageView) mHeadView
				.findViewById(R.id.mListHeadLoading);
		mHeaderAnim = (AnimationDrawable) mListHeadLoading.getDrawable();
		mListStatus = (TextView) mHeadView.findViewById(R.id.mListStatus);
		// mListUpdateTime = (TextView)
		// mHeadView.findViewById(R.id.mListUpdateTime);

		measureView(mHeadView);
		mHeadHeight = mHeadView.getMeasuredHeight();
		mHeadView.setPadding(0, -1 * mHeadHeight, 0, 0);
		mHeadView.invalidate();
		addHeaderView(mHeadView, null, false);

		/** 底部View. */
		mFootView = (RelativeLayout) mInflater.inflate(R.layout.list_footer,
				null);
		mFootView.setOnClickListener(this);
		mFootBgLayout = (LinearLayout) mFootView
				.findViewById(R.id.mFootBgLayout);
		mListFootLoading = (ImageView) mFootView
				.findViewById(R.id.mListFootLoading);
		mFooterAnim = (AnimationDrawable) mListFootLoading.getDrawable();
		mRefreshFooterText = (TextView) mFootView
				.findViewById(R.id.refresh_tv_message);
		// emptyFootView = mInflater.inflate(R.layout.empty_foot, null);
		setOnScrollListener(this);

		animation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(200);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);

		mRefreshState = DONE;
		isRefreshable = false;
	}

	public void configRefreshStateContent(int mReleaseToRefresh,
			int mPullToRefreshStr, int mListRefreshing, int ptr_load_more) {
		this.mReleaseToRefresh = mReleaseToRefresh;
		this.mPullToRefreshStr = mPullToRefreshStr;
		this.mListRefreshing = mListRefreshing;
		this.ptr_load_more = ptr_load_more;
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public void onScrollStateChanged(AbsListView arg0, int scrollState) {
		mListState = scrollState;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// Trace.d("firstVisibleItem:" + firstVisibleItem
		// + " visibleItemCount:" + visibleItemCount + " totalItemCount:"
		// + totalItemCount);
		if ((mode == 5 || mode == 4 || mode == 0) && isSuccess) {
			if (visibleItemCount != totalItemCount
					&& view.getLastVisiblePosition() == (view.getCount() - 1)) {
				// Trace.d("last" + view.getLastVisiblePosition()
				// + " count:" + view.getCount());
				// firstVisibleItem + visibleItemCount + 3 > totalItemCount
				// && totalItemCount > 20
				// Trace.d("firstVisibleItem:" + firstVisibleItem
				// + " visibleItemCount:" + visibleItemCount
				// + " totalItemCount:" + totalItemCount);
				if (mRefreshState != REFRESHING
						&& lastTotalItemCount != totalItemCount) {
					lastTotalItemCount = totalItemCount;
					initRefresh(FOOTER);
				}
			}
		}
		mFirstVisibleItem = firstVisibleItem;
		switch (mListState) {
		case SCROLL_STATE_FLING:
			if (mFirstVisibleItem == 0) {
				isShowBackToTop = false;
				mHandler.sendEmptyMessageDelayed(HIDEBACKTOTOP, 1000);
			} else {
				isShowBackToTop = true;
			}
			break;
		case SCROLL_STATE_TOUCH_SCROLL:
			if (mBackToTopView != null) {
				mBackToTopView.setVisibility(View.VISIBLE);
				isShowBackToTop = true;
			}
			break;
		case SCROLL_STATE_IDLE:
			isShowBackToTop = false;
			mHandler.sendEmptyMessageDelayed(HIDEBACKTOTOP, 3000);
			break;
		}
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (isRefreshable && mBanRefresh) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (mFirstVisibleItem == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
				}
				break;
			case MotionEvent.ACTION_CANCEL:
				if (mRefreshState != REFRESHING) {
					if (mRefreshState == RELEASE_To_REFRESH) {
						mRefreshState = REFRESHING;
						changeHeadStatus();
						onRefresh(HEADER);
					} else {
						mRefreshState = DONE;
						changeHeadStatus();
					}
				}
				isRecored = false;
				isBack = false;
				break;
			case MotionEvent.ACTION_UP:
				if (mRefreshState != REFRESHING) {
					if (mRefreshState == RELEASE_To_REFRESH) {
						mRefreshState = REFRESHING;
						changeHeadStatus();
						onRefresh(HEADER);
					} else {
						mRefreshState = DONE;
						changeHeadStatus();
					}
				}
				isRecored = false;
				isBack = false;
				break;
			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();
				if (!isRecored && mFirstVisibleItem == 0) {
					isRecored = true;
					startY = tempY;
				}
				if (mRefreshState != REFRESHING && isRecored) {
					if (mRefreshState == RELEASE_To_REFRESH) {
						setSelection(0);
						if (((tempY - startY)
								/ PixelDpHelper.dip2px(getContext(), RATIO) < mHeadHeight)
								&& (tempY - startY) > 0) {
							mRefreshState = PULL_To_REFRESH;
							changeHeadStatus();
						} else if (tempY - startY <= 0) {
							mRefreshState = DONE;
							changeHeadStatus();
						}
					}
					if (mRefreshState == PULL_To_REFRESH) {
						setSelection(0);
						if ((tempY - startY)
								/ PixelDpHelper.dip2px(getContext(), RATIO) >= mHeadHeight) {
							mRefreshState = RELEASE_To_REFRESH;
							isBack = true;
							changeHeadStatus();
						} else if (tempY - startY <= 0) {
							mRefreshState = DONE;
							changeHeadStatus();
						}
					}
					if (mRefreshState == DONE) {
						if (tempY - startY > 0) {
							mRefreshState = PULL_To_REFRESH;
							changeHeadStatus();
						}
					}
					if (mRefreshState == PULL_To_REFRESH) {
						mHeadView.setPadding(
								0,
								-1
										* mHeadHeight
										+ (tempY - startY)
										/ PixelDpHelper.dip2px(getContext(),
												RATIO), 0, 0);
					}
					if (mRefreshState == RELEASE_To_REFRESH) {
						mHeadView.setPadding(0, (tempY - startY)
								/ PixelDpHelper.dip2px(getContext(), RATIO)
								- mHeadHeight, 0, 0);
					}
				}
				break;
			}
		}
		return super.onTouchEvent(event);
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeadStatus() {
		switch (mRefreshState) {
		case RELEASE_To_REFRESH:
			mListHeadArrow.setVisibility(View.VISIBLE);
			mListHeadLoading.setVisibility(View.GONE);
			mHeaderAnim.stop();
			mListStatus.setVisibility(View.VISIBLE);
			// mListUpdateTime.setVisibility(View.VISIBLE);
			mListHeadArrow.clearAnimation();
			mListHeadArrow.startAnimation(animation);
			if (mReleaseToRefresh != 0) {
				mListStatus.setText(mReleaseToRefresh);
			} else {
				mListStatus.setText(R.string.mReleaseToRefresh);
			}

			break;
		case PULL_To_REFRESH:
			mListHeadLoading.setVisibility(View.GONE);
			mHeaderAnim.stop();
			mListStatus.setVisibility(View.VISIBLE);
			// mListUpdateTime.setVisibility(View.VISIBLE);
			mListHeadArrow.clearAnimation();
			mListHeadArrow.setVisibility(View.VISIBLE);
			if (isBack) {
				isBack = false;
				mListHeadArrow.clearAnimation();
				mListHeadArrow.startAnimation(reverseAnimation);
				if (mPullToRefreshStr != 0) {
					mListStatus.setText(mPullToRefreshStr);
				} else {
					mListStatus.setText(R.string.mPullToRefreshStr);
				}
				// mApp.playSound(R.raw.pushup);
			} else {
				if (mPullToRefreshStr != 0) {
					mListStatus.setText(mPullToRefreshStr);
				} else {
					mListStatus.setText(R.string.mPullToRefreshStr);
				}
			}
			break;
		case REFRESHING:
			/** 当前状态,正在刷新... */
			mHeadView.setPadding(0, 0, 0, 0);
			mListHeadLoading.setVisibility(View.VISIBLE);
			mHeaderAnim.start();
			mListHeadArrow.clearAnimation();
			mListHeadArrow.setVisibility(View.GONE);
			if (mListRefreshing != 0) {
				mListStatus.setText(mListRefreshing);
			} else {
				mListStatus.setText(R.string.mListRefreshing);
			}
			// mListUpdateTime.setVisibility(View.VISIBLE);
			break;
		case DONE:
			mHeadView.setPadding(0, -1 * mHeadHeight, 0, 0);
			mListHeadLoading.setVisibility(View.GONE);
			mHeaderAnim.stop();
			mListHeadArrow.clearAnimation();
			mListHeadArrow.setImageResource(R.drawable.ic_refresh_droparrow);
			if (mPullToRefreshStr != 0) {
				mListStatus.setText(mPullToRefreshStr);
			} else {
				mListStatus.setText(R.string.mPullToRefreshStr);
			}
			// mListUpdateTime.setVisibility(View.VISIBLE);
			break;
		}
	}

	/** ListView数据刷新或加载更多时回调的注册函数. */
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.mOnRefreshListener = onRefreshListener;
		isRefreshable = true;
	}

	public interface OnRefreshListener {
		public void onRefresh(int which);
	}

	public void setRefreshAble(boolean enable) {
		mBanRefresh = enable;
	}

	/**
	 * Resets the list to reset normal mRefreshState after reset refresh.
	 */
	public void onRefreshComplete(int which) {
		isSuccess = true;
		switch (which) {
		case HEADER:
			mRefreshState = DONE;
			changeHeadStatus();
			break;
		case FOOTER:
			mListFootLoading.setVisibility(View.GONE);
			mFooterAnim.stop();
			mRefreshState = DONE;
			removeFooterView();
			break;
		}
	}

	/**
	 * Resets the list to reset normal mRefreshState after reset refresh.
	 */
	public void onRefreshComplete(int which, boolean isSuccess) {
		switch (which) {
		case HEADER:
			this.isSuccess = true;
			mRefreshState = DONE;
			changeHeadStatus();
			break;
		case FOOTER:
			mListFootLoading.setVisibility(View.GONE);
			mFooterAnim.stop();
			mRefreshState = DONE;
			// TODO change to failure
			mRefreshFooterText.setText("刷新失败，请重试");
			this.isSuccess = isSuccess;
			if (isSuccess) {
				removeFooterView();
			}
			break;
		}
	}

	public void enableFooter(boolean enable) {
		if (enable) {
			lastTotalItemCount = 0;
		}
	}

	/** ListView刷新时调用的方法(可能由下拉ListView或点击"更多"触发该方法). */
	public void initRefresh(int which) {
		prepareForRefresh(which);
		onRefresh(which);
	}

	public final static int HEADER = 0;
	public final static int FOOTER = 1;

	private void prepareForRefresh(int which) {
		switch (which) {
		case HEADER:
			mRefreshState = REFRESHING;
			changeHeadStatus();
			break;
		case FOOTER:
			if (mTempFootView == null) {
				addFooterView();
			}
			mFootBgLayout.setBackgroundDrawable(null);
			mListFootLoading.setVisibility(View.VISIBLE);
			mFooterAnim.start();
			if (ptr_load_more != 0) {
				mRefreshFooterText.setText(ptr_load_more);
			} else {
				mRefreshFooterText.setText(R.string.ptr_load_more);
			}
			mRefreshState = REFRESHING;
			break;
		default:
			break;
		}
	}

	private void onRefresh(int which) {
		if (mOnRefreshListener != null) {
			mOnRefreshListener.onRefresh(which);
		}
	}

	public void setAdapter(BaseAdapter adapter) {
		super.setAdapter(adapter);
	}

	private ImageView mBackToTopView;

	public void setTopViewImage(ImageView mTopViewImage) {
		this.mBackToTopView = mTopViewImage;
		mTopViewImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setSelection(0);
			}
		});
	}

	private View mTempFootView = null;
	private TextView mRefreshFooterText;

	public void addFooterView() {
		// if (!hasFooter) {
		removeFooterView();
		mTempFootView = mFootView;
		addFooterView(mFootView);
		// hasFooter = true;
		// }
	}

	// public void addEmptyView(int drawableId) {
	// removeFooterView();
	// emptyFootView.findViewById(R.id.empty_imageview).setBackgroundResource(
	// drawableId);
	// mTempFootView = emptyFootView;
	// addFooterView(mTempFootView);
	// }

	public void removeFooterView() {
		if (mTempFootView != null) {
			removeFooterView(mTempFootView);
			mTempFootView = null;
		}
	}

	@Override
	public void onClick(View arg0) {
		if (mRefreshState == DONE) {
			initRefresh(FOOTER);
		}
	}
}
