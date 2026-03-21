plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
    kotlin("plugin.serialization") version "2.0.0"
    alias(libs.plugins.google.gms.google.services)

}

android {
    namespace = "com.example.zomatoclone"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.zomatoclone"
        minSdk = 24
        targetSdk = 36
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
    //Razorpay
    implementation("com.razorpay:checkout:1.6.33")


    //Shared Element Transition
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.navigation.compose)

    //DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")


    //Rooms
    implementation ("androidx.room:room-runtime:2.6.1")
    implementation ("androidx.room:room-ktx:2.6.1")
    kapt ("androidx.room:room-compiler:2.6.1")

    //Retrofit , gson , json
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")


    //More icons
    implementation("androidx.compose.material:material-icons-extended")
    implementation(platform(libs.firebase.bom))

    implementation("androidx.compose.animation:animation:1.5.4")


    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.compose.material3:material3")

    //FirebaseStore
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-firestore-ktx")

    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore.ktx)

    implementation("androidx.compose.material:material-icons-extended")


    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.bumptech.glide:compose:1.0.0-alpha.1")

    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    // this ia for hillt dependency injection
    implementation("com.google.dagger:hilt-android:2.50")
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.ui.text)
    implementation(libs.ui)
    implementation(libs.material3)
    implementation(libs.firebase.database)
    implementation(libs.firebase.firestore)
    kapt("com.google.dagger:hilt-android-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    kapt("androidx.hilt:hilt-compiler:1.2.0")



    // this is for coil
    implementation("io.coil-kt:coil-compose:2.6.0")


    // Navigation
    implementation("androidx.navigation:navigation-compose:2.8.0-beta06")

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")



    //this is for pager
    implementation ("com.google.accompanist:accompanist-pager:0.28.0")
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.28.0")

// this is for payment getway
    implementation ("com.razorpay:checkout:1.6.40")


    //custem bottom nev bar
    implementation ("com.canopas.compose-animated-navigationbar:bottombar:1.0.1")

    //lottie for Place order dialog
    implementation ("com.airbnb.android:lottie-compose:4.2.0")

    implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")

    //splashScreen
    implementation("androidx.core:core-splashscreen:1.0.1")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
