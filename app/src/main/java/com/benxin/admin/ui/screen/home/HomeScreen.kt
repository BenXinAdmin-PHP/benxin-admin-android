// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 首页组合落地页（Hero/关于/视频/精选，1:1 复刻 uniapp）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 10:30:00
// | @updated   2026-06-22 10:30:00
// +----------------------------------------------------------------------
package com.benxin.admin.ui.screen.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.benxin.admin.config.About
import com.benxin.admin.config.DEMO_VIDEOS
import com.benxin.admin.config.DemoVideo
import com.benxin.admin.config.HERO_SLIDES
import com.benxin.admin.config.HeroSlide
import com.benxin.admin.ui.component.ArticleCard
import com.benxin.admin.ui.component.StatusBarIcons
import com.benxin.admin.ui.theme.HeroGradientColors
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    onSeeAllArticles: () -> Unit,
    onOpenDetail: (Long) -> Unit,
    viewModel: HomeViewModel = viewModel(),
) {
    StatusBarIcons(darkIcons = false) // 首页 hero 深底：浅色状态栏图标
    val ui by viewModel.state.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        item { HeroPager() }
        item { AboutCard() }
        item { VideoSection() }
        item { FeaturedHeader(onSeeAll = onSeeAllArticles) }

        when {
            ui.firstLoading -> item { FeaturedLoading() }
            ui.error != null -> item { FeaturedError(message = ui.error!!, onRetry = viewModel::load) }
            ui.featured.isEmpty() -> item { FeaturedEmpty() }
            else -> {
                val catName = ui.categories.associate { it.id to it.name }
                items(ui.featured, key = { it.id }) { item ->
                    ArticleCard(
                        item = item,
                        categoryName = catName[item.categoryId],
                        onClick = { onOpenDetail(item.id) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                    )
                }
            }
        }
        item { Spacer(Modifier.height(16.dp)) }
    }
}

/* ---------------- Hero ---------------- */

@Composable
private fun HeroPager() {
    val pagerState = rememberPagerState(pageCount = { HERO_SLIDES.size })

    // 自动轮播（circular 取模）
    LaunchedEffect(pagerState) {
        while (true) {
            delay(4500)
            val next = (pagerState.currentPage + 1) % HERO_SLIDES.size
            pagerState.animateScrollToPage(next)
        }
    }

    Box {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth()) { page ->
            HeroSlideView(HERO_SLIDES[page])
        }
        // 指示点
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            repeat(HERO_SLIDES.size) { i ->
                val active = pagerState.currentPage == i
                Box(
                    modifier = Modifier
                        .size(if (active) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (active) Color.White else Color.White.copy(alpha = 0.4f),
                        ),
                )
            }
        }
    }
}

@Composable
private fun HeroSlideView(slide: HeroSlide) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    colors = HeroGradientColors,
                    start = Offset.Zero,
                    end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                ),
            ),
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 24.dp, end = 24.dp, top = 28.dp, bottom = 44.dp)
                .heightIn(min = 200.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "BenXinAdmin",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.78f),
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = slide.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = slide.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f),
            )
        }
    }
}

/* ---------------- 关于卡 ---------------- */

@Composable
private fun AboutCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = About.TITLE,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(10.dp))
            Text(
                text = About.BODY,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = MaterialTheme.typography.bodyMedium.fontSize * 1.7f,
            )
        }
    }
}

/* ---------------- 视频演示 ---------------- */

@Composable
private fun VideoSection() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "视频演示",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            DEMO_VIDEOS.forEach { v ->
                VideoCard(video = v, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun VideoCard(video: DemoVideo, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    Toast.makeText(context, "演示视频待配置", Toast.LENGTH_SHORT).show()
                },
        ) {
            Image(
                painter = painterResource(video.poster),
                contentDescription = video.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0x6B0A1F44)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayCircle,
                    contentDescription = "播放",
                    tint = Color.White,
                    modifier = Modifier.size(34.dp),
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = video.title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }
}

/* ---------------- 精选文章 ---------------- */

@Composable
private fun FeaturedHeader(onSeeAll: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "精选文章",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "全部 ›",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onSeeAll() },
        )
    }
}

@Composable
private fun FeaturedLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(modifier = Modifier.size(28.dp), strokeWidth = 2.dp)
    }
}

@Composable
private fun FeaturedError(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "点击重试",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onRetry() },
        )
    }
}

@Composable
private fun FeaturedEmpty() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = "演示内容待发布",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = "配置演示数据后，这里将展示精选文章",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}
