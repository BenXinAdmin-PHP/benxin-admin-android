// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 首页 ViewModel（分类/分页/刷新）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 10:00:00
// | @updated   2026-06-22 10:00:00
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
 * 首页 ViewModel。N0 不引 Hilt，仓库字段持有（保证默认 viewModel() 工厂可无参构造）。
 * 重载路径统一走 [reload]：首屏(firstLoad=true)走骨架、下拉刷新(firstLoad=false)走刷新指示器。
 */
class HomeViewModel : ViewModel() {

    private val repository = ContentRepository()
    private val pageSize = 15

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        loadCategories()
        reload(firstLoad = true)
    }

    /** 加载分类；失败静默降级（chip 行仅留「全部」，不阻断列表）。 */
    private fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories().onSuccess { cats ->
                _state.update { it.copy(categories = cats) }
            }
        }
    }

    /** 切换分类 → 重置分页、清空列表、按新 category_id 重拉（走首屏骨架路径）。 */
    fun selectCategory(categoryId: Long?) {
        if (_state.value.categoryId == categoryId) return
        _state.update { it.copy(categoryId = categoryId, list = emptyList(), total = 0, page = 0) }
        reload(firstLoad = true)
    }

    /** 下拉刷新：重置到第 1 页、重拉当前分类（区别于首屏骨架）。 */
    fun refresh() = reload(firstLoad = false)

    fun retryFirstLoad() = reload(firstLoad = true)

    fun retryLoadMore() = loadMore()

    private fun reload(firstLoad: Boolean) {
        val catId = _state.value.categoryId
        _state.update {
            it.copy(
                firstLoading = firstLoad,
                refreshing = !firstLoad,
                error = null,
                loadMoreError = null,
            )
        }
        viewModelScope.launch {
            repository.listContents(page = 1, pageSize = pageSize, categoryId = catId)
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
                        // 下拉刷新失败：保留旧 list，不整屏报错；首屏失败：list 本就空 → 整屏错误页
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

    /** 上拉分页加载：page+1 追加。并发护栏：刷新/首屏/已在加载/无更多 一律跳过。 */
    fun loadMore() {
        val s = _state.value
        if (s.firstLoading || s.refreshing || s.loadingMore) return
        if (!s.hasMore || s.list.isEmpty()) return

        _state.update { it.copy(loadingMore = true, loadMoreError = null) }
        val next = s.page + 1
        val catId = s.categoryId
        viewModelScope.launch {
            repository.listContents(page = next, pageSize = pageSize, categoryId = catId)
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
                    _state.update {
                        it.copy(loadingMore = false, loadMoreError = e.message ?: "加载失败")
                    }
                }
        }
    }
}
