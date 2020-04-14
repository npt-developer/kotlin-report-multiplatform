plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.multiplatform")
}

android {
    compileSdkVersion(29)
    defaultConfig {
        applicationId = "org.konan.multiplatform"
        minSdkVersion(15)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    packagingOptions {
        exclude("META-INF/main.kotlin_module")
    }
}

kotlin {
    android()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("androidx.appcompat:appcompat:1.1.0")

    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("com.google.android.material:material:1.1.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")

    //    recycler view swipe
    implementation("com.chauthai.swipereveallayout:swipe-reveal-layout:1.4.1")
    // refresh layout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.0.0")
    // debug: chrome inspect network + sqlite
    implementation("com.facebook.stetho:stetho:1.5.1")

    implementation(project(":shared"))


//    testImplementation("junit:junit:4.12")
}
