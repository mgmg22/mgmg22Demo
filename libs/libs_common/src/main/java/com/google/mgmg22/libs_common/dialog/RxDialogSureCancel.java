package com.google.mgmg22.libs_common.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.mgmg22.libs_common.R;


/**
 * @author vondear
 * @date 2016/7/19
 * 确认 取消 Dialog
 */
public class RxDialogSureCancel extends RxDialog {

    private ImageView mIvLogo;
    private TextView mTvhead;
    private TextView mTvContent;
    private TextView mTvSure;
    private TextView mTvCancel;
    private TextView mTvTitle;

    public RxDialogSureCancel(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public RxDialogSureCancel(Context context) {
        super(context);
        initView();
    }

    public RxDialogSureCancel(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView();
    }

    public RxDialogSureCancel setIcon(int resID) {
        mIvLogo.setImageResource(resID);
        return this;
    }

    public RxDialogSureCancel setTitle(String title) {
        mTvTitle.setText(title);
        return this;
    }

    public RxDialogSureCancel setHead(String content) {
        this.mTvhead.setText(content);
        this.mTvhead.setVisibility(View.VISIBLE);
        return this;
    }

    public RxDialogSureCancel setContent(String content) {
        this.mTvContent.setText(content);
        if (content.isEmpty()) {
            this.mTvContent.setVisibility(View.GONE);
        }
        return this;
    }

    public RxDialogSureCancel setSure(String strSure) {
        this.mTvSure.setText(strSure);
        return this;
    }

    public RxDialogSureCancel setCancel(String strCancel) {
        this.mTvCancel.setText(strCancel);
        return this;
    }

    public RxDialogSureCancel setSureListener(View.OnClickListener sureListener) {
        mTvSure.setOnClickListener(sureListener);
        return this;
    }

    public RxDialogSureCancel setCancelListener(View.OnClickListener cancelListener) {
        mTvCancel.setOnClickListener(cancelListener);
        return this;
    }

    private void initView() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_sure_false, null);
        mIvLogo = dialogView.findViewById(R.id.iv_logo);
        mTvhead = dialogView.findViewById(R.id.tv_icon_info);
        mTvSure = dialogView.findViewById(R.id.tv_sure);
        mTvCancel = dialogView.findViewById(R.id.tv_cancel);
        mTvContent = dialogView.findViewById(R.id.tv_content);
        mTvContent.setTextIsSelectable(true);
        mTvTitle = dialogView.findViewById(R.id.tv_title);
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultCancel();
            }
        });
        setContentView(dialogView);
    }

    private void defaultCancel() {
        this.cancel();
    }
}
