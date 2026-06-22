// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 文章 tab（搜索 + 分类筛选 + 下拉刷新 + 上拉分页）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 10:30:00
// +----------------------------------------------------------------------
package com.benxin.admin.ui.screen.article

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.benxin.admin.data.model.Category
import com.benxin.admin.ui.component.ArticleCard
import com.benxin.admin.ui.component.EmptyView
import com.benxin.admin.ui.component.ErrorView
import com.benxin.admin.ui.component.LoadingSkeleton
import com.benxin.admin.ui.component.StatusBarIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleScreen(
    onOpenDetail: (Long) -> Unit,
    viewModel: ArticleViewModel = viewModel(),
) {
    StatusBarIcons(darkIcons = true) // 浅底页：深色状态栏图标
    val ui by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
    ) {
        // 标题 + 搜索 + 分类 chips
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "文章",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp, bottom = 8.dp),
            )
            OutlinedTextField(
                value = ui.keyword,
                onValueChange = viewModel::onKeywordChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("搜索文章标题") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                trailingIcon = {
                    if (ui.keyword.isNotEmpty()) {
                        IconButton(onClick = viewModel::clearKeyword) {
                            Icon(Icons.Filled.Close, contentDescription = "清除")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
            )
        }
        CategoryChips(
            categories = ui.categories,
            selectedId = ui.categoryId,
            onSelect = viewModel::selectCategory,
        )

        PullToRefreshBox(
            isRefreshing = ui.refreshing,
            onRefresh = viewModel::refresh,
            modifier = Modifier.fillMaxSize(),
        ) {
            when {
                ui.firstLoading -> LoadingSkeleton()
                ui.error != null && ui.list.isEmpty() ->
                    ErrorView(message = ui.error!!, onRetry = viewModel::retryFirstLoad)
                ui.list.isEmpty() ->
                    EmptyView(message = if (ui.keyword.isNotBlank()) "没有匹配的文章" else "暂无内容")
                else -> ContentList(ui = ui, viewModel = viewModel, onOpenDetail = onOpenDetail)
            }
        }
    }
}

@Composable
private fun CategoryChips(
    categories: List<Category>,
    selectedId: Long?,
    onSelect: (Long?) -> Unit,
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            FilterChip(
                selected = selectedId == null,
                onClick = { onSelect(null) },
                label = { Text("全部") },
            )
        }
        items(categories, key = { it.id }) { cat ->
            FilterChip(
                selected = selectedId == cat.id,
                onClick = { onSelect(cat.id) },
                label = { Text(cat.name) },
            )
        }
    }
}

@Composable
private fun ContentList(
    ui: ArticleUiState,
    viewModel: ArticleViewModel,
    onOpenDetail: (Long) -> Unit,
) {
    val listState = rememberLazyListState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val info = listState.layoutInfo
            val last = info.visibleItemsInfo.lastOrNull()?.index ?: -1
            info.totalItemsCount > 0 && last >= info.totalItemsCount - 3
        }
    }
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) viewModel.loadMore()
    }

    val catName = ui.categories.associate { it.id to it.name }
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(ui.list, key = { it.id }) { item ->
            ArticleCard(
                item = item,
                categoryName = catName[item.categoryId],
                onClick = { onOpenDetail(item.id) },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        item {
            ListFooter(ui = ui, onRetry = viewModel::retryLoadMore)
        }
    }
}

@Composable
private fun ListFooter(ui: ArticleUiState, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        when {
            ui.loadingMore -> CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp,
            )

            ui.loadMoreError != null -> Text(
                text = "加载失败 · 点击重试",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.clickable { onRetry() },
            )

            !ui.hasMore && ui.list.isNotEmpty() -> Text(
                text = "没有更多了",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
