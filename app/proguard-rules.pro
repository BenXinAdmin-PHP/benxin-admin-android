# +----------------------------------------------------------------------
# | @project   BenXinAdmin · 原生 Android 门面（N0 脚手架）
# | release 收缩规则（N0 仅自测 debug，release 规则先就位）
# +----------------------------------------------------------------------

# ---- kotlinx.serialization ----
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**
-keepclassmembers class **$$serializer { *; }
# 保留所有 @Serializable 数据模型及其生成的 serializer
-keep,includedescriptorclasses class com.benxin.admin.data.model.**$$serializer { *; }
-keepclassmembers class com.benxin.admin.data.model.** {
    *** Companion;
}
-keepclasseswithmembers class com.benxin.admin.data.model.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# ---- Retrofit / OkHttp（R8 已内置大部分规则，补充保险）----
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**
