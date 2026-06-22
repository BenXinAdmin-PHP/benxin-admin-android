// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 文章卡片（横向，1:1 复刻 uniapp ArticleCard）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 14:00:00
// +----------------------------------------------------------------------
package com.benxin.admin.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.benxin.admin.config.DemoCovers
import com.benxin.admin.config.categoryGradient
import com.benxin.admin.data.api.ApiClient
import com.benxin.admin.data.model.Content
import com.benxin.admin.ui.theme.BrandBlue
import com.benxin.admin.ui.theme.BrandBlueSoft

/**
 * 横向文章卡（首页精选 + 文章 tab 共用）：
 * 左缩略图(62dp 方·圆角8) + 右〔置顶红标/标题2行/摘要1行/分类胶囊+日期〕，无作者无浏览量。
 * 缩略图两段逻辑对齐 uniapp：演示封面直显 → http 封面 Coil → 否则分类渐变 + 首字兜底。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleCard(
    item: Content,
    categoryName: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Row(modifier = Modifier.padding(10.dp)) {
            Thumbnail(
                item = item,
                categoryName = categoryName,
                modifier = Modifier.size(62.dp),
            )
            Spacer(Modifier.width(10.dp))
            Column(
                modifier = Modifier.height(62.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                TitleRow(item)
                if (!item.summary.isNullOrBlank()) {
                    Text(
                        text = item.summary,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                MetaRow(item = item, categoryName = categoryName)
            }
        }
    }
}

@Composable
private fun TitleRow(item: Content) {
    Row(verticalAlignment = Alignment.Top) {
        if (item.isTopped) {
            Surface(
                color = MaterialTheme.colorScheme.error, // 红色置顶标，对齐 uniapp $bx-danger
                shape = RoundedCornerShape(3.dp),
                modifier = Modifier.padding(top = 2.dp, end = 6.dp),
            ) {
                Text(
                    text = "置顶",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onError,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                )
            }
        }
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun MetaRow(item: Content, categoryName: String?) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (!categoryName.isNullOrBlank()) {
            Surface(
                color = BrandBlueSoft, // 淡蓝底
                shape = RoundedCornerShape(999.dp),
            ) {
                Text(
                    text = categoryName,
                    style = MaterialTheme.typography.labelSmall,
                    color = BrandBlue, // 蓝字
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 1.dp),
                )
            }
        }
        val date = (item.publishAt ?: item.createdAt)?.take(10)
        if (!date.isNullOrBlank()) {
            Text(
                text = date,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary, // 弱灰 #9aa1ad
            )
        }
    }
}

/** 缩略图：演示封面直显 → http 封面 Coil → 分类渐变 + 首字兜底（渐变恒在底层，加载失败自然回退）。 */
@Composable
private fun Thumbnail(
    item: Content,
    categoryName: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Brush.linearGradient(categoryGradient(item.categoryId))),
        contentAlignment = Alignment.Center,
    ) {
        // 兜底首字（分类名优先，否则标题首字）
        val glyph = (categoryName?.takeIf { it.isNotBlank() } ?: item.title)
            .trim().take(1)
        Text(
            text = glyph,
            color = Color.White.copy(alpha = 0.92f),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        // 覆盖真实封面（不透明则盖住渐变；加载中/失败时渐变+首字透出）
        val bundled = DemoCovers.resOf(item.cover)
        when {
            bundled != null -> Image(
                painter = painterResource(bundled),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )

            isHttp(item.cover) -> AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(ApiClient.absoluteUrl(item.cover))
                    .crossfade(true)
                    .build(),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

private fun isHttp(cover: String?): Boolean {
    val v = cover?.trim().orEmpty()
    return v.startsWith("http://") || v.startsWith("https://")
}
