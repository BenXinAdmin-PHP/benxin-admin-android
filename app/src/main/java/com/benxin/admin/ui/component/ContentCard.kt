// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 内容卡片（首页精选 + 文章列表共用）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 10:30:00
// +----------------------------------------------------------------------
package com.benxin.admin.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.benxin.admin.data.model.Content

/** 内容卡：封面(16:9 兜底) + 置顶徽标 + 标题(2行) + 摘要(2行·空隐藏) + 作者·日期·浏览量。 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentCard(
    item: Content,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column {
            CoverImage(
                cover = item.cover,
                contentDescription = item.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
            )

            Column(modifier = Modifier.padding(14.dp)) {
                if (item.isTopped) {
                    TopBadge()
                    Spacer(Modifier.height(8.dp))
                }
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                if (!item.summary.isNullOrBlank()) {
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = item.summary,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Spacer(Modifier.height(10.dp))
                MetaRow(item)
            }
        }
    }
}

@Composable
private fun TopBadge() {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(6.dp),
    ) {
        Text(
            text = "置顶",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
        )
    }
}

@Composable
private fun MetaRow(item: Content) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val author = item.author?.takeIf { it.isNotBlank() } ?: "BenXinAdmin"
        Text(
            text = author,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        val date = (item.publishAt ?: item.createdAt)?.take(10)
        if (date != null) {
            Text(
                text = " · $date",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Spacer(Modifier.weight(1f))
        Icon(
            imageVector = Icons.Filled.Visibility,
            contentDescription = "浏览量",
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = " ${item.viewCount}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
