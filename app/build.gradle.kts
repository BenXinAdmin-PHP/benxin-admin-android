// +----------------------------------------------------------------------
// | @project   BenXinAdmin
// | @mission   原生 Android 门面 — :app 模块构建脚本（N0 脚手架）
// | @author    仗键天涯(daxing)
// | @email     3442535897@qq.com
// | @date      2026-06-21 23:10:00
// +----------------------------------------------------------------------

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.net.URI

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

val releaseApiBase = providers.gradleProperty("BENXIN_API_BASE_URL")
    .orElse(providers.environmentVariable("BENXIN_API_BASE_URL"))
    .orNull

val checkReleaseApiBase = tasks.register("checkReleaseApiBase") {
    doLast {
        val url = runCatching { releaseApiBase?.let(::URI) }.getOrNull()
        val host = url?.host.orEmpty()
        require(url?.scheme == "https" && host.isNotBlank() &&
            !host.endsWith(".example.com") && host != "localhost") {
            "发布包必须通过 BENXIN_API_BASE_URL 指定真实 HTTPS 后端地址"
        }
    }
}

tasks.matching { it.name == "preReleaseBuild" }.configureEach {
    dependsOn(checkReleaseApiBase)
}

android {
    namespace = "com.benxin.admin"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.benxin.admin"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "0.1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("debug") {
            // 模拟器访问宿主机后端用 10.0.2.2；真机调试改成宿主机局域网 IP（如 http://192.168.1.x:8801）
            buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8801\"")
            isMinifyEnabled = false
        }
        getByName("release") {
            // 发布包必须显式指定真实 HTTPS 地址；preReleaseBuild 会阻断缺失配置。
            buildConfigField("String", "BASE_URL", "\"${releaseApiBase ?: "https://invalid.example.com"}\"")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// 运行 Gradle 的 JDK 为 AS 自带 JBR 21，字节码目标锁 17（与 compileOptions 一致，不触发 toolchain 下载）
kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    // AndroidX 基础
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.activity.compose)

    // Compose（BOM 统一管理版本）
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.navigation.compose)

    // 网络层
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)

    // 图片加载（封面图）
    implementation(libs.coil.compose)

    // 本地持久化（N3 token 用，N0 先加依赖不使用）
    implementation(libs.androidx.datastore.preferences)

    // 调试期 Compose 预览/Inspector
    debugImplementation(libs.androidx.ui.tooling)
}
