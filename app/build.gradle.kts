plugins {
    id("com.android.application")
}

android {
    namespace = "com.ziac.aquastpapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ziac.aquastpapp"
        minSdk = 19
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.7.4")
    implementation("androidx.navigation:navigation-ui:2.7.4")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("com.google.android.material:material:1.5.0")
    implementation ("com.google.android.material:material:1.3.0-alpha03")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("androidx.viewpager2:viewpager2:1.0.0-alpha01")


    implementation ("io.github.chaosleung:pinview:1.4.4")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")


    implementation ("com.airbnb.android:lottie:3.4.0")

}