package com.ilikezhibo.ggzb.login;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.jack.lib.AppException;
import com.jack.lib.net.RequestInformation;
import com.jack.lib.net.callback.JsonCallback;
import com.jack.utils.AppConstants;
import com.jack.utils.JsonParser;
import com.jack.utils.MobileConfig;
import com.jack.utils.Trace;
import com.jack.utils.UrlHelper;
import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.AULiveApplication;
import com.ilikezhibo.ggzb.BaseFragmentActivity;
import com.ilikezhibo.ggzb.R;
import com.ilikezhibo.ggzb.home.AULiveHomeActivity;
import com.ilikezhibo.ggzb.login.entity.QQTokenEntity;
import com.ilikezhibo.ggzb.login.entity.WeiXinTokenEntity;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import de.greenrobot.event.EventBus;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;
import tinker.android.util.TinkerManager;

public class LoginMainActivity extends BaseFragmentActivity implements
		OnClickListener {

	private Button register_phone;
	private Button register_weixin;
	private Button register_qq;

	private boolean back_home = true;

	public static String back_home_key = "back_home_key";

	@Override
	protected void setContentView() {
		setContentView(R.layout.activity_register_main);
		Button rl_back = (Button) this.findViewById(R.id.back);
		rl_back.setOnClickListener(this);

		TextView tv_title = (TextView) this.findViewById(R.id.title);
		tv_title.setText("登陆");

		back_home = getIntent().getBooleanExtra(back_home_key, true);

		register_phone = (Button) findViewById(R.id.register_phone);
		register_phone.setOnClickListener(this);

		register_weixin = (Button) findViewById(R.id.register_weixin);
		register_weixin.setOnClickListener(this);

		register_qq = (Button) findViewById(R.id.register_qq);
		register_qq.setOnClickListener(this);

		//EventBus.getDefault().register(this, "weixn_register", WeiXinRegEvent.class);


	}

	@Override
	protected void initializeViews() {

	}

	@Override
	protected void initializeData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_phone:
			Intent login_intent = new Intent(LoginMainActivity.this,
					LoginActivity.class);
			login_intent.putExtra(LoginActivity.back_home_key, back_home);
			startActivity(login_intent);
			break;
		case R.id.register_qq:
			qqLogin();
			break;
		case R.id.register_weixin:
			IWXAPI wxApi = WXAPIFactory.createWXAPI(this,
					AppConstants.WEI_XIN_ID, true);
			wxApi.registerApp(AppConstants.WEI_XIN_ID);

			SendAuth.Req req = new SendAuth.Req();
			req.scope = "snsapi_userinfo";
			req.state = "wechat_sdk_demo_test";
			wxApi.sendReq(req);

			break;

		case R.id.back:
			this.finish();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	// 微信回调处理
	private void weixn_register(WeiXinRegEvent token_event) {

		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ AppConstants.WEI_XIN_ID + "&secret="
				+ AppConstants.WEI_XIN_SCRET + "&code=" + token_event.token
				+ "&grant_type=authorization_code";

		RequestInformation request = new RequestInformation(url,
				RequestInformation.REQUEST_METHOD_GET);

		Trace.d("token url:" + url);
		request.setCallback(new JsonCallback<WeiXinTokenEntity>() {

			@Override
			public void onCallback(WeiXinTokenEntity callback) {

				if (callback != null && callback.getAccess_token() != null) {
					register_next(callback.getAccess_token());
				}
			}

			@Override
			public void onFailure(AppException e) {
			}

		}.setReturnType(WeiXinTokenEntity.class));
		request.execute();
	}

	// 获取微信资料
	private void register_next(String token_event) {

		String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
				+ token_event + "&openid=" + AppConstants.WEI_XIN_ID
				+ "&secret=" + AppConstants.WEI_XIN_SCRET;

		RequestInformation request = new RequestInformation(url,
				RequestInformation.REQUEST_METHOD_GET);

		Trace.d("token url:" + url);
		request.setCallback(new JsonCallback<WeiXinTokenEntity>() {

			@Override
			public void onCallback(WeiXinTokenEntity callback) {
				doOtherLogin(callback.getSex(), callback.getNickname(),
						callback.getHeadimgurl(), "weixin",
						callback.getUnionid());
			}

			@Override
			public void onFailure(AppException e) {
			}

		}.setReturnType(WeiXinTokenEntity.class));
		request.execute();
	}

	// if (back_home) {
	// startActivity(new Intent(LoginActivity.this,
	// baobaoHomeActivity.class));
	// } else {
	// LoginActivity.this.finish();
	// }
	Tencent mTencent = null;
	String openidString = null;

	// QQ授权登陆
	public void qqLogin() {
		mTencent = Tencent.createInstance(AppConstants.QQ_APP_ID,
				this.getApplicationContext());
		if (!mTencent.isSessionValid()) {
			mTencent.login(this, "all", new IUiListener() {

				@Override
				public void onError(UiError arg0) {

				}

				@Override
				public void onComplete(Object response) {

					try {
						openidString = ((JSONObject) response)
								.getString("openid");
						Trace.d("openid:" + openidString);
						// access_token= ((JSONObject)
						// response).getString("access_token"); //expires_in =
						// ((JSONObject) response).getString("expires_in");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					/**
					 * 到此已经获得OpneID以及其他你想获得的内容了
					 * QQ登录成功了，我们还想获取一些QQ的基本信息，比如昵称，头像什么的，这个时候怎么办？
					 * sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息
					 * 如何得到这个UserInfo类呢？
					 */
					QQToken qqToken = mTencent.getQQToken();
					UserInfo info = new UserInfo(getApplicationContext(),
							qqToken);
					// 这样我们就拿到这个类了，之后的操作就跟上面的一样了，同样是解析JSON

					info.getUserInfo(new IUiListener() {

						@Override
						public void onError(UiError arg0) {

						}

						@Override
						public void onComplete(Object userInfo1) {
							JSONObject userInfo = (JSONObject) userInfo1;
							QQTokenEntity qqTokenEntity = JsonParser
									.deserializeByJson(userInfo.toString(),
											QQTokenEntity.class);
							Trace.d(userInfo.toString());
							String sex1 = qqTokenEntity.getGender();
							String sex = null;
							if (sex != null && sex1.equals("男")) {
								sex = "1";
							} else {
								sex = "2";
							}
							doOtherLogin(qqTokenEntity.getGender(),
									qqTokenEntity.getNickname(),
									qqTokenEntity.getFigureurl_qq_2(), "qq",
									openidString);
						}

						@Override
						public void onCancel() {

						}
					});

				}

				@Override
				public void onCancel() {

				}
			});
		}
	}

	private void doOtherLogin(String sex, String nickname, String face,
			String type, String unique_flag) {
		showProgressDialog("正在登录" + Utils.trans(R.string.app_name) + "，请稍候...");
		String url = null;
		try {
			MobileConfig config = MobileConfig.getMobileConfig(this);
			url = UrlHelper.URL_HEAD + "/reg/sync_other_login?udid="
					+ config.getIemi() + "&sex=" + sex + "&nickname="
					+ URLEncoder.encode(nickname, "utf-8") + "&face=" + face
					+ "&type=" + type + "&unique_flag=" + unique_flag;

			AULiveApplication application = (AULiveApplication) TinkerManager.getTinkerApplicationLike();
			if (application.getAddress() != null) {
				url = url + "&lat=" + application.getLatitude() + "&lng="
						+ application.getLatitude() + "&city="
						+ URLEncoder.encode(application.getCity(), "utf-8");
			}

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		RequestInformation request = new RequestInformation(url,
				RequestInformation.REQUEST_METHOD_GET);

		request.setCallback(new JsonCallback<com.ilikezhibo.ggzb.entity.UserInfo>() {

			@Override
			public void onCallback(com.ilikezhibo.ggzb.entity.UserInfo callback) {
				cancelProgressDialog();
				if (callback == null) {
					Utils.showMessage("获取数据失败");
					return;
				}

				if (callback.getStat() == 200) {
					if (callback.getUserinfo() != null) {
						AULiveApplication.setUserInfo(callback.getUserinfo());

						// 缓存手机号，用户名
						// SharedPreferenceTool.getInstance().saveString(
						// SharedPreferenceTool.LOGIN_USER_PHONE, account);
					}

					if (back_home) {
						startActivity(new Intent(LoginMainActivity.this,
								AULiveHomeActivity.class));
					} else {
						LoginMainActivity.this.finish();
					}
				} else {
					Utils.showMessage(callback.getMsg());
				}
			}

			@Override
			public void onFailure(AppException e) {
				cancelProgressDialog();
				Utils.showMessage("获取网络数据失败");
			}
		}.setReturnType(com.ilikezhibo.ggzb.entity.UserInfo.class));
		request.execute();
	}
}
