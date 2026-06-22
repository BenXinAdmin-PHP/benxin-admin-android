// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 首页 ViewModel（精选 6 篇）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 10:30:00
// | @updated   2026-06-22 10:30:00
// +----------------------------------------------------------------------
package com.benxin.admin.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benxin.admin.data.repository.ContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 首页 ViewModel：取精选前 6 篇（GET /api/v1/contents?page=1&page_size=6，置顶优先由后端保证）。
 */
class HomeViewModel : ViewModel() {

    private val repository = ContentRepository()

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.update { it.copy(firstLoading = true, error = null) }
        viewModelScope.launch {
            repository.listContents(page = 1, pageSize = 6)
                .onSuccess { paged ->
                    _state.update {
                        it.copy(firstLoading = false, featured = paged.list, error = null)
                    }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(firstLoading = false, error = e.message ?: "加载失败，请稍后重试")
                    }
                }
        }
    }
}
