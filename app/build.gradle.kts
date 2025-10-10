import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
    kotlin("plugin.serialization") version "2.2.10"

}

android {
    namespace = "com.tyro.birthdayreminder"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.tyro.birthdayreminder"
        minSdk = 24
        targetSdk = 36
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
        buildConfigField("String", "GEMINI_API_KEY", "\"${localProperties.getProperty("GEMINI_API_KEY")}\"")

    }

    buildFeatures{
        buildConfig = true // enable BuildConfig generation
    }

//    configurations.all {
//        resolutionStrategy {
//            force("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.9.0")
//
//        }
//    }

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

//    implementation("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.9.0")
//    kapt("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.9.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.googleid)
    implementation(libs.generativeai)
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
    implementation (libs.firebase.storage)

    //supa base

    implementation(platform("io.github.jan-tennert.supabase:bom:3.2.3"))
    implementation("io.github.jan-tennert.supabase:storage-kt-android:3.2.3")
    implementation("io.github.jan-tennert.supabase:realtime-kt:3.2.3")
    implementation("io.github.jan-tennert.supabase:postgrest-kt:3.2.3")
    implementation("io.ktor:ktor-client-android:3.2.3")

    implementation("io.coil-kt.coil3:coil-network-okhttp:3.3.0")
    implementation("io.coil-kt.coil3:coil-gif:3.3.0")
    implementation("io.coil-kt.coil3:coil-svg:3.3.0")
    implementation("io.coil-kt.coil3:coil-compose:3.3.0")


    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation (libs.firebase.messaging)

    implementation("androidx.work:work-runtime-ktx:2.10.0")

    //Gemini Api calling
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.14")

}
