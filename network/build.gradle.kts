plugins {
    alias(libs.plugins.ksp)
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.spotless)
}

android {
    namespace = "com.me.recipe.network"
    compileSdk = 34

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
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

spotless {
    kotlin {
        target("src/**/*.kt")
        ktlint()
    }

    kotlinGradle {
        target("*.kts")
        ktlint()
    }
}

dependencies {
    implementation(project(":core"))
    api(platform(libs.compose.bom))
    implementation(libs.compose.runtime)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.retrofit)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)
    implementation(libs.converter.moshi)

    implementation(libs.timber)
    compileOnly(libs.spotless.gradlePlugin)
}
