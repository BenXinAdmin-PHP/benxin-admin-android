// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 底部导航壳（首页 / 文章 / 我的）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 10:30:00
// | @updated   2026-06-22 10:30:00（升三 tab，对齐 uniapp tabbar）
// +----------------------------------------------------------------------
package com.benxin.admin.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.benxin.admin.ui.screen.article.ArticleScreen
import com.benxin.admin.ui.screen.detail.DetailScreen
import com.benxin.admin.ui.screen.home.HomeScreen
import com.benxin.admin.ui.screen.profile.ProfileScreen
import com.benxin.admin.ui.theme.BrandBlue
import com.benxin.admin.ui.theme.BrandBlueSoft
import com.benxin.admin.ui.theme.TextWeak

/** 底部导航目的地（对齐 uniapp tabbar 三项）。 */
sealed class Dest(val route: String, val label: String, val icon: ImageVector) {
    data object Home : Dest("home", "首页", Icons.Filled.Home)
    data object Article : Dest("article", "文章", Icons.AutoMirrored.Filled.Article)
    data object Mine : Dest("mine", "我的", Icons.Filled.Person)
}

private val bottomDestinations = listOf(Dest.Home, Dest.Article, Dest.Mine)

/** App 级导航：main（三 tab 主框架）↔ detail/{id}（全屏详情，隐藏底部栏）。 */
@Composable
fun AppNavHost() {
    val appNav = rememberNavController()
    NavHost(navController = appNav, startDestination = "main") {
        composable("main") {
            AppScaffold(onOpenDetail = { id -> appNav.navigate("detail/$id") })
        }
        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType }),
        ) { entry ->
            DetailScreen(
                id = entry.arguments?.getLong("id") ?: 0L,
                onBack = { appNav.popBackStack() },
            )
        }
    }
}

@Composable
fun AppScaffold(onOpenDetail: (Long) -> Unit) {
    val navController = rememberNavController()

    Scaffold(
        // 顶部 inset 由各页自行管理（首页 hero 需全幅延伸至状态栏下）
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = { BottomBar(navController) },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Dest.Home.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Dest.Home.route) {
                HomeScreen(
                    onSeeAllArticles = { navController.switchTab(Dest.Article.route) },
                    onOpenDetail = onOpenDetail,
                )
            }
            composable(Dest.Article.route) { ArticleScreen(onOpenDetail = onOpenDetail) }
            composable(Dest.Mine.route) {
                ProfileScreen(onGoArticles = { navController.switchTab(Dest.Article.route) })
            }
        }
    }
}

@Composable
private fun BottomBar(navController: NavHostController) {
    NavigationBar(containerColor = Color.White) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        bottomDestinations.forEach { dest ->
            val selected = currentDestination?.hierarchy?.any { it.route == dest.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = { navController.switchTab(dest.route) },
                icon = { Icon(dest.icon, contentDescription = dest.label) },
                label = { Text(dest.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BrandBlue,
                    selectedTextColor = BrandBlue,
                    indicatorColor = BrandBlueSoft,
                    unselectedIconColor = TextWeak,
                    unselectedTextColor = TextWeak,
                ),
            )
        }
    }
}

/** 切换 tab：避免重复入栈、保留各 tab 状态。 */
private fun NavHostController.switchTab(route: String) {
    navigate(route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
