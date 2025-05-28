plugins {
    id("com.android.application")
    id("com.google.gms.google-services") // For Firebase
}

android {
    namespace = "com.oiltechapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.oiltechapp"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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

    // Вкл ViewBinding
    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1") // Для AppCompatActivity
    implementation("com.google.android.material:material:1.11.0") // Для Material Design


    // Firebase
    implementation (platform("com.google.firebase:firebase-bom:32.8.1"))
    implementation ("com.google.firebase:firebase-auth")
    implementation ("com.google.firebase:firebase-firestore")

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    // QR Scanner
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    // Для работы с камерой
    implementation("androidx.camera:camera-core:1.2.3")
    implementation("androidx.camera:camera-camera2:1.2.3")


}