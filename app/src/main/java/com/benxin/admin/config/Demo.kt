// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — C 端演示门面常量（1:1 取自 uniapp src/config/demo.ts）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 10:30:00
// +----------------------------------------------------------------------
package com.benxin.admin.config

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.benxin.admin.R
import kotlin.math.abs

/**
 * 门面演示用硬编码文案/海报（守底座铁律 §1：默认占位、纯本地可跑）。
 * 文案与 uniapp HERO_SLIDES/ABOUT/DEMO_VIDEOS 逐字一致，保证多端门面观感统一。
 */

/** 应用元信息（「我的」关于弹层用，逐字对齐 uniapp APP_META）。 */
object AppMeta {
    const val NAME = "BenXinAdmin"
    const val VERSION = "v0.2.1"
    const val LICENSE = "Apache-2.0"
    const val REPO = "https://gitee.com/binxin-admin/binxin-admin-server"
}

/** 首页 hero 轮播文案（纯渐变 + 标题 + 副标题，无图）。 */
data class HeroSlide(val title: String, val subtitle: String)

val HERO_SLIDES: List<HeroSlide> = listOf(
    HeroSlide("本心通用管理后台底座", "PHP8 + Vue3 + uni-app，一套代码三端通吃"),
    HeroSlide("代码生成器护城河", "bx:make 一条命令复刻 CRUD / 树形 / 授权链路三类范式"),
    HeroSlide("开箱即用·安全合规", "八项安全基线贯穿全程，纯本地零配置即可跑通"),
)

/** 首页「关于」介绍卡文案。 */
object About {
    const val TITLE = "关于 BenXinAdmin"
    const val BODY =
        "BenXinAdmin 是一套开箱即用的通用管理后台开源底座（Apache-2.0）。后端 ThinkPHP8 多应用，" +
        "后台 Vue3 + Element Plus，C 端 uni-app 一码出微信小程序与 H5。代码生成器复刻三类范式、" +
        "数据权限横切贯穿、安全基线作为每个模块的验收硬指标——让你把精力留给真正的业务。"
}

/**
 * 演示视频：默认占位（无真实 src）。海报取自 uniapp 同源资产（项目自有原创，§10 合规）：
 * demo_video_overview.png ← uniapp static/demo/video-overview.png
 * demo_video_codegen.png  ← uniapp static/demo/video-codegen-demo.png
 * 点击仅提示「演示视频待配置」，与 uniapp 行为一致。
 */
data class DemoVideo(val title: String, @DrawableRes val poster: Int)

val DEMO_VIDEOS: List<DemoVideo> = listOf(
    DemoVideo("功能速览", R.drawable.demo_video_overview),
    DemoVideo("代码生成器演示", R.drawable.demo_video_codegen),
)

/**
 * 演示封面解析（对齐 uniapp resolveMedia 第一段）：
 * 后端 cover 形如 `/static/demo/covers/cover-xxx.png`，uniapp 直接读自己打包的同名资源；
 * 本端把这 6 张封面打进 res/drawable，按 basename 1:1 映射（cover-xxx.png ← demo_cover_xxx.png，
 * 来源 uniapp src/static/demo/covers/，项目自有原创 §10）。
 */
object DemoCovers {
    private val byBasename: Map<String, Int> = mapOf(
        "cover-quickstart.png" to R.drawable.demo_cover_quickstart,
        "cover-multiend.png" to R.drawable.demo_cover_multiend,
        "cover-codegen.png" to R.drawable.demo_cover_codegen,
        "cover-storage.png" to R.drawable.demo_cover_storage,
        "cover-security.png" to R.drawable.demo_cover_security,
        "cover-login.png" to R.drawable.demo_cover_login,
    )

    /** cover 路径 → 打包封面资源 id；非演示封面（空/未知）返回 null。 */
    @DrawableRes
    fun resOf(cover: String?): Int? {
        val v = cover?.trim().orEmpty()
        if (v.isEmpty()) return null
        return byBasename[v.substringAfterLast('/')]
    }
}

/**
 * 无封面回退（对齐 uniapp resolveMedia 第二段 categoryGradient）：
 * 6 组品牌系渐变，按 category_id % 6 稳定取色（同分类恒定同色）。
 */
val CATEGORY_GRADIENTS: List<List<Color>> = listOf(
    listOf(Color(0xFF1C3F86), Color(0xFF2B6FFF)), // 品牌蓝
    listOf(Color(0xFF0A1F44), Color(0xFF1C3F86)), // 深海蓝
    listOf(Color(0xFF2B6FFF), Color(0xFF5AA0FF)), // 亮蓝
    listOf(Color(0xFF3A3F8C), Color(0xFF6F74D6)), // 蓝紫
    listOf(Color(0xFF0E7490), Color(0xFF22B8CF)), // 青蓝
    listOf(Color(0xFF334155), Color(0xFF64748B)), // 商务灰蓝
)

fun categoryGradient(seed: Long): List<Color> =
    CATEGORY_GRADIENTS[(abs(seed) % CATEGORY_GRADIENTS.size).toInt()]
