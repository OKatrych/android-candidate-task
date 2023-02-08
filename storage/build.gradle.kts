plugins {
    id("com.android.library")
    kotlin("android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.nordlocker.storage"
    compileSdk = 33

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":domain"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.0")

    implementation("io.insert-koin:koin-android:3.3.2")

    implementation("androidx.room:room-runtime:2.5.0")
    implementation("androidx.room:room-ktx:2.5.0")
    ksp("androidx.room:room-compiler:2.5.0")

    testImplementation("junit:junit:4.13.2")
}
