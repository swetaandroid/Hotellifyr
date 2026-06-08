plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    id ("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.xongolab.hotellifyr"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.xongolab.hotellifyr"
        minSdk = 24
        targetSdk = 37
        versionCode = 3
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        viewBinding = true
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.retrofit2.adapter.rxjava2)
    implementation(libs.converter.scalars)
    implementation(libs.retrofit2.converter.simplexml)
    implementation(libs.gson)
    implementation(libs.logging.interceptor)

    //Dots Indicator
    implementation(libs.tbuonomo.dotsindicator)

    //Flexbox
    implementation(libs.flexbox)

    //Shimmer
    implementation(libs.shimmer)

    // OTP view
    implementation(libs.otpview)

    // Country Code Picker
    implementation(libs.ccp)

    //Image Loading
    implementation(libs.fresco)

    // Cropping Image Selection
    implementation(libs.ucrop)

    // Dimension
    implementation(libs.ssp.android)
    implementation(libs.sdp.android)

    implementation(libs.play.services.location)

    implementation(libs.glide)

    implementation(libs.play.services.maps)

    implementation(libs.play.services.maps.utils)

    implementation(libs.circleimageview)

    implementation(libs.glide.transformations)

    implementation (libs.awesome.calendar)

    implementation (libs.payu.checkout.pro)

    implementation(libs.okhttp)

    //firebase
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.analytics)

    //Crashlytics
    implementation(platform(libs.firebase.bom))

    implementation (libs.androidx.media3.exoplayer)
    implementation (libs.androidx.media3.ui)

    implementation (libs.stripe.android)

}