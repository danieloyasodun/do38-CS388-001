plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.0"
}

android {
    namespace = "com.example.bitfit"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bitfit"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    val roomVersion = "2.4.2"
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt("androidx.room:room-compiler:$roomVersion")

    implementation(libs.androidx.fragment.ktx)

    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx.v180)
    implementation(libs.androidx.appcompat.v142)
    implementation(libs.material.v161)
    implementation(libs.androidx.constraintlayout.v220)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.recyclerview.selection)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v113)
    androidTestImplementation(libs.androidx.espresso.core.v340)
}