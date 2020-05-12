package example.kotlinreport.multiplatform.shared.db

import example.kotlinreport.multiplatform.shared.model.User

public abstract class DatabaseManager {

    abstract fun countUser(): Long

    abstract fun getUserList(offset: Long, limit: Long): ArrayList<User>

    abstract fun insertUser(user: User): Unit
}