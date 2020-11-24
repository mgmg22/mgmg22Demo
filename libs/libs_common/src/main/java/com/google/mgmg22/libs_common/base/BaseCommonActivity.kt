package com.google.mgmg22.libs_common.base

import android.app.Activity
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import com.google.mgmg22.libs_common.R
import com.google.mgmg22.libs_common.dialog.CustomProgressDialog
import com.google.mgmg22.libs_common.helper.StatusBarUtils
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_base.*


/**
 * @Description:
 * @Author:         沈晓顺
 * @CreateDate:     2019-12-05 14:00
 */
abstract class BaseCommonActivity : AppCompatActivity() {
    /**
     * 设置布局文件
     */
    protected abstract fun getLayoutResID(): Int

    /**
     * 控制是否需要重设状态栏颜色
     */
    protected open var isNeedAdjustStatusBar = true

    protected val mProgressDialog by lazy { CustomProgressDialog(this) }
    protected lateinit var rightText: TextView
    protected lateinit var shareIv: ImageView

    //TODO 待优化
    protected lateinit var postionView: View
    protected lateinit var mToolbar: Toolbar
    protected lateinit var baseLine: View

    //RxJava2
    private val disposes = CompositeDisposable()

    override fun onStop() {
        disposes.clear()
        super.onStop()
    }

    protected fun Disposable.addDispose() {
        disposes.add(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResID())
    }

    /**
     * 初始化，绑定点击事件等
     */
    protected open fun initView() {}

    override fun setContentView(layoutResID: Int) {
        super.setContentView(R.layout.activity_base)
        toolbar.title = ""
        mToolbar = toolbar
        baseLine = base_line
        postionView = position_view
        shareIv = share_iv
        rightText = toolbar_right_text
        toolbar_title.text = title
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowTitleEnabled(false)
        }
        if (isNeedAdjustStatusBar) {
            position_view.isGone = false
            StatusBarUtils.adjustStatusBar(this, position_view)
            StatusBarUtils.setDarkMode(this, true, position_view)
        }
        setMobileContentView(LayoutInflater.from(this).inflate(layoutResID, null))
        initView()
    }

    fun setShareOnClickListener(listener: View.OnClickListener?) {
        shareIv.visibility = View.VISIBLE
        shareIv.setOnClickListener(listener)
    }

    @Deprecated("禁止使用！！", ReplaceWith("setToolbarTitle"))
    override fun setTitle(title: CharSequence?) {
        toolbar_title.text = title
    }

    fun setToolbarTitle(title: CharSequence?) {
        toolbar_title.text = title
    }

    protected fun setToolbarTitleColor(@ColorInt color: Int) {
        toolbar_title.setTextColor(color)
    }

    override fun setContentView(view: View) {
        super.setContentView(R.layout.activity_base)
        StatusBarUtils.adjustStatusBar(this, position_view)
        setMobileContentView(view)
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        super.setContentView(R.layout.activity_base)
        StatusBarUtils.adjustStatusBar(this, position_view)
        setMobileContentView(view)
    }

    protected fun setTitleRight(rightStr: String, extraOnClickListener: () -> Unit) {
        rightText.isGone = rightStr.isNullOrEmpty()
        rightText.text = rightStr
        rightText.setOnClickListener { extraOnClickListener() }
    }

    private fun setMobileContentView(view: View) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val container = findViewById<FrameLayout>(R.id.base_container)
        val layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        container.addView(view, 0, layoutParams)
    }

    fun showActionbar() {
        supportActionBar!!.show()
    }

    fun hiddenActionbar() {
//        supportActionBar!!.hide()
        toolbar.visibility = View.GONE
        base_line.visibility = View.GONE
    }

    protected open fun setToolbarCustomTheme(@ColorInt color: Int) {
        val upArrow = ContextCompat.getDrawable(this, R.drawable.base_back_icon)
        if (upArrow != null) {
            upArrow.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            if (supportActionBar != null) {
                supportActionBar!!.setHomeAsUpIndicator(upArrow)
            }
        }
    }

    protected fun darkenBackgroud(bgcolor: Float?) {
        val lp = window.attributes
        lp.alpha = bgcolor!!
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        window.attributes = lp
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // 点击返回图标事件
                setResult(Activity.RESULT_OK)
                finish()
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopProgressDialog()
    }

    /**
     * 显示进度条
     */
    fun startProgressDialog() {
        if (isFinishing) {
            return
        }
        mProgressDialog.setCanceledOnTouchOutside(false)
        if (!mProgressDialog.isShowing) {
            mProgressDialog.show()
        }
    }

    /**
     * 关闭进度条
     */
    fun stopProgressDialog() {
        if (mProgressDialog.isShowing) {
            mProgressDialog.dismiss()
        }
    }
}