// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — 我的（未登录态 1:1 复刻 uniapp Style B）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-22 16:00:00
// | @updated   2026-06-22 16:00:00（N4：替换 N0/N1 占位，1:1 渐变头部+四宫格+列表卡+关于弹层）
// +----------------------------------------------------------------------
package com.benxin.admin.ui.screen.profile

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.benxin.admin.config.AppMeta
import com.benxin.admin.ui.component.StatusBarIcons
import com.benxin.admin.ui.theme.BrandBlue
import com.benxin.admin.ui.theme.BrandBlueSoft

// 我的页头部渐变（对齐 uniapp：#0a1f44 → #21478f → #2b6fff）
private val MineHeaderGradient = listOf(Color(0xFF0A1F44), Color(0xFF21478F), Color(0xFF2B6FFF))

/**
 * 「我的」未登录态门面（1:1 uniapp Style B）：渐变头部 + 四宫格 + 列表卡 + 关于弹层。
 * 登录态/真实外链/持久化留 N3；占位项点击给诚实反馈（Toast），不虚构功能。
 */
@Composable
fun ProfileScreen(onGoArticles: () -> Unit) {
    StatusBarIcons(darkIcons = false) // 头部深色渐变：浅色状态栏图标
    val context = LocalContext.current
    var showAbout by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
    ) {
        Header(onLogin = { context.toast("登录功能开发中（N3）") })

        // 四宫格 + 列表卡整体上移 24dp，宫格叠在渐变下沿
        Column(modifier = Modifier.offset(y = (-24).dp)) {
            GridCard(
                onArticles = onGoArticles,
                onAbout = { showAbout = true },
                onDocs = { context.copyRepoToast() },
                onSettings = { context.toast("设置功能开发中") },
            )
            Spacer(Modifier.height(14.dp))
            MenuCard(
                onDocs = { context.copyRepoToast() },
                onFeedback = { context.toast("意见反馈为演示占位") },
                onAbout = { showAbout = true },
            )
        }
    }

    if (showAbout) {
        AboutDialog(
            onCopy = { context.copyRepoToast(copiedMsg = "仓库地址已复制") },
            onDismiss = { showAbout = false },
        )
    }
}

/* ---------------- 头部 ---------------- */

@Composable
private fun Header(onLogin: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(MineHeaderGradient))
            .statusBarsPadding()
            .clickable { onLogin() }
            .padding(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 36.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE9EBF0)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "默认头像",
                    tint = Color(0xFFAAB0BB),
                    modifier = Modifier.size(28.dp),
                )
            }
            Spacer(Modifier.size(12.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Text(
                    text = "点击登录 / 注册",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                )
                Text(
                    text = "登录后体验完整功能",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.82f),
                )
            }
            Text(
                text = "›",
                fontSize = 22.sp,
                color = Color.White.copy(alpha = 0.7f),
            )
        }
    }
}

/* ---------------- 四宫格 ---------------- */

@Composable
private fun GridCard(
    onArticles: () -> Unit,
    onAbout: () -> Unit,
    onDocs: () -> Unit,
    onSettings: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
    ) {
        Row(modifier = Modifier.padding(vertical = 16.dp)) {
            GridItem("文章", Icons.AutoMirrored.Filled.List, Modifier.weight(1f), onArticles)
            GridItem("关于", Icons.Filled.Info, Modifier.weight(1f), onAbout)
            GridItem("文档", Icons.Filled.Description, Modifier.weight(1f), onDocs)
            GridItem("设置", Icons.Filled.Settings, Modifier.weight(1f), onSettings)
        }
    }
}

@Composable
private fun GridItem(label: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(7.dp),
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BrandBlueSoft),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = label, tint = BrandBlue, modifier = Modifier.size(22.dp))
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/* ---------------- 列表卡 ---------------- */

@Composable
private fun MenuCard(onDocs: () -> Unit, onFeedback: () -> Unit, onAbout: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
    ) {
        Column {
            MenuItem(text = "项目文档", onClick = onDocs)
            MenuDivider()
            MenuItem(text = "意见反馈", demoTag = true, onClick = onFeedback)
            MenuDivider()
            MenuItem(text = "关于 BenXinAdmin", onClick = onAbout)
        }
    }
}

@Composable
private fun MenuItem(text: String, demoTag: Boolean = false, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            if (demoTag) {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(999.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                ) {
                    Text(
                        text = "演示",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 1.dp),
                    )
                }
            }
            Text(text = "›", fontSize = 18.sp, color = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
private fun MenuDivider() {
    HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outline)
}

/* ---------------- 关于弹层 ---------------- */

@Composable
private fun AboutDialog(onCopy: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("关于 ${AppMeta.NAME}") },
        text = {
            Text(
                "通用管理后台开源底座\n" +
                    "版本 ${AppMeta.VERSION} · 开源协议 ${AppMeta.LICENSE}\n" +
                    "仓库 ${AppMeta.REPO}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        confirmButton = {
            TextButton(onClick = { onCopy(); onDismiss() }) { Text("复制仓库") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("关闭") }
        },
    )
}

/* ---------------- 工具 ---------------- */

private fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

private fun Context.copyRepoToast(copiedMsg: String = "文档链接已复制，请在浏览器打开") {
    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cm.setPrimaryClip(ClipData.newPlainText("repo", AppMeta.REPO))
    toast(copiedMsg)
}
