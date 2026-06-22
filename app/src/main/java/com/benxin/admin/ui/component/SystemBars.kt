// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 状态栏图标明暗按页驱动
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 10:30:00
// +----------------------------------------------------------------------
package com.benxin.admin.ui.component

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * 按当前页内容设置状态栏图标明暗：
 * - darkIcons=true：深色图标（用于浅底页，如文章/我的）
 * - darkIcons=false：浅色图标（用于 hero 深底页，如首页）
 * 边到边由 MainActivity.enableEdgeToEdge() 开启。
 */
@Composable
fun StatusBarIcons(darkIcons: Boolean) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as Activity).window
        SideEffect {
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkIcons
        }
    }
}
