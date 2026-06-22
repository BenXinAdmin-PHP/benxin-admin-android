// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 封面图组件（Coil + 自绘品牌占位兜底）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 10:00:00
// +----------------------------------------------------------------------
package com.benxin.admin.ui.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.benxin.admin.R
import com.benxin.admin.data.api.ApiClient

/**
 * 封面图：
 * - cover 为相对路径时由 [ApiClient.absoluteUrl] 规范化拼成绝对 URL（对称处理斜杠，避免 `//` 或缺斜杠）；
 * - cover 为空/空串 → 不发请求，直接显示自绘品牌占位；
 * - 加载中 / 404 / 解码失败 → 统一落到自绘 16:9 品牌占位（看起来是设计而非坏图，守 §1）。
 */
@Composable
fun CoverImage(
    cover: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    val placeholder = painterResource(R.drawable.ic_cover_placeholder)
    val url = ApiClient.absoluteUrl(cover)

    if (url == null) {
        Image(
            painter = placeholder,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
            modifier = modifier,
        )
    } else {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            placeholder = placeholder,
            error = placeholder,
            fallback = placeholder,
            contentScale = ContentScale.Crop,
            modifier = modifier,
        )
    }
}
