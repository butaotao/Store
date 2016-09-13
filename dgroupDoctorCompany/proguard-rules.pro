# 指定代码的压缩级别
-optimizationpasses 5

# 包明不混合大小写
-dontusemixedcaseclassnames

# 不去忽略非公共的库类
-dontskipnonpubliclibraryclasses

 # 优化不优化输入的类文件
-dontoptimize

 # 预校验
-dontpreverify

 # 混淆时是否记录日志
-verbose

 # 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

# 保护注解
-keepattributes *Annotation*

# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
# 如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment



# 忽略警告
-ignorewarning

#********************记录生成的日志数据,gradle build时在本项目根目录输出
# apk 包内所有 class 的内部结构
-dump class_files.txt
# 未混淆的类和成员
-printseeds seeds.txt
# 列出从 apk 中删除的代码
-printusage unused.txt
# 混淆前后的映射
-printmapping mapping.txt
#********************记录生成的日志数据,gradle build时在本项目根目录输出


#如果引用了v4或者v7包
-dontwarn android.support.**


#保持自定义控件类不被混淆
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
-keep class * extends java.lang.annotation.Annotation
#保持view的子类成员： getter setter
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}


#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep class com.alipay.euler.andfix.**{
    *;
}
-keep class com.taobao.hotfix.aidl.**{*;}
-keep class com.ta.utdid2.device.**{*;}
-keep class com.taobao.hotfix.HotFixManager{
    public *;
}

# 避免xml里面编写onClick受影响
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

#避免混淆泛型 如果混淆报错建议关掉
-keepattributes Signature

# gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.** { *;}
#-keep class com.google.gson.examples.android.model.** { *; }

# java bean
-keep class com.dachen.imsdk.entity.** { *; }
-keep class com.dachen.imsdk.db.po.** { *; }
-keep class com.dachen.common.json.** { *; }
-keep class com.dachen.common.bean.** { *; }
-keep class com.dachen.common.toolbox.** { *; }

-keep class com.dachen.medicine.entity.** { *; }
-keep class com.dachen.mediecinelibraryrealizedoctor.entity.** { *; }
-keep class com.dachen.mediecinelibraryrealize.entity.** { *; }

-keep class com.dachen.dgroupdoctorcompany.db.dbentity.** { *; }
-keep class com.dachen.dgroupdoctorcompany.archive.entity.** { *; }
-keep class com.dachen.dgroupdoctorcompany.entity.** { *; }




#sharesdk
-keep class android.net.http.SslError
-keep class android.webkit.**{*;}
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class m.framework.**{*;}


# ormLite
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }
-keepclassmembers class * {
@com.j256.ormlite.field.DatabaseField *;
}

# eventbus
-keepclassmembers class ** {
    public void onEvent*(**);
}
-keepclassmembers class ** {
     @org.greenrobot.eventbus.Subscribe <methods>;
 }
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
     <init>(java.lang.Throwable);
 }

# butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
     @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
     @butterknife.* <methods>;
}

# volley
-keep class com.android.volley.** {*;}
-keep class com.android.volley.toolbox.** {*;}
-keep class com.android.volley.Response$* { *; }
-keep class com.android.volley.Request$* { *; }
-keep class com.android.volley.RequestQueue$* { *; }
-keep class com.android.volley.toolbox.HurlStack$* { *; }
-keep class com.android.volley.toolbox.ImageLoader$* { *; }

# fastjson
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*;}

# green dao
-keep class de.greenrobot.dao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
     public static java.lang.String TABLENAME;
 }
-keep class **$Properties

# UIL
-keep class com.nostra13.universalimageloader.** { *; }
-keepclassmembers class com.nostra13.universalimageloader.** {*;}

# pinyin4j
-dontwarn net.soureceforge.pinyin4j.**
-dontwarn demo.**
-keep class net.sourceforge.pinyin4j.** { *;}
-keep class demo.** { *;}

# Android-PullToRefresh
-dontwarn com.handmark.pulltorefresh.library.**
-keep class com.handmark.pulltorefresh.library.** { *;}
-dontwarn com.handmark.pulltorefresh.library.extras.**
-keep class com.handmark.pulltorefresh.library.extras.** { *;}
-dontwarn com.handmark.pulltorefresh.library.internal.**
-keep class com.handmark.pulltorefresh.library.internal.** { *;}

# 友盟
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep class com.umeng.**
-keep public class com.umeng.fb.ui.ThreadView {
}
-dontwarn com.umeng.**
-dontwarn org.apache.commons.**
-keep public class * extends com.umeng.**
-keep class com.umeng.** {*; }

# 百度地图
-keep class com.baidu.mapapi.** {*;}
-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}


# 内部类不能被混淆，不然fastjson报错
-keepattributes Exceptions,InnerClasses,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,Synthetic,EnclosingMethod

# 腾讯视频
-keep class com.tencent.** {*; }
-keep class tencent.tls.** {*; }
-keep class com.qq.** {*; }

# vitamio
-keep class io.vov.utils.** { *; }
-keep class io.vov.vitamio.** { *; }

# 高德地图
#3D 地图
-keep class com.amap.api.mapcore.**{*;}
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.amap.mapcore.*{*;}
#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
#搜索
-keep class com.amap.api.services.**{*;}
#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}

# 七牛
-keep class com.qiniu.**{*;}
-keep class com.qiniu.**{public <init>();}
-ignorewarnings

# okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-dontwarn okio.*

# 展视互动
-keep class com.gensee.** {*; }
