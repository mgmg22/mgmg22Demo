package com.google.mgmg22.libs_common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.mgmg22.libs_common.R;


/**
 * @author zhangll
 */
public class CustomProgressDialog extends Dialog {

    private String loadingMessage;
    private final TextView messageTv;

    public CustomProgressDialog(Context context) {
        this(context, R.style.CustomDialog);
    }

    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.layout_progress_dialog);
        messageTv = findViewById(R.id.id_tv_loadingmsg);
        getWindow().getAttributes().gravity = Gravity.CENTER;
    }

    public void setLoadingMessage(String loadingMessage) {
        this.loadingMessage = loadingMessage;

        if (!TextUtils.isEmpty(loadingMessage) && messageTv != null) {
            messageTv.setText(loadingMessage);
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        ImageView progressImage = findViewById(R.id.myloading_image_id);
        AnimationDrawable animationDrawable = (AnimationDrawable) progressImage.getDrawable();
        animationDrawable.start();
    }
}
