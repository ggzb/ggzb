package com.ilikezhibo.ggzb.views;

import android.content.Context;
import android.util.Log;

import com.jack.utils.Utils;
import com.ilikezhibo.ggzb.R;

/**
 * Created by stephensun on 2017/4/17.
 */
public class VariousDialog {
    Context context;
    private CustomDialog mPayDialog;
    private CustomDialog p2fDialog;
    private CustomDialog f2pDialog;

    public boolean payResult;
    public boolean f2pResult;

    public VariousDialog(Context context) {
        this.context = context;
    }

    public void payDialog() {
        mPayDialog = new CustomDialog(context, new CustomDialogListener() {
            @Override
            public void onDialogClosed(int closeType) {
                switch (closeType) {
                    case BUTTON_POSITIVE:
                        //左button
                        payResult = true;
                        if (listener != null) {
                            listener.buttonClick(payResult);
                        }
                        break;
                    case BUTTON_NEUTRAL:
                        //右button
                        payResult = false;
                        if (listener != null) {
                            listener.buttonClick(payResult);
                        }
                        break;
                }
            }
        });
        mPayDialog.setCustomMessage(Utils.trans(R.string.live_enterpay));
        mPayDialog.setCancelable(true);
        mPayDialog.setType(CustomDialog.DOUBLE_BTN);
        mPayDialog.show();
    }

    public void pay2free() {
        p2fDialog = new CustomDialog(context, new CustomDialogListener() {
            @Override
            public void onDialogClosed(int closeType) {

            }
        });
        p2fDialog.setCustomMessage(Utils.trans(R.string.live_pay2free));
        p2fDialog.setCancelable(true);
        p2fDialog.setType(CustomDialog.DOUBLE_BTN);
        p2fDialog.show();
    }

    public void free2pay() {
        f2pDialog = new CustomDialog(context, new CustomDialogListener() {
            @Override
            public void onDialogClosed(int closeType) {
                switch (closeType) {
                    case BUTTON_POSITIVE:
                        f2pResult = true;
                        if (listener != null) {
                            listener.buttonClick(f2pResult);
                        }
                        break;
                    case BUTTON_NEUTRAL:
                        f2pResult = false;
                        if (listener != null) {
                            listener.buttonClick(f2pResult);
                        }
                        break;
                    case BUTTON_NEGATIVE:
                        f2pResult = false;
                        if (listener != null) {
                            listener.buttonClick(f2pResult);
                        }
                        break;
                }

            }
        });
        f2pDialog.setCustomMessage(Utils.trans(R.string.live_free2pay));
        f2pDialog.setCancelable(false);
        f2pDialog.setCanceledOutside(false);
        f2pDialog.setType(CustomDialog.DOUBLE_BTN);
        f2pDialog.show();
    }

    private DialogListener listener;

    public interface DialogListener {
        void buttonClick(boolean payResult);
    }

    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }
}
