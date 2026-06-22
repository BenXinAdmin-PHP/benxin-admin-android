// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — C 端演示门面常量（1:1 取自 uniapp src/config/demo.ts）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 10:30:00
// +----------------------------------------------------------------------
package com.benxin.admin.config

import androidx.annotation.DrawableRes
import com.benxin.admin.R

/**
 * 门面演示用硬编码文案/海报（守底座铁律 §1：默认占位、纯本地可跑）。
 * 文案与 uniapp HERO_SLIDES/ABOUT/DEMO_VIDEOS 逐字一致，保证多端门面观感统一。
 */

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
