// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 内容分类模型（映射 GET /api/v1/content/categories）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 10:00:00
// +----------------------------------------------------------------------
package com.benxin.admin.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 内容分类。分类接口直接返回数组（无分页）：[{id,name,parent_id}]。
 * N1 一级平铺消费，暂不做 parent_id 树形下钻（YAGNI）。
 */
@Serializable
data class Category(
    val id: Long = 0,
    val name: String = "",
    @SerialName("parent_id") val parentId: Long = 0,
)
