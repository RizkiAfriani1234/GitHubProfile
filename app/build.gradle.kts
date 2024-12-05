import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}
val apikeyPropertiesFile = rootProject.file("apikey.properties")
val apikeyProperties = Properties()

if (apikeyPropertiesFile.exists()) {
    apikeyProperties.load(FileInputStream(apikeyPropertiesFile))
} else {
    throw GradleException("File apikey.properties tidak ditemukan di root project.")
}

android {
    namespace = "id.ac.polbeng.rizkiafriani.githubprofile"
    compileSdk = 35

    defaultConfig {
        applicationId = "id.ac.polbeng.rizkiafriani.githubprofile"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField ("String", "ACCESS_TOKEN", "\"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\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true // Mengaktifkan BuildConfig
    }
}


dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Library untuk menampilkan gambar bergerak splashscreen
    implementation("com.airbnb.android:lottie:5.2.0")
    // Library untuk menampilkan gambar bulat (circle)
    implementation("de.hdodenhof:circleimageview:3.1.0")
    // Library untuk menampilkan gambar melalui URL
    ("kath com.github.bumptech.glide:compiler:4.13.0")
    implementation("com.github.bumptech.glide:glide:4.13.2")
    // Library untuk request API
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Library untuk logging hasil request API
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.9")

}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17) // Ganti ke JDK 17
    }
}
