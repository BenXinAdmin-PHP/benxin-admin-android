// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — Material3 暗色主题（固定暗色，不启用动态取色）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-21 23:14:00
// +----------------------------------------------------------------------
package com.benxin.admin.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

/**
 * 固定暗色品牌主题（不跟随系统/动态取色，保证三端品牌一致性）。
 */
private val BenXinDarkColors = darkColorScheme(
    primary = BrandBlue,
    onPrimary = TextPrimary,
    primaryContainer = BrandBlueDeep,
    onPrimaryContainer = TextPrimary,
    secondary = BrandBlueSoft,
    onSecondary = TextPrimary,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = DarkOutline,
    error = ErrorRed,
    onError = TextPrimary,
)

@Composable
fun BenXinAdminTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BenXinDarkColors,
        typography = AppTypography,
        content = content,
    )
}
