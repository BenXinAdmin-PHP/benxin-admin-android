// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 文章详情 ViewModel（GET /api/v1/contents/:id）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 15:00:00
// +----------------------------------------------------------------------
package com.benxin.admin.ui.screen.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benxin.admin.data.repository.ContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * 文章详情 ViewModel：按 id 拉详情（访问使 view_count+1），并解析分类名。
 */
class DetailViewModel : ViewModel() {

    private val repository = ContentRepository()

    private val _state = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val state: StateFlow<DetailUiState> = _state.asStateFlow()

    fun load(id: Long) {
        _state.value = DetailUiState.Loading
        viewModelScope.launch {
            // 分类名尽力而为（失败不阻断详情）
            val categories = repository.getCategories().getOrDefault(emptyList())
            repository.getContent(id)
                .onSuccess { content ->
                    val name = categories.find { it.id == content.categoryId }?.name
                    _state.value = DetailUiState.Success(content, name)
                }
                .onFailure { e ->
                    _state.value = DetailUiState.Error(e.message ?: "内容不存在或已下架")
                }
        }
    }
}
