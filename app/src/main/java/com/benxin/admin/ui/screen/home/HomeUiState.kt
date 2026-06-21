// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 首页 UI 状态
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-21 23:16:00
// +----------------------------------------------------------------------
package com.benxin.admin.ui.screen.home

import com.benxin.admin.data.model.Content

/** 首页四态：加载 / 成功 / 空 / 错误。 */
sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val items: List<Content>) : HomeUiState
    data object Empty : HomeUiState
    data class Error(val message: String) : HomeUiState
}
