// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 单 Activity 宿主（N0 脚手架）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-21 23:12:00
// +----------------------------------------------------------------------
package com.benxin.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.benxin.admin.ui.navigation.AppScaffold
import com.benxin.admin.ui.theme.BenXinAdminTheme

/**
 * 单 Activity + Compose 架构宿主。
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BenXinAdminTheme {
                AppScaffold()
            }
        }
    }
}
