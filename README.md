# BenXinAdmin · 原生 Android 多端门面

> BenXinAdmin（本心通用管理后台底座）的**原生 Android 展示门面**——纯 Kotlin + Jetpack Compose，真连底座公开接口 `/api/v1/*`，证明底座 API 可被任意端消费。与 `benxin-admin-uniapp`（小程序 / H5）并存，分别覆盖原生与跨端两条多端故事。

> 本仓为**只读展示门面**：仅消费底座既有公开接口，不含业务后端、不碰代码生成器与护城河基线。

## 技术栈
- Kotlin 2.4 · Jetpack Compose（Material 3）· Navigation Compose
- Retrofit 3 + OkHttp 4.12 + kotlinx.serialization · Kotlin Coroutines
- Coil 图片加载 · DataStore（token 存储，登录阶段启用）
- minSdk 26（Android 8.0）· compileSdk / targetSdk 36 · **不引 Hilt**（手动构造依赖）
- 全部依赖 Apache-2.0 / 可商用（字体用系统字体栈、图标 material-icons-extended、启动图自绘矢量，无任何付费字库或来源不明素材）

## 运行
1. Android Studio（Panda 2025.3.2+）打开本工程，等待 Gradle 同步。
2. 启动 BenXinAdmin 底座后端（本地默认监听 8801）。
3. 选 `app` 运行配置，运行到模拟器或真机。

### 真机调试
`app/build.gradle.kts` 中 debug `BASE_URL` 默认 `http://10.0.2.2:8801`（仅模拟器访问宿主机可用）。
真机请改为宿主机局域网 IP（如 `http://192.168.x.x:8801`）；debug 已对该明文按白名单放行（**仅调试**），release 强制 HTTPS。

### 发布地址
构建发布包前设置 `BENXIN_API_BASE_URL=https://你的后端域名`（环境变量或 Gradle 属性）。
未配置真实 HTTPS 地址时，`preReleaseBuild` 会阻止发布包构建。

## 许可证
Apache-2.0，详见 [LICENSE](./LICENSE)。
