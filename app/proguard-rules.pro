-dontskipnonpubliclibraryclasses # 不忽略非公共的库类
-optimizationpasses 5            # 指定代码的压缩级别
-dontusemixedcaseclassnames      # 是否使用大小写混合
-dontpreverify                   # 混淆时是否做预校验
-verbose                         # 混淆时是否记录日志
-keepattributes *Annotation*     # 保持注解

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

#保持哪些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

#生成日志数据，gradle build时在本项目根目录输出
-dump class_files.txt            #apk包内所有class的内部结构
-printseeds seeds.txt            #未混淆的类和成员
-printusage unused.txt           #打印未被使用的代码
-printmapping mapping.txt        #混淆前后的映射

-keep public class * extends android.support.** #如果有引用v4或者v7包，需添加
-keep public class * extends androidx.** #如果有引用v4或者v7包，需添加




#避免混淆泛型 如果混淆报错建议关掉
#–keepattributes Signature

-keepnames class * implements java.io.Serializable #不混淆Serializable

-keepclassmembers class **.R$* { #不混淆资源类
　　public static <fields>;
}
-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {      # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {      # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {             # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {         # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}


#gson
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }




# 微信混淆
-keep class com.tencent.mm.opensdk.** {*;}
# qq混淆
-dontwarn com.tencent.**
-keep class com.tencent.** {*;}
# 友盟混淆
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
# 极光推送混淆
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

-keep public class cn.jiguang.analytics.android.api.** {*;}
# 导入第三方libaray混淆
-keep class me.iwf.photopicker.** {*;}

# 美团robust不混淆
-keep class com.meituan.robust.** {*;}


-dontwarn top.zibin.luban.**
-keep class top.zibin.luban.** { *; }
-keepclassmembers class rx.internal.util.unsafe.** {
    long producerIndex;
    long consumerIndex;
}

#支付宝混淆
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}

#魅族推送混淆
-keep class com.meizu.cloud.pushsdk.** { *; }
#小米推送混淆
-dontwarn com.xiaomi.push.**
-keep class com.xiaomi.push.**{*;}
# OPPO推送混淆
-dontwarn com.coloros.mcsdk.**
-keep class com.coloros.mcsdk.** { *; }
-dontwarn com.heytap.**
-keep class com.heytap.** { *; }
-dontwarn com.mcs.**
-keep class com.mcs.** { *; }
# VIVO推送混淆
-dontwarn com.vivo.push.**
-keep class com.vivo.push.**{*; }
-keep class com.vivo.vms.**{*; }
#华为HMS推送混淆
-keepattributes SourceFile,LineNumberTable
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}
-dontwarn com.huawei.**
-keep public class * extends android.app.Activity
-keep interface com.huawei.android.hms.agent.common.INoProguard {*;}
-keep class * extends com.huawei.android.hms.agent.common.INoProguard {*;}
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
# hmscore-support: remote transport
-keep class * extends com.huawei.hms.core.aidl.IMessageEntity { *; }
# hmscore-support: remote transport
-keepclasseswithmembers class * implements com.huawei.hms.support.api.transport.DatagramTransport {
 <init>(...);
}
# manifest: provider for updates
-keep public class com.huawei.hms.update.provider.UpdateProvider { public *; protected *; }
#glide混淆
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#event bus混淆
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
# adapter混淆
-keep class com.chad.library.adapter.** {
*;
}
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keepclassmembers  class **$** extends com.chad.library.adapter.base.BaseViewHolder {
     <init>(...);
}

# bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

# AndPermission
-dontwarn com.yanzhenjie.permission.**

# ARouter
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# If you use the byType method to obtain Service, add the following rules to protect the interface:
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# If single-type injection is used, that is, no interface is defined to implement IProvider, the following rules need to be added to protect the implementation
# -keep class * implements com.alibaba.android.arouter.facade.template.IProvider

-keep class com.shuyu.gsyvideoplayer.video.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.**
-keep class com.shuyu.gsyvideoplayer.video.base.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.base.**
-keep class com.shuyu.gsyvideoplayer.utils.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.utils.**
-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}


#获取OAID
-keep class com.bun.miitmdid.core.** {*;}
#新增OAID混淆，防止三星10崩溃
-keep class com.bun.** {*;}
-keep class com.asus.msa.** {*;}
-keep class com.heytap.openid.** {*;}
-keep class com.huawei.android.hms.pps.** {*;}
-keep class com.meizu.flyme.openidsdk.** {*;}
-keep class com.samsung.android.deviceidservice.** {*;}
-keep class com.zui.** {*;}
-keep class com.huawei.hms.ads.** {*; }
-keep interface com.huawei.hms.ads.** {*; }
-keepattributes *Annotation*
-keep @android.support.annotation.Keep class **{
      @android.support.annotation.Keep <fields>;
      @android.support.annotation.Keep <methods>;
}
-keep @androidx.annotation.Keep class **{
      @androidx.annotation.Keep <fields>;
      @androidx.annotation.Keep <methods>;
}

##gradle3.6以上 默认启用R8优化,ChangeQuickRedirect变量会被优化掉,需要keep
#-keepclassmembers class **{ public static com.meituan.robust.ChangeQuickRedirect *; }
#


##阿里云一键登录
#-keep class cn.com.chinatelecom.gateway.lib.** {*;}
#-keep class com.unicom.xiaowo.login.** {*;}
#-keep class com.cmic.sso.sdk.** {*;}
#-keep class com.mobile.auth.** {*;}
#-keep class android.support.v4.** { *;}
#-keep class org.json.**{*;}
#-keep class com.alibaba.fastjson.** {*;}
##魅族
#-dontwarn com.meizu.cloud.pushsdk.**
#-keep class com.meizu.cloud.pushsdk.**{*;}
##小米
#-keep class com.xiaomi.**{*;}
#-keep public class * extends com.xiaomi.mipush.sdk.PushMessageReceiver
##vivo
#-dontwarn com.vivo.push.**
#-keep class com.vivo.push.**{*; }
#-keep class com.vivo.vms.**{*; }
#-keep class com.tencent.android.vivopush.VivoPushMessageReceiver{*;}
##oppo
#-keep class com.heytap.mcssdk.** {*;}
#
##腾讯地图
#-keep class com.tencent.tencentmap.**{*;}
#-keep class com.tencent.map.**{*;}
#-keep class com.tencent.beacontmap.**{*;}
#-keep class navsns.**{*;}
#-dontwarn com.qq.**
#-dontwarn com.tencent.**
#
##svga相关的混淆
#-keep class com.squareup.wire.** { *; }
#-keep class com.opensource.svgaplayer.proto.* { *; }
