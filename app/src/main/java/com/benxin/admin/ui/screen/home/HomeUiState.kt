// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 首页 UI 状态（组合落地页：仅精选数据）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 10:30:00
// | @updated   2026-06-22 10:30:00（重构为组合落地页；列表逻辑迁至文章 tab）
// +----------------------------------------------------------------------
package com.benxin.admin.ui.screen.home

import com.benxin.admin.data.model.Content

/**
 * 首页状态：hero/关于/视频均写死，首页只负责「精选 6 篇」数据。
 */
data class HomeUiState(
    val firstLoading: Boolean = true,
    val featured: List<Content> = emptyList(),
    val error: String? = null,
) {
    val isEmpty: Boolean get() = !firstLoading && error == null && featured.isEmpty()
}
