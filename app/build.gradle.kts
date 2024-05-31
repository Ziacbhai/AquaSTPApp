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
        versionCode = 100
        versionName = "1.0.0"
        multiDexEnabled = true

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
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.github.dhaval2404:imagepicker:2.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.7.4")
    implementation("androidx.navigation:navigation-ui:2.7.4")
    implementation("com.google.android.gms:play-services-cast-framework:21.3.0")
    implementation("com.android.support:support-annotations:28.0.0")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    implementation("com.gitee.archermind-ti:datepicker:1.0.1")
    implementation("com.google.ar.sceneform:filament-android:1.17.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.legacy:legacy-support-v4:1.0.0")
    implementation ("com.google.android.material:material:1.5.0")
    implementation ("com.google.android.material:material:1.3.0-alpha03")
    implementation("com.github.Drjacky:ImagePicker:2.3.22")
   /* implementation ("com.google.android.gms:play-services-auth:17.0.1")*/

    implementation ("androidx.viewpager2:viewpager2:1.0.0-alpha01")
    implementation ("io.github.chaosleung:pinview:1.4.4")
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation ("com.squareup.picasso:picasso:2.71828")
    implementation ("com.android.support:multidex:1.0.3")
    implementation ("com.airbnb.android:lottie:3.4.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.1")
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    implementation ("com.wdullaer:materialdatetimepicker:3.6.0")
    /*implementation ("com.androidx.autofill:autofill:1.1.0")*/
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")

    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("com.github.barteksc:android-pdf-viewer:2.8.2")
    implementation ("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation ("com.karumi:dexter:6.2.2")
    implementation ("com.android.volley:volley:1.2.1")


    implementation ("com.afollestad.material-dialogs:core:3.3.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.github.barteksc:android-pdf-viewer:3.2.0-beta.1")
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("androidx.test.espresso:espresso-core:3.5.0")
    testImplementation ("com.github.yalantis:ucrop:2.2.6")




}