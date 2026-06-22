// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — Material3 亮色主题（1:1 复刻 uniapp 亮色）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 10:30:00
// | @updated   2026-06-22 10:30:00
// +----------------------------------------------------------------------
package com.benxin.admin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/** 亮色品牌主题（对齐 uniapp：白卡 + 浅灰底 + 品牌蓝）。 */
private val BenXinLightColors = lightColorScheme(
    primary = BrandBlue,
    onPrimary = TextInverse,
    primaryContainer = BrandBlueSoft,
    onPrimaryContainer = BrandBlue,
    secondary = TextWeak,
    onSecondary = TextInverse,
    background = PageBg,
    onBackground = TextPrimary,
    surface = CardWhite,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSub,
    outline = OutlineLight,
    error = ErrorRed,
    onError = TextInverse,
)

// 暗色 token 保留（后续可选·跟随系统深色；N1 默认亮色，暂不启用）
@Suppress("unused")
private val BenXinDarkColors = darkColorScheme(
    primary = BrandBlue,
    onPrimary = TextInverse,
    background = DarkBackground,
    onBackground = TextInverse,
    surface = DarkSurface,
    onSurface = TextInverse,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextWeak,
    error = ErrorRed,
)

@Composable
fun BenXinAdminTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BenXinLightColors,
        typography = AppTypography,
        content = content,
    )
}
