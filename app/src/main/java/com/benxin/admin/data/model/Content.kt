// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 内容模型（映射 GET /api/v1/contents[/:id]）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-21 23:12:00
// +----------------------------------------------------------------------
package com.benxin.admin.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 内容项。字段对齐后端实际契约（N0 实测）：
 * - 列表项：id / category_id / title / cover / summary / author / source /
 *   is_top / view_count / publish_at / created_at
 * - 详情项：在列表项基础上多 content（富文本 HTML，N2 渲染）
 *
 * cover 为相对路径（如 "/static/demo/covers/xxx.png"）或完整 URL，
 * 由前端按 BASE_URL 补全（见 [com.benxin.admin.data.api.ApiClient.absoluteUrl]）。
 */
@Serializable
data class Content(
    val id: Long = 0,
    @SerialName("category_id") val categoryId: Long = 0,
    val title: String = "",
    val cover: String? = null,
    val summary: String? = null,
    /** 富文本正文，仅详情接口返回（N2 做渲染） */
    val content: String? = null,
    val author: String? = null,
    val source: String? = null,
    @SerialName("is_top") val isTop: Int = 0,
    @SerialName("view_count") val viewCount: Int = 0,
    @SerialName("publish_at") val publishAt: String? = null,
    @SerialName("created_at") val createdAt: String? = null,
) {
    val isTopped: Boolean get() = isTop == 1
}
