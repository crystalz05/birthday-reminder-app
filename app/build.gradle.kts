import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

android {
    namespace = "com.tyro.birthdayreminder"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tyro.birthdayreminder"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = gradleLocalProperties(rootDir, providers)

//        val webClientId: String = gradleLocalProperties(
//            rootDir,
//            providers
//        )
//            .getProperty("WEB_CLIENT_ID") ?: ""
        buildConfigField("String", "WEB_CLIENT_ID", "\"${localProperties.getProperty("WEB_CLIENT_ID")}\"")
    }

    buildFeatures{
        buildConfig = true // enable BuildConfig generation
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.googleid)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.navigation.compose)


    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.hilt.navigation.compose)


    implementation(libs.play.services.auth)

    implementation(libs.androidx.credentials) // latest stable
    implementation(libs.androidx.credentials.play.services.auth)

    implementation(libs.google.firebase.analytics)
    implementation(libs.com.google.firebase.firebase.auth)
    implementation(libs.google.firebase.firestore)

}