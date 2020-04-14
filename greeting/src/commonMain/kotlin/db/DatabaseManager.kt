package org.greeting.db

import kotlin.jvm.JvmStatic

import org.greeting.model.SexType
import org.greeting.model.User

public abstract class DatabaseManager {

    abstract fun countUser(): Long

    abstract fun getUserList(offset: Long, limit: Long): ArrayList<User>

    abstract fun insertUser(user: User): Long
}