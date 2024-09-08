import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.ksp)
}
android {
    namespace = "jp.aqua.shizen"
    compileSdk = 34

    defaultConfig {
        applicationId = "jp.aqua.shizen"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        buildConfigField(
            "String",
            "YANDEX_TRANSLATE_API_KEY",
            properties.getProperty("YANDEX_TRANSLATE_API_KEY")
        )
        buildConfigField(
            "String",
            "YANDEX_OCR_API_KEY",
            properties.getProperty("YANDEX_OCR_API_KEY")
        )
        buildConfigField(
            "String",
            "GOOGLE_IMAGE_API_KEY",
            properties.getProperty("GOOGLE_IMAGE_API_KEY")
        )
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Import the Compose BOM
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.fragment.ktx)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Icons
    implementation(libs.eva.icons)

    // Coil
    implementation(libs.coil)
    implementation(libs.coil.compose)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Libs: EpubLib
    implementation(fileTree("dir" to "libs", "include" to "*.jar"))

    // SL4J
    implementation(libs.slf4j.api)


    // Reordable
    implementation(libs.reorderable)

    // Readium
    implementation(libs.readium.shared)
    implementation(libs.readium.streamer)
    implementation(libs.readium.navigator)
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // Joda
    implementation(libs.joda.time)

    // Ksoup
    implementation(libs.ksoup.html)
    implementation(libs.ksoup.entities)

    // Worker
    implementation(libs.androidx.work.runtime.ktx)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.moshi)

    // Moshi
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.kotlin.codegen)

    // WebView
    implementation(libs.accompanist.webview)

    // ExoPlayer
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.exoplayer.dash)
    implementation(libs.androidx.media3.ui)

    // Base64 Apache
    implementation(libs.commons.codec)

    // Permissions
    implementation(libs.accompanist.permissions)

    // MLKit
    // Text recognition
    // To recognize Latin script
    implementation(libs.mlkit.text.recognition)

    // To recognize Chinese script
    implementation(libs.text.recognition.chinese)

    // To recognize Devanagari script
    implementation (libs.text.recognition.devanagari)

    // To recognize Japanese script
    implementation (libs.text.recognition.japanese)

    // To recognize Korean script
    implementation (libs.text.recognition.korean)

    // Test
    testImplementation(libs.junit)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    debugImplementation(libs.androidx.ui.test.manifest)
}