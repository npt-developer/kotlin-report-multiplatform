package example.kotlinreport.multiplatform.shared

import android.os.Build

actual class Platform actual constructor() {
    actual val platform: String = "Android"
}

actual class Product(actual val user: String) {
    fun androidSpecificOperation() {
        println("I am ${Build.MODEL} by ${Build.MANUFACTURER}")
    }

    override fun toString() = "Android 2 product of $user for ${Build.MODEL}"
}

actual object Factory {
    actual fun create(config: Map<String, String>) =
        Product(config["user"]!!)

    actual val platform: String = "android"
}

actual class Userx {
    actual var id: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    actual var name: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    actual var sex: String
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
}