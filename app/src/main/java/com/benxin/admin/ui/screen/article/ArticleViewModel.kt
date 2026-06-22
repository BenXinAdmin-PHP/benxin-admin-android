// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 文章 tab ViewModel（分类/搜索 debounce/分页/刷新）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 10:30:00
// +----------------------------------------------------------------------
package com.benxin.admin.ui.screen.article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.benxin.admin.data.repository.ContentRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 文章 tab ViewModel。列表/分页/刷新逻辑由 N1 首页迁移；新增关键字搜索（debounce 400ms）。
 * pageSize=10 对齐 uniapp 文章 tab。
 */
@OptIn(FlowPreview::class)
class ArticleViewModel : ViewModel() {

    private val repository = ContentRepository()
    private val pageSize = 10

    private val _state = MutableStateFlow(ArticleUiState())
    val state: StateFlow<ArticleUiState> = _state.asStateFlow()

    // 搜索输入流，debounce 后触发重拉（与 category_id 叠加）
    private val keywordInput = MutableStateFlow("")

    init {
        loadCategories()
        reload(firstLoad = true)
        viewModelScope.launch {
            keywordInput
                .debounce(400)
                .drop(1) // 跳过初始空串
                .distinctUntilChanged()
                .collect { reload(firstLoad = true) }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories().onSuccess { cats ->
                _state.update { it.copy(categories = cats) }
            }
        }
    }

    /** 输入框变化：立即回显 + 推入 debounce 流。 */
    fun onKeywordChange(kw: String) {
        _state.update { it.copy(keyword = kw) }
        keywordInput.value = kw
    }

    fun clearKeyword() = onKeywordChange("")

    fun selectCategory(categoryId: Long?) {
        if (_state.value.categoryId == categoryId) return
        _state.update { it.copy(categoryId = categoryId, list = emptyList(), total = 0, page = 0) }
        reload(firstLoad = true)
    }

    fun refresh() = reload(firstLoad = false)

    fun retryFirstLoad() = reload(firstLoad = true)

    fun retryLoadMore() = loadMore()

    private fun reload(firstLoad: Boolean) {
        val s = _state.value
        val catId = s.categoryId
        val kw = s.keyword.trim().ifBlank { null }
        _state.update {
            it.copy(firstLoading = firstLoad, refreshing = !firstLoad, error = null, loadMoreError = null)
        }
        viewModelScope.launch {
            repository.listContents(page = 1, pageSize = pageSize, categoryId = catId, keyword = kw)
                .onSuccess { paged ->
                    _state.update {
                        it.copy(
                            firstLoading = false,
                            refreshing = false,
                            list = paged.list,
                            total = paged.total,
                            page = 1,
                            error = null,
                        )
                    }
                }
                .onFailure { e ->
                    _state.update {
                        val keepOld = it.list.isNotEmpty()
                        it.copy(
                            firstLoading = false,
                            refreshing = false,
                            error = if (keepOld) null else (e.message ?: "加载失败，请稍后重试"),
                        )
                    }
                }
        }
    }

    fun loadMore() {
        val s = _state.value
        if (s.firstLoading || s.refreshing || s.loadingMore) return
        if (!s.hasMore || s.list.isEmpty()) return

        _state.update { it.copy(loadingMore = true, loadMoreError = null) }
        val next = s.page + 1
        val catId = s.categoryId
        val kw = s.keyword.trim().ifBlank { null }
        viewModelScope.launch {
            repository.listContents(page = next, pageSize = pageSize, categoryId = catId, keyword = kw)
                .onSuccess { paged ->
                    _state.update {
                        it.copy(
                            loadingMore = false,
                            list = it.list + paged.list,
                            total = paged.total,
                            page = next,
                        )
                    }
                }
                .onFailure { e ->
                    _state.update { it.copy(loadingMore = false, loadMoreError = e.message ?: "加载失败") }
                }
        }
    }
}
