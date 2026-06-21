// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 网络客户端单例（OkHttp + Retrofit + kotlinx.serialization）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-21 23:12:00
// +----------------------------------------------------------------------
package com.benxin.admin.data.api

import com.benxin.admin.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 手动构造的网络单例（N0 不引 Hilt，避免过度设计）。
 * BASE_URL 取自 BuildConfig（debug=http://10.0.2.2:8801）；日志拦截器仅 debug 挂载。
 */
object ApiClient {

    private val json = Json {
        ignoreUnknownKeys = true   // 防后端字段漂移
        coerceInputValues = true   // null 落到默认值，增强健壮性
        explicitNulls = false
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            }
        }
        .build()

    /** 规整后的 baseUrl（保证以 "/" 结尾，Retrofit 要求） */
    val baseUrl: String = BuildConfig.BASE_URL.trimEnd('/') + "/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val contentApi: ContentApi = retrofit.create(ContentApi::class.java)

    /**
     * 将后端返回的相对路径（如 "/static/..."）补全为可访问完整 URL；
     * 已是 http(s) 绝对地址则原样返回；空值返回 null。
     */
    fun absoluteUrl(path: String?): String? {
        if (path.isNullOrBlank()) return null
        if (path.startsWith("http://") || path.startsWith("https://")) return path
        return baseUrl.trimEnd('/') + "/" + path.trimStart('/')
    }
}
