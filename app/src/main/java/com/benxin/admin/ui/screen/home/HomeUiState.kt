// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 首页 UI 状态（分类/分页/刷新一体）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 10:00:00
// | @updated   2026-06-22 10:00:00
// +----------------------------------------------------------------------
package com.benxin.admin.ui.screen.home

import com.benxin.admin.data.model.Category
import com.benxin.admin.data.model.Content

/**
 * 首页一体化状态：首屏加载 / 下拉刷新 / 上拉分页 / 分类筛选 各自独立标记，互不打架。
 */
data class HomeUiState(
    val firstLoading: Boolean = true,      // 首屏骨架（区别于下拉刷新）
    val refreshing: Boolean = false,       // 下拉刷新中
    val loadingMore: Boolean = false,      // 上拉分页加载中
    val list: List<Content> = emptyList(),
    val total: Int = 0,
    val page: Int = 0,                     // 已加载到的页码（0=未加载）
    val error: String? = null,             // 首屏错误（list 为空时才作为整屏错误）
    val loadMoreError: String? = null,     // 分页加载错误（不清空已有 list）
    val categories: List<Category> = emptyList(),
    val categoryId: Long? = null,          // null = 全部
) {
    /** 还有更多：已加载条数 < 总数。 */
    val hasMore: Boolean get() = list.size < total

    /** 整屏空态：非首屏加载、无错误、且列表为空。 */
    val isEmpty: Boolean get() = !firstLoading && error == null && list.isEmpty()
}
