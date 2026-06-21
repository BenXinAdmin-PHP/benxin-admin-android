// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 统一响应信封模型（业务码风格 A）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-21 23:12:00
// +----------------------------------------------------------------------
package com.benxin.admin.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 后端统一返回信封：{ code, msg, data, request_id, timestamp }。
 * code=0 表示业务成功（HTTP 默认 200）。
 */
@Serializable
data class ApiEnvelope<T>(
    val code: Int = -1,
    val msg: String = "",
    val data: T? = null,
    @SerialName("request_id") val requestId: String? = null,
    val timestamp: Long = 0,
)
