// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 主题色板（亮色，对齐 uniapp uni.scss）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 10:30:00
// | @updated   2026-06-22 10:30:00（ADR-28 修订①：暗色→亮色，1:1 复刻 uniapp）
// +----------------------------------------------------------------------
package com.benxin.admin.ui.theme

import androidx.compose.ui.graphics.Color

// 品牌（与 uniapp $bx-primary 同源）
val BrandBlue = Color(0xFF2B6FFF)
val BrandBlueDeep = Color(0xFF1C3F86) // 渐变深端（我的页头部）
val BrandBlueSoft = Color(0x1A2B6FFF) // rgba(43,111,255,.1)：pill/选中底

// Hero 渐变（深海蓝→品牌蓝→亮蓝，对齐 $bx-gradient-hero 135°）
val HeroGradientColors = listOf(Color(0xFF0A1F44), Color(0xFF1C3F86), Color(0xFF2B6FFF))

// 文字
val TextPrimary = Color(0xFF1F2430) // 主
val TextSub = Color(0xFF6B7280)     // 次
val TextWeak = Color(0xFF9AA1AD)    // 弱（meta/占位/未选）
val TextInverse = Color(0xFFFFFFFF) // 反白（渐变上文字）

// 面
val PageBg = Color(0xFFF4F6FA)            // 页面底
val CardWhite = Color(0xFFFFFFFF)         // 卡片白
val OutlineLight = Color(0xFFE7E9EF)      // 细边
val SurfaceVariantLight = Color(0xFFECEFF4) // 骨架/占位浅灰

val ErrorRed = Color(0xFFE5484D)

// 暗色 token 保留（后续可选·跟随系统深色；N1 当前默认亮色）
val DarkBackground = Color(0xFF0E1116)
val DarkSurface = Color(0xFF161B22)
val DarkSurfaceVariant = Color(0xFF222B36)
