// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 文章详情页（头图 + 元信息 + WebView 富文本，1:1 uniapp）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 15:00:00
// +----------------------------------------------------------------------
package com.benxin.admin.ui.screen.detail

import android.graphics.Color as AndroidColor
import android.webkit.WebView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.benxin.admin.BuildConfig
import com.benxin.admin.config.DemoCovers
import com.benxin.admin.config.categoryGradient
import com.benxin.admin.data.api.ApiClient
import com.benxin.admin.data.model.Content
import com.benxin.admin.ui.component.ErrorView
import com.benxin.admin.ui.component.StatusBarIcons
import com.benxin.admin.ui.theme.BrandBlue
import com.benxin.admin.ui.theme.BrandBlueSoft

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    id: Long,
    onBack: () -> Unit,
    viewModel: DetailViewModel = viewModel(),
) {
    StatusBarIcons(darkIcons = true) // 浅底页：深色状态栏图标
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(id) { viewModel.load(id) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("详情") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            when (val s = state) {
                is DetailUiState.Loading -> DetailSkeleton()
                is DetailUiState.Error -> ErrorView(message = s.message, onRetry = { viewModel.load(id) })
                is DetailUiState.Success -> DetailContent(content = s.content, categoryName = s.categoryName)
            }
        }
    }
}

@Composable
private fun DetailContent(content: Content, categoryName: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        HeadImage(content)

        // 正文容器：圆角顶 + 上移 14dp 叠在头图上（对齐 uniapp -28rpx）
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = (-14).dp)
                .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(start = 16.dp, end = 16.dp, top = 18.dp, bottom = 24.dp),
        ) {
            Text(
                text = content.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(12.dp))
            MetaRow(content, categoryName)
            Spacer(Modifier.height(16.dp))

            val html = content.content
            if (!html.isNullOrBlank()) {
                ArticleWebView(html = html, modifier = Modifier.fillMaxWidth())
            } else {
                Text(
                    text = "暂无正文",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun HeadImage(content: Content) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(100f / 42f) // 对齐 uniapp head padding-bottom:42%
            .background(Brush.linearGradient(categoryGradient(content.categoryId))),
    ) {
        val bundled = DemoCovers.resOf(content.cover)
        when {
            bundled != null -> Image(
                painter = painterResource(bundled),
                contentDescription = content.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )

            isHttp(content.cover) -> AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(ApiClient.absoluteUrl(content.cover))
                    .crossfade(true)
                    .build(),
                contentDescription = content.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
            // 否则仅显分类渐变（不破图）
        }
    }
}

@Composable
private fun MetaRow(content: Content, categoryName: String?) {
    androidx.compose.foundation.layout.FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        if (!categoryName.isNullOrBlank()) {
            Surface(color = BrandBlueSoft, shape = RoundedCornerShape(999.dp)) {
                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.labelMedium,
                    color = BrandBlue,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                )
            }
        }
        content.author?.takeIf { it.isNotBlank() }?.let {
            MetaText(it)
        }
        (content.publishAt ?: content.createdAt)?.take(10)?.let {
            MetaText(it)
        }
        MetaText("${content.viewCount} 阅读")
    }
}

@Composable
private fun MetaText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.secondary,
    )
}

/**
 * 正文 WebView（方案 A·零新依赖·安全收口）：
 * JS 关闭、禁文件/内容访问；baseURL=BuildConfig.BASE_URL 让正文相对图片路径解析到本站；
 * 正文已由 server HtmlPurifier 净化 → 双保险防 XSS。CSS 包壳融入亮色原生页。
 */
@Composable
private fun ArticleWebView(html: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            WebView(ctx).apply {
                settings.javaScriptEnabled = false
                settings.allowFileAccess = false
                settings.allowContentAccess = false
                // file-URL 跨源访问在 API 26+ 默认即 false，无需显式设置（避免弃用 API）
                isVerticalScrollBarEnabled = false
                isHorizontalScrollBarEnabled = false
                setBackgroundColor(AndroidColor.TRANSPARENT)
            }
        },
        update = { web ->
            web.loadDataWithBaseURL(
                BuildConfig.BASE_URL,
                wrapHtml(html),
                "text/html",
                "utf-8",
                null,
            )
        },
    )
}

private fun isHttp(cover: String?): Boolean {
    val v = cover?.trim().orEmpty()
    return v.startsWith("http://") || v.startsWith("https://")
}

/** CSS 包壳：亮色排版、图片/表格不溢出、链接品牌蓝。 */
private fun wrapHtml(body: String): String = """
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<style>
  html,body{margin:0;padding:0;background:transparent;color:#6b7280;
    font-size:16px;line-height:1.85;
    font-family:-apple-system,'PingFang SC','Noto Sans CJK SC','Microsoft YaHei',sans-serif;
    word-wrap:break-word;overflow-wrap:break-word;}
  p{margin:0 0 14px;}
  img{max-width:100%;height:auto;border-radius:8px;}
  a{color:#2b6fff;text-decoration:none;}
  h1,h2,h3,h4{color:#1f2430;line-height:1.4;margin:18px 0 10px;}
  ul,ol{padding-left:22px;}
  blockquote{margin:0 0 14px;padding:8px 14px;border-left:3px solid #2b6fff;
    background:#f4f6fa;color:#6b7280;border-radius:0 8px 8px 0;}
  code{background:#f4f6fa;border-radius:6px;padding:2px 6px;font-size:0.92em;}
  pre{background:#f4f6fa;border-radius:8px;padding:12px;overflow-x:auto;}
  pre code{background:transparent;padding:0;}
  table{max-width:100%;border-collapse:collapse;display:block;overflow-x:auto;}
  th,td{border:1px solid #e7e9ef;padding:6px 10px;}
</style>
</head>
<body>$body</body>
</html>
""".trimIndent()

@Composable
private fun DetailSkeleton() {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(100f / 42f)
                .background(MaterialTheme.colorScheme.surfaceVariant),
        )
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            SkeletonBar(0.7f, 24.dp)
            SkeletonBar(0.45f, 16.dp)
            SkeletonBar(1f, 14.dp)
            SkeletonBar(1f, 14.dp)
            SkeletonBar(0.55f, 14.dp)
        }
    }
}

@Composable
private fun SkeletonBar(widthFraction: Float, height: androidx.compose.ui.unit.Dp) {
    Spacer(
        modifier = Modifier
            .fillMaxWidth(widthFraction)
            .height(height)
            .clip(RoundedCornerShape(6.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant),
    )
}
