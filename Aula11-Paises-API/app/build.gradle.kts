plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.aula11_paises"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.exemplo.consultapaises"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("com.google.android.material:material:1.12.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // Carregamento da imagem da bandeira
    implementation("com.github.bumptech.glide:glide:4.16.0")
}