// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 首页 ViewModel（拉取 contents 列表）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-21 23:16:00
// +----------------------------------------------------------------------
package com.benxin.admin.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benxin.admin.data.repository.ContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 首页 ViewModel。N0 不引 Hilt，仓库直接实例化（字段持有，
 * 保证 androidx 默认 viewModel() 工厂可无参构造）。
 */
class HomeViewModel : ViewModel() {

    private val repository = ContentRepository()

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.value = HomeUiState.Loading
        viewModelScope.launch {
            repository.listContents(page = 1, pageSize = 20)
                .onSuccess { paged ->
                    _state.value = if (paged.list.isEmpty()) {
                        HomeUiState.Empty
                    } else {
                        HomeUiState.Success(paged.list)
                    }
                }
                .onFailure { e ->
                    _state.value = HomeUiState.Error(e.message ?: "网络异常，请稍后重试")
                }
        }
    }
}
