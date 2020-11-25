package com.google.mgmg22.libs_common.ext

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.mgmg22.libs_common.base.BaseApplication
import com.google.mgmg22.lib_util.LogUtilsKt
import java.text.DecimalFormat

/**
 * 对系统组件的扩展方法写在这里
 */

/****************************************************************
 * 工具类扩展（log toast json）
 ****************************************************************/
fun toast(msg: String?) = Toast.makeText(BaseApplication.getContext(),msg, Toast.LENGTH_SHORT).show()


fun Context.toast(@StringRes resId: Int) = toast(getString(resId))

fun Fragment.toast(@StringRes resId: Int) = toast(getString(resId))

/**
 * dp和像素转换
 */
fun dp2px(dipValue: Float): Int {
    val context: Context = BaseApplication.getContext()
    val m = context.resources.displayMetrics.density
    return (dipValue * m + 0.5f).toInt()
}

fun Any.logD(msg: String?) = LogUtilsKt.e(msg ?: "Log为null")

// 封装了`Gson.fromJson(String json , Class<T> classOf)`方法,反序列化json
inline fun <reified T : Any> Gson.fromJson(json: String): T = fromJson(json, object : TypeToken<T>() {}.type)

/****************************************************************
 * context扩展方法
 ****************************************************************/
fun Context.getCompactColor(@ColorRes colorRes: Int): Int = ContextCompat.getColor(this, colorRes)

fun Context.getCompactDrawable(@DrawableRes drawableRes: Int): Drawable? = ContextCompat.getDrawable(this, drawableRes)

/****************************************************************
 * View扩展方法
 ****************************************************************/
fun EditText.value() = text.toString()


fun RecyclerView.bindAdapter(adp: RecyclerView.Adapter<*>) {
    run {
        layoutManager = LinearLayoutManager(this.context)
        adapter = adp
    }
}

fun <T> toArray(list: MutableList<T>) = list.toString().substring(1, list.toString().length - 1).replace(" ", "")

//inline fun <reified T : Any> BaseEasyAdapter<T>.bindOnItemClickListener(crossinline onItemClickListener: (T) -> Unit): BaseEasyAdapter<T> {
//    this.setOnItemClickListener { _, _, position ->
//        onItemClickListener.invoke(this.data[position])
//    }
//    return this
//}
//
//@Deprecated("不安全，禁止使用")
//inline fun <reified T : Any> BaseEasyAdapter<T>.bindOnItemLongClickListener(crossinline onItemLongClick: (T) -> Unit): BaseEasyAdapter<T> {
//    this.setOnItemLongClickListener { _, _, position ->
//        onItemLongClick.invoke(this.data[position])
//        false
//    }
//    return this
//}
//
////图片
//fun BaseViewHolder.setImageUrl(@IdRes idRes: Int, url: String?): BaseViewHolder {
//    val imageView = getView<ImageView>(idRes)
//    imageView.loadUrl(url)
//    return this
//}

fun TextView.setPrice(text: String?): TextView {
    val sMoney: String = "¥ $text"
    setText(sMoney)
    typeface = Typeface.createFromAsset(BaseApplication.getContext().assets, "fonts/DIN Alternate Bold.ttf")
    return this
}

fun TextView.setPrice(price: Double?): TextView {
    val format = DecimalFormat("0.00")
    val sMoney: String = "¥ " + format.format(price)
    text = sMoney
    typeface = Typeface.createFromAsset(BaseApplication.getContext().assets, "fonts/DIN Alternate Bold.ttf")
    return this
}