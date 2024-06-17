plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.ksp)
    alias(libs.plugins.spotless)
}

android {
    namespace = "com.me.recipe"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mycomposeapplication"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlinCompilerExtensionVersion.get()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
    kotlinOptions {
        freeCompilerArgs += listOf("-Xcontext-receivers")
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
    implementation(project(":data"))
    implementation(project(":domain"))

    api(platform(libs.compose.bom))
    androidTestImplementation(platform(libs.compose.bom))
    debugImplementation(platform(libs.compose.bom))

    implementation(libs.compose.ui)
    implementation(libs.compose.animation)
    implementation(libs.compose.ui.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.viewmodel.lifecycle)
    implementation(libs.compose.navigation)
    implementation(libs.compose.activity)
    implementation(libs.compose.constraintlayout)
    implementation(libs.compose.coil)
    lintChecks(libs.compose.lint.checks)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.work)
    ksp(libs.androidx.hilt.compiler)

    implementation(libs.datastore.preferences)
    implementation(libs.timber)
    implementation(libs.work)
    implementation(libs.kotlinx.collections.immutable)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    testImplementation(libs.hilt.testing)
    testImplementation(libs.junit)
    testImplementation(libs.androidx.compose.ui.test.junit4)
    testImplementation(libs.robolectric)
    testImplementation(libs.mock.webserver)

    androidTestImplementation(libs.androidx.test.ext)
    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(libs.androidx.navigation.testing)
    compileOnly(libs.spotless.gradlePlugin)
}
