package example.kotlinreport.shared.db

import example.kotlinreport.shared.model.User

public abstract class DatabaseManager {

    abstract fun countUser(): Long

    abstract fun getUserList(offset: Long, limit: Long): ArrayList<User>

    abstract fun insertUser(user: User): Unit
}