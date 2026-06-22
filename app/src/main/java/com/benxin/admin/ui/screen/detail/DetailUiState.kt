// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 文章详情 UI 状态
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 15:00:00
// +----------------------------------------------------------------------
package com.benxin.admin.ui.screen.detail

import com.benxin.admin.data.model.Content

/** 文章详情三态。 */
sealed interface DetailUiState {
    data object Loading : DetailUiState
    data class Success(val content: Content, val categoryName: String?) : DetailUiState
    data class Error(val message: String) : DetailUiState
}
