// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 文章 tab UI 状态（分类/搜索/分页/刷新一体）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 10:30:00
// +----------------------------------------------------------------------
package com.benxin.admin.ui.screen.article

import com.benxin.admin.data.model.Category
import com.benxin.admin.data.model.Content

/**
 * 文章 tab 一体化状态：首屏加载 / 下拉刷新 / 上拉分页 / 分类筛选 / 关键字搜索 各自独立标记。
 * 由 N1 首页列表逻辑迁移而来，新增 keyword。
 */
data class ArticleUiState(
    val firstLoading: Boolean = true,
    val refreshing: Boolean = false,
    val loadingMore: Boolean = false,
    val list: List<Content> = emptyList(),
    val total: Int = 0,
    val page: Int = 0,
    val error: String? = null,
    val loadMoreError: String? = null,
    val categories: List<Category> = emptyList(),
    val categoryId: Long? = null, // null = 全部
    val keyword: String = "",
) {
    val hasMore: Boolean get() = list.size < total
}
