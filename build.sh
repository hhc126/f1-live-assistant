#!/bin/bash

# F1 直播助手 - 安卓打包脚本
# 功能：复制原始文件 → 优化 HTML → 打包成 APK

set -e  # 遇到错误立即退出

echo "=== F1 直播助手 - 安卓打包工具 ==="
echo ""

# 检查原始文件是否存在
if [ ! -f "../jrs_wlty_online.html" ]; then
    echo "❌ 错误：找不到原始文件 ../jrs_wlty_online.html"
    echo "请确保您在 android-app/ 目录下运行此脚本"
    exit 1
fi

# 第 1 步：复制原始文件到 assets/
echo "[1/4] 复制原始文件到 assets/..."
cp ../jrs_wlty_online.html assets/jrs_wlty_online.html
echo "  ✅ 已复制：jrs_wlty_online.html"

if [ -f "../f1_2026_schedule.json" ]; then
    cp ../f1_2026_schedule.json assets/f1_2026_schedule.json
    echo "  ✅ 已复制：f1_2026_schedule.json"
fi

# 第 2 步：优化 HTML（大屏适配）
echo ""
echo "[2/4] 优化 HTML（大屏适配）..."

if command -v node &> /dev/null; then
    if [ -f "optimize_html.js" ]; then
        node optimize_html.js assets/jrs_wlty_online.html
        echo "  ✅ HTML 已优化"
    else
        echo "  ⚠️  找不到 optimize_html.js，跳过优化"
    fi
else
    echo "  ⚠️  找不到 Node.js，跳过优化"
    echo "  提示：安装 Node.js 后可以自动优化 HTML"
fi

# 第 3 步：复制到安卓项目
echo ""
echo "[3/4] 复制到安卓项目..."
cp assets/jrs_wlty_online.html app/src/main/assets/
echo "  ✅ 已复制到：app/src/main/assets/"

if [ -f "assets/f1_2026_schedule.json" ]; then
    cp assets/f1_2026_schedule.json app/src/main/assets/
    echo "  ✅ 已复制 JSON 到：app/src/main/assets/"
fi

# 第 4 步：打包 APK
echo ""
echo "[4/4] 打包 APK..."

if [ -f "app/gradlew" ]; then
    cd app
    ./gradlew assembleRelease
    cd ..
    
    # 移动到 output/
    if [ -f "app/build/outputs/apk/release/app-release.apk" ]; then
        cp app/build/outputs/apk/release/app-release.apk output/F1_Live_Assistant.apk
        echo "  ✅ APK 已生成：output/F1_Live_Assistant.apk"
        echo ""
        echo "=== 打包完成！ ==="
        echo "APK 位置：android-app/output/F1_Live_Assistant.apk"
        echo ""
        echo "下一步："
        echo "1. 将 APK 传输到投影仪"
        echo "2. 在投影仪上安装（设置 → 安全 → 允许未知来源）"
        echo "3. 打开 App，享受大屏体验！"
    else
        echo "  ❌ 打包失败，请检查 Gradle 配置"
        exit 1
    fi
else
    echo "  ⚠️  找不到 Gradle Wrapper"
    echo ""
    echo "=== 文件已准备完毕 ==="
    echo "下一步："
    echo "1. 用 Android Studio 打开 android-app/app/ 目录"
    echo "2. 点击 Build → Generate Signed Bundle / APK"
    echo "3. 按照向导生成 APK"
    echo ""
    echo "或者，如果您已配置 Gradle，运行："
    echo "  cd app && ./gradlew assembleRelease"
fi
