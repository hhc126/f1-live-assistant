plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.f1live"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.f1live"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

// 自动复制 HTML 文件到 assets 目录
val copyHtml = tasks.register<Copy>("copyHtml") {
    description = "Copy jrs_wlty_online.html to assets"
    from(layout.projectDirectory)
    include("jrs_wlty_online.html")
    into(layout.projectDirectory.dir("app/src/main/assets"))
}

// 在 preBuild 之前执行
tasks.named("preBuild") {
    dependsOn(copyHtml)
}

dependencies {
    // 只需要 WebView（framework 自带），不需要 AppCompat
}
