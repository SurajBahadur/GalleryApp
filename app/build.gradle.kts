plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.gallery"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.gallery"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.suraj.gallery.CustomerRunner"
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }




    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
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
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //Architectural Components
    implementation(libs.lifecycle.viewmodel.ktx)

    //Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android.v173)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // RecyclerView
    implementation(libs.androidx.recyclerview)

    // SwipeRefreshLayout
    implementation(libs.androidx.swiperefreshlayout)

    // Image Loading
    implementation(libs.glide)


    ksp(libs.squareup.moshi.kotlin.codegen)

    //Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.common)


    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)

    androidTestImplementation(libs.androidx.junit)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    testImplementation(libs.androidx.core.testing)

    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.6.1") // For RecyclerViewActions
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.6.1")

    // For instrumentation tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.56.2")
    androidTestAnnotationProcessor("com.google.dagger:hilt-compiler:2.56.2")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.56.2")

    // For local unit tests
    testAnnotationProcessor("com.google.dagger:hilt-compiler:2.56.2")

}
kapt {
    correctErrorTypes = true
}