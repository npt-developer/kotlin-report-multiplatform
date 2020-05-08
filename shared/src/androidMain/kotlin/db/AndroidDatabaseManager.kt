package example.kotlinreport.shared.db

import android.content.Context
import android.util.Log
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import example.kotlinreport.shared.model.SexType
import example.kotlinreport.shared.model.User
import example.kotlinreport.shared.MyDatabase
import example.kotlinreport.shared.UserQueries
import kotlin.collections.ArrayList

public class AndroidDatabaseManager: DatabaseManager {

    private val mUserQueries: UserQueries;

    constructor(context: Context): super() {
        val driver: SqlDriver = AndroidSqliteDriver(MyDatabase.Schema, context.applicationContext, "db")
        val database = MyDatabase(driver)
        this.mUserQueries = database.userQueries

    }

    override fun countUser(): Long {
        synchronized(AndroidDatabaseManager::class) {
            return this.mUserQueries.selectAll(mapper = {id, name, sex, avatar -> User(id, name, SexType.valueOf(sex.toInt()), avatar) }).executeAsList().size.toLong()
        }
    }

    override fun getUserList(offset: Long, limit: Long): ArrayList<User> {
        synchronized(AndroidDatabaseManager::class) {
            val list: List<User>  = this.mUserQueries.selectAll(mapper = {id, name, sex, avatar -> User(id, name, SexType.valueOf(sex.toInt()), avatar) }).executeAsList();
            val result: ArrayList<User> = ArrayList(list)

            return result
        }
    }

    override fun insertUser(user: User): Unit {
        Log.d("DatabaseManager", "Insert user")
        synchronized(DatabaseManager::class) {
            this.mUserQueries.insertItem(user.name, user.sex.value.toShort(), user.avatar!!)
        }
    }
}