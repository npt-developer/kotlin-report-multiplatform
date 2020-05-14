package example.kotlinreport.multiplatform.shared

expect class Product {
    val user: String
}

expect class Userx {
    var id: String
    var name: String
    var sex: String
}

expect object Factory {
    fun create(config: Map<String, String>): Product
    val platform: String
}
