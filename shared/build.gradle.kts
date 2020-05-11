import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id( "com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("com.squareup.sqldelight")
}

group = "example.kotlinreport.shared"
version = 1.0

android {
    compileSdkVersion(27)
    defaultConfig {
        minSdkVersion(15)
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

sqldelight {
    database("MyDatabase") {
        packageName = "example.kotlinreport.shared"
        sourceFolders = listOf("sqldelight")
//        schemaOutputDirectory = file("build/dbs")
//        dependency(project(":androidApp"))
    }
    linkSqlite = false
}

dependencies {
    // Specify Kotlin/JVM stdlib dependency.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7")

    implementation("androidx.core:core-ktx:1.2.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")

    implementation("com.squareup.sqldelight:android-driver:1.3.0")

    androidTestImplementation("junit:junit:4.12")
    androidTestImplementation("org.jetbrains.kotlin:kotlin-test")
    androidTestImplementation("org.jetbrains.kotlin:kotlin-test-junit")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}

kotlin {
   android("android")
   
   val buildForDevice = project.findProperty("device") as? Boolean ?: false
   val iosTarget = if(buildForDevice) iosArm64("ios") else iosX64("ios")
   iosTarget.binaries {
      framework {
         // Disable bitcode embedding for the simulator build.
         if (!buildForDevice) {
            embedBitcode("disable")
         }
      }
   }

   sourceSets {
      commonMain {
         dependencies {
            implementation( "org.jetbrains.kotlin:kotlin-stdlib-common")
             implementation("com.squareup.sqldelight:runtime:1.3.0")
         }
      }
      commonTest {
         dependencies {
            implementation( "org.jetbrains.kotlin:kotlin-test-common")
             implementation("org.jetbrains.kotlin:kotlin-test-annotations-common")
         }
      }
    }
    sourceSets["iosMain"].dependencies {
        implementation("com.squareup.sqldelight:native-driver:1.3.0")
    }
}

tasks.register("copyFramework") {
    val buildType = project.findProperty("kotlin.build.type") as? String ?: "DEBUG"
    dependsOn("link${buildType.toLowerCase().capitalize()}FrameworkIos")

    doLast {
        val srcFile = (kotlin.targets["ios"] as KotlinNativeTarget).binaries.getFramework(buildType).outputFile
        val targetDir = project.property("configuration.build.dir")!!
        copy {
            from(srcFile.parent)
            into(targetDir)
            include( "shared.framework/**")
            include("shared.framework.dSYM")
        }
    }
}

tasks.register("iosTest")  {
    val  device = project.findProperty("iosDevice") as? String ?: "iPhone 8"
    dependsOn("linkDebugTestIos")
    group = JavaBasePlugin.VERIFICATION_GROUP
    description = "Runs tests for target 'ios' on an iOS simulator"

    doLast {
        val  binary = (kotlin.targets["ios"] as KotlinNativeTarget).binaries.getTest("DEBUG").outputFile
        exec {
            commandLine("xcrun", "simctl", "spawn", "--standalone", device, binary.absolutePath)
        }
    }
}
