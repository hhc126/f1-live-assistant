# F1 直播助手 - 安卓打包教程

## 📱 项目简介

将 F1 直播助手打包成安卓 App，适配投影仪大屏显示。

**特点：**
- ✅ 不修改原始文件（`jrs_wlty_online.html` 保持不变）
- ✅ 内容固定（HTML 打包在 APK 里，不会被修改）
- ✅ 离线可用（除了点击"观看直播"按钮）
- ✅ 大屏优化（字体、按钮、横屏）
- ✅ 全屏模式（隐藏状态栏和导航栏）
- ✅ 保持屏幕常亮（投影仪重要）

---

## 🚀 打包方式

### 方案 A：用 Android Studio 打包（推荐，最简单）

#### 1. 安装 Android Studio
- 下载：https://developer.android.com/studio
- 安装：按照向导完成（会自动安装 Android SDK）

#### 2. 打开项目
- 启动 Android Studio
- 选择 **Open an existing project**
- 选择 `android-app/app/` 目录
- 等待 Gradle 同步完成（首次会下载 Gradle，需要联网）

#### 3. 打包 APK
- 点击菜单：**Build** → **Generate Signed Bundle / APK**
- 选择 **APK**
- 创建签名密钥（Key Store）：
  - 点击 **Create new...**
  - 填写信息（密码、姓名等）
  - 保存密钥（重要！以后更新 App 需要）
- 选择 **Release** 构建变体
- 点击 **Finish**
- APK 会生成在：`app/build/outputs/apk/release/app-release.apk`

#### 4. 安装到投影仪
- 将 APK 传输到投影仪（U 盘、网络共享等）
- 在投影仪上：设置 → 安全 → 允许未知来源
- 点击 APK 文件，安装
- 打开 App，享受大屏体验！

---

### 方案 B：用 Gradle 命令行打包（高级用户）

#### 1. 配置环境
- 安装 Android SDK
- 设置环境变量：`ANDROID_HOME` 或 `ANDROID_SDK_ROOT`
- 安装 JDK 11+

#### 2. 运行构建脚本
```bash
cd android-app/
chmod +x build.sh
./build.sh
```

#### 3. 手动打包（如果没有 Gradle Wrapper）
```bash
cd app/

# 创建 Gradle Wrapper（需要联网）
gradle wrapper

# 打包
./gradlew assembleRelease
```

#### 4. 获取 APK
```bash
# APK 位置
app/build/outputs/apk/release/app-release.apk

# 复制到 output/
cp app/build/outputs/apk/release/app-release.apk output/F1_Live_Assitant.apk
```

---

## 📂 目录结构

```
android-app/
├── build.sh                          # 构建脚本（复制 + 优化 + 打包）
├── optimize_html.js                  # HTML 优化脚本（大屏适配）
├── assets/                          # 临时文件（构建时复制到这里）
│   ├── jrs_wlty_online.html       # ← 副本（可修改）
│   └── f1_2026_schedule.json      # ← 副本（可选）
├── app/                             # Android Studio 项目
│   ├── build.gradle                # 模块级 Gradle 配置
│   ├── settings.gradle             # 项目设置
│   ├── src/main/
│   │   ├── assets/                # ← 最终打包到 APK 的文件
│   │   │   ├── jrs_wlty_online.html
│   │   │   └── f1_2026_schedule.json
│   │   ├── java/com/example/f1live/
│   │   │   └── MainActivity.kt    # WebView 主 Activity
│   │   ├── res/layout/
│   │   │   └── activity_main.xml  # WebView 布局
│   │   ├── res/values/
│   │   │   └── strings.xml       # 字符串资源
│   │   └── AndroidManifest.xml    # 应用配置（横屏、全屏）
│   └── build/outputs/apk/         # 生成的 APK（构建后）
├── output/                          # 最终输出目录
│   └── F1_Live_Assitant.apk      # ← 打包完成的 APK
└── README.md                       # 本文件
```

---

## 🔨 构建流程

### 完整流程（自动）

```bash
# 1. 复制原始文件到 assets/
# 2. 优化 HTML（大屏适配）
# 3. 复制到安卓项目
# 4. 打包成 APK

./build.sh
```

### 手动流程（分步）

#### 1. 更新原始文件
```bash
# 在 F1/ 目录下，修改 jrs_wlty_online.html
# （通过 CloudStudio 部署更新）
```

#### 2. 复制文件
```bash
cd android-app/
cp ../jrs_wlty_online.html assets/
cp ../f1_2026_schedule.json assets/  # 可选
```

#### 3. 优化 HTML
```bash
node optimize_html.js assets/jrs_wlty_online.html
```

#### 4. 复制到安卓项目
```bash
cp assets/jrs_wlty_online.html app/src/main/assets/
cp assets/f1_2026_schedule.json app/src/main/assets/  # 可选
```

#### 5. 打包
- **用 Android Studio**：参考"方案 A"
- **用 Gradle**：参考"方案 B"

---

## ⚙️ 配置说明

### 1. 横屏模式
在 `AndroidManifest.xml` 里配置：
```xml
<activity
    android:screenOrientation="landscape"  <!-- 强制横屏 -->
>
```

### 2. 全屏模式
在 `MainActivity.kt` 里实现：
```kotlin
// 隐藏状态栏和导航栏
window.decorView.systemUiVisibility = (
    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    or View.SYSTEM_UI_FLAG_FULLSCREEN
)
```

### 3. 保持屏幕常亮
在 `AndroidManifest.xml` 里配置：
```xml
<uses-permission android:name="android.permission.WAKE_LOCK" />

<activity
    android:keepScreenOn="true"  <!-- 保持屏幕常亮 -->
>
```

### 4. 大屏优化
在 `optimize_html.js` 里配置：
- 字体大小：`body { font-size: 20px; }`
- 按钮大小：`.watch-btn { padding: 30px 80px; }`
- 横屏适配：`@media (orientation: landscape) { ... }`
- 超宽屏适配：`@media (min-width: 1920px) { ... }`

---

## 🐛 常见问题

### 1. Gradle 同步失败
**原因：** 没有联网，或者 Android SDK 路径错误

**解决：**
- 检查网络
- 在 Android Studio 里：File → Settings → Android SDK → 确认 SDK 路径
- 或者：设置环境变量 `ANDROID_HOME`

### 2. 打包时提示"签名密钥不存在"
**原因：** 没有创建签名密钥

**解决：**
- 参考"方案 A"第 3 步，创建签名密钥
- 或者，使用调试密钥（不推荐，无法发布到应用商店）：
  ```bash
  ./gradlew assembleDebug  # 生成调试 APK
  ```

### 3. 投影仪无法安装 APK
**原因：** 没有开启"允许未知来源"

**解决：**
- 在投影仪上：设置 → 安全 → 允许未知来源
- 或者：设置 → 应用 → 特殊访问权限 → 安装未知应用

### 4. WebView 无法加载 HTML
**原因：** HTML 文件路径错误

**解决：**
- 确认 HTML 文件在 `app/src/main/assets/` 目录下
- 确认 `MainActivity.kt` 里加载路径正确：
  ```kotlin
  webView.loadUrl("file:///android_asset/jrs_wlty_online.html")
  ```

### 5. 大屏显示不正常
**原因：** 没有运行 `optimize_html.js`

**解决：**
```bash
node optimize_html.js assets/jrs_wlty_online.html
cp assets/jrs_wlty_online.html app/src/main/assets/
```
然后重新打包。

---

## 📝 更新流程

### 场景 1：更新赛程数据
```bash
# 1. 修改 f1_2026_schedule.json（在 F1/ 目录下）
# 2. 运行构建脚本
cd android-app/
./build.sh
# 3. 重新安装 APK 到投影仪
```

### 场景 2：更新直播页面
```bash
# 1. 修改 jrs_wlty_online.html（在 F1/ 目录下）
#    （通过 CloudStudio 部署更新）
# 2. 运行构建脚本
cd android-app/
./build.sh
# 3. 重新安装 APK 到投影仪
```

### 场景 3：优化大屏显示
```bash
# 1. 修改 optimize_html.js（调整样式）
# 2. 运行优化脚本
cd android-app/
node optimize_html.js assets/jrs_wlty_online.html
# 3. 复制到安卓项目
cp assets/jrs_wlty_online.html app/src/main/assets/
# 4. 重新打包
```

---

## 🎯 最佳实践

### 1. 版本管理
- 每次打包，修改 `app/build.gradle` 里的 `versionCode` 和 `versionName`
- 保留旧版本 APK（方便回滚）

### 2. 签名密钥备份
- **重要！** 备份签名密钥（Key Store 文件）
- 如果丢失，无法更新已安装的 App（只能卸载重装）

### 3. 测试流程
- 在手机上测试 APK（确认功能正常）
- 再安装到投影仪（避免投影仪安装失败）

### 4. 原始文件保护
- **永远不要直接修改 `android-app/app/src/main/assets/` 里的文件**
- 总是修改原始文件（`F1/jrs_wlty_online.html`）
- 然后运行 `build.sh` 复制过来

---

## 📞 技术支持

如果遇到问题：
1. 检查 `README.md` 的"常见问题"章节
2. 查看 Android Studio 的 **Build** 输出窗口
3. 搜索错误信息（Google / Stack Overflow）

---

## 📄 许可协议

本项目仅供个人使用，请勿用于商业用途。

F1 是 Formula One World Championship Limited 的商标。

---

**祝您观赛愉快！🏎️💨**
