plugins {
    id("com.android.application")
}

android {
    namespace = "com.ziac.aquastpapp"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.ziac.aquastpapp"
        minSdk = 21
        targetSdk = 35
        versionCode = 106
        versionName = "1.0.6"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
        }

    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            //isDebuggable = true
            isShrinkResources = false
            // Enable test coverage for ANR detection
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            ndk {
                debugSymbolLevel = "FULL" // or SYMBOL_TABLE
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    // Update these libraries:
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:2.2.0"))
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.github.dhaval2404:imagepicker:2.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.navigation:navigation-fragment:2.9.0")
    implementation("androidx.navigation:navigation-ui:2.9.0")
    implementation("com.google.android.gms:play-services-cast-framework:22.1.0")
    implementation("com.android.support:support-annotations:28.0.0")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation("com.gitee.archermind-ti:datepicker:1.0.1")
    implementation("com.google.ar.sceneform:filament-android:1.17.1")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.github.Drjacky:ImagePicker:2.3.22")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("io.github.chaosleung:pinview:1.4.4")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.android.support:multidex:1.0.3")
    implementation("com.airbnb.android:lottie:6.6.7")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.wdullaer:materialdatetimepicker:4.2.3")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.afollestad.material-dialogs:core:3.3.0")
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:3.0.0")
    testImplementation("com.github.yalantis:ucrop:2.2.6")
    testImplementation("junit:junit:4.13.2")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    debugImplementation("com.github.anrwatchdog:anrwatchdog:1.4.0")

}