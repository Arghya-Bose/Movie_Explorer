import org.jetbrains.kotlin.commonizer.OptimisticNumberCommonizationEnabledKey.alias
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
}
val localProperties = Properties()

localProperties.load(
    rootProject.file("local.properties").inputStream()
)

android {

    namespace = "com.example.movieexplorer"
    compileSdk {
        version = release(37)
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true

    }

    defaultConfig {
        applicationId = "com.example.movieexplorer"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "TMDB_ACCESS_TOKEN",
            "\"${localProperties.getProperty("TMDB_ACCESS_TOKEN")}\""
        )
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }


}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    implementation("androidx.navigation:navigation-fragment-ktx:2.9.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.5")
    implementation("com.google.android.material:material:1.13.0")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    implementation("io.coil-kt.coil3:coil:3.3.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.3.0")

    implementation("androidx.room:room-runtime:2.8.0")
    implementation("androidx.room:room-ktx:2.8.0")
    ksp("androidx.room:room-compiler:2.8.0")

    implementation("com.google.android.material:material:1.13.0")
    implementation("androidx.core:core-splashscreen:1.0.1")

    implementation("com.facebook.shimmer:shimmer:0.5.0")


}