@file:JvmName("ViewExtensions")

package com.google.mgmg22.extensions


import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.ImageSpan
import android.view.View
import android.widget.EditText
import android.widget.TextView

fun View.onSafeClick(interval: Long, onClick: (View?) -> Unit, onIntercept: (View?) -> Unit) {
    setOnClickListener(object : SafeOnClickListener(interval) {
        override fun onSafeClick(v: View?) {
            onClick(v)
        }

        override fun onInterceptClick(v: View?) {
            onIntercept(v)
        }
    })
}

fun View.onSafeClick(onClick: (View?) -> Unit, onIntercept: (View?) -> Unit) {
    setOnClickListener(object : SafeOnClickListener(800) {
        override fun onSafeClick(v: View?) {
            onClick(v)
        }

        override fun onInterceptClick(v: View?) {
            onIntercept(v)
        }
    })
}

fun View.onSafeClick(interval: Long, onClick: (View?) -> Unit) {
    setOnClickListener(object : SafeOnClickListener(interval) {
        override fun onSafeClick(v: View?) {
            onClick(v)
        }
    })
}

fun View.onSafeClick(onClick: (View?) -> Unit) {
    setOnClickListener(object : SafeOnClickListener(800) {
        override fun onSafeClick(v: View?) {
            onClick(v)
        }
    })
}

fun View.onSafeClick(onClickListener: SafeOnClickListener) {
    setOnClickListener(onClickListener)
}

/**
 * 能自动解除的delay task
 */
fun View.safePostDelayed(delay: Long, runnable: () -> Unit): Cancelable {
    val task = Runnable { runnable() } //必须包裹,否则取消时会新new Runnable
    addOnAttachStateChangeListener(object : SimpleOnAttachStateChangeListener() {
        override fun onViewDetachedFromWindow(v: View?) {
            v?.removeCallbacks(task)
            removeOnAttachStateChangeListener(this)
        }
    })
    postDelayed(task, delay)
    return TaskCancel(this, task)
}


/**
 * 自动解除的循环任务,支持手动取消
 *
 * times:可设置执行次数,不设或者<0 表示无限制次数
 * delay:延迟x ms开始任务
 * period:间隔
 * runnable:执行的具体任务
 * return: Cancelable
 */
fun View.safeIntervalTask(
    times: Int = 0,
    delay: Long = 0L,
    period: Long,
    runnable: () -> Unit
): Cancelable {
    var hasDone = 0
    var maxTime = -1
    if (times > 0) {
        maxTime = times
    }
    var task: Runnable? = null
    task = Runnable {
        runnable()
        hasDone++
        if (maxTime == -1 || hasDone < maxTime) {
            postDelayed(task, period)
        }

    }
    addOnAttachStateChangeListener(object : SimpleOnAttachStateChangeListener() {
        override fun onViewDetachedFromWindow(v: View?) {
            v?.removeCallbacks(task)
            removeOnAttachStateChangeListener(this)
        }
    })
    postDelayed(task, delay)
    return TaskCancel(this, task)
}


/**
 * 自动解除的倒计时任务
 * millsInFuture:总时长 ms
 * countDownInterval:间隔 ms
 * onTick:间隔回调
 * onFinish:结束回调
 * onCancel:未到时间时,取消回调
 */
fun View.safeCountDownTask(
    millsInFuture: Long,
    countDownInterval: Long,
    onTick: ((timer: CountDownTimer, millisUntilFinished: Long) -> Unit)? = null,
    onFinish: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null
): CountDownTimer? {
    var isFinishInvoked = false
    var timer: CountDownTimer? = object : CountDownTimer(millsInFuture, countDownInterval) {
        override fun onFinish() {
            onFinish?.invoke()
            isFinishInvoked = true
        }

        override fun onTick(millisUntilFinished: Long) {
            onTick?.invoke(this, millisUntilFinished)
        }
    }

    addOnAttachStateChangeListener(object : SimpleOnAttachStateChangeListener() {
        override fun onViewDetachedFromWindow(v: View?) {
            timer?.cancel()
            timer = null
            if (!isFinishInvoked) onCancel?.invoke()
            removeOnAttachStateChangeListener(this)
        }
    })

    return timer?.apply { start() }
}

fun EditText.watch(action: TextWatcherImp.() -> Unit) {
    TextWatcherImp().apply { action() }.let { watcher ->
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                watcher.after?.invoke(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                watcher.before?.invoke(s, start, count, after)
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                watcher.on?.invoke(s, start, before, count)
            }

        })
    }
}

/**
 * 简单图文混编富文本,让icon与文字居中对齐
 */
fun TextView.setImageSpanText(
    text: String,
    drawble: Drawable?,
    start: Int,
    end: Int,
    flag: Int = Spanned.SPAN_INCLUSIVE_EXCLUSIVE
) {
    if (drawble == null) return
    setText(SpannableString(text).apply {
        drawble.setBounds(0, 0, drawble.intrinsicWidth, drawble.intrinsicHeight)
        setSpan(CenterAlignImageSpan(drawble, ImageSpan.ALIGN_BASELINE), start, end, flag)
    })
}

/******************  classes followed *************************/

/**
 * Created on 2019-09-26,@author Zen.
 * E-mail:jyzen@foxmail.com
 * This is ground control to Major Tom.
 * Don't panic.
 */
class CenterAlignImageSpan : ImageSpan {
    constructor(d: Drawable?) : super(d!!)
    constructor(d: Drawable?, verticalAlignment: Int) : super(d!!, verticalAlignment)

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        val drawable = drawable
        val fm = paint.fontMetricsInt
        //计算y方向的位移
        val translationY = (y + fm.descent + y + fm.ascent) / 2 - drawable.bounds.bottom / 2
        canvas.save()
        //绘制图片位移一段距离
        canvas.translate(x, translationY.toFloat())
        drawable.draw(canvas)
        canvas.restore()
    }
}

class TextWatcherImp {
    var after: ((Editable?) -> Unit)? = null
    var before: ((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null
    var on: ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null

    fun watchAfter(action: (Editable?) -> Unit) {
        after = action
    }

    fun watchBefore(action: (s: CharSequence?, start: Int, count: Int, after: Int) -> Unit) {
        before = action
    }

    fun watchOn(action: (s: CharSequence?, start: Int, before: Int, count: Int) -> Unit) {
        on = action
    }
}

abstract class SimpleOnAttachStateChangeListener : View.OnAttachStateChangeListener {
    override fun onViewAttachedToWindow(v: View?) {

    }
}

class TaskCancel(val view: View, val runnable: Runnable) : Cancelable {
    override fun cancel() {
        view.removeCallbacks(runnable)
    }

}

interface Cancelable {
    fun cancel()
}
