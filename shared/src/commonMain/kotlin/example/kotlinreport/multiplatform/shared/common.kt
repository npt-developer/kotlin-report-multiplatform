package example.kotlinreport.multiplatform.shared

expect class Platform() {
    val platform: String
}

class Greeting {
    fun greeting(): String = "Hello, ${Platform().platform}"
}
