package com.google.mgmg22.libs_common.base

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.mgmg22.libs_common.dialog.CustomProgressDialog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @Author:         luchong
 * @CreateDate:     2019/7/24 10:15
 */
abstract class BaseCommonFragment : Fragment() {
    protected lateinit var mContext: Context
    private val disposes = CompositeDisposable()
    protected var mProgressDialog: Dialog? = null
    protected abstract fun getLayoutId(): Int

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mContext = activity ?: throw Exception("activity 为null")
        initView()
    }

    /**
     * 初始化，绑定点击事件等
     */
    protected open fun initView() {}

    protected fun Disposable.addDispose() {
        disposes.add(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        this.disposes.clear()
    }

    /** 显示进度条  */
    open fun startProgressDialog() {
        if (activity!!.isFinishing) {
            return
        }
        if (mProgressDialog == null) {
            mProgressDialog = CustomProgressDialog(activity)
        }
        if (!mProgressDialog!!.isShowing) {
            mProgressDialog!!.show()
        }
    }

    /** 关闭进度条  */
    open fun stopProgressDialog() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.dismiss()
        }
    }
}
