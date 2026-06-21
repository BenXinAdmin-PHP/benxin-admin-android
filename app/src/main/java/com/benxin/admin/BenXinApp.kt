// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — Application 入口（N0 脚手架）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-21 23:12:00
// +----------------------------------------------------------------------
package com.benxin.admin

import android.app.Application

/**
 * 全局 Application。
 * N0 仅占位（不引 Hilt，依赖手动构造）；后续如需全局初始化在此挂载。
 */
class BenXinApp : Application()
