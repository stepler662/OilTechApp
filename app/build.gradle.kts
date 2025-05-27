plugins {
    id("com.android.application")

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

    // Room Database
    implementation ("androidx.room:room-runtime:2.5.0")
    annotationProcessor ("androidx.room:room-compiler:2.5.0") // Для Java
    implementation ("androidx.room:room-ktx:2.5.0")
    implementation ("androidx.annotation:annotation:1.6.0")

    // QR Scanner
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")

    // Для работы с камерой
    implementation("androidx.camera:camera-core:1.2.3")
    implementation("androidx.camera:camera-camera2:1.2.3")

}