package example.kotlinreport.shared.db

import com.squareup.sqldelight.drivers.ios.NativeSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import example.kotlinreport.shared.model.SexType
import example.kotlinreport.shared.model.User
import example.kotlinreport.shared.MyDatabase
import example.kotlinreport.shared.UserQueries
import kotlin.collections.ArrayList

public class IosDatabaseManager: DatabaseManager {

    private val mUserQueries: UserQueries;

    constructor(): super() {
        val driver: SqlDriver = NativeSqliteDriver(MyDatabase.Schema, "db")
        val database = MyDatabase(driver)
        this.mUserQueries = database.userQueries

    }

    override fun countUser(): Long {
        return this.mUserQueries.countUser().executeAsOne()
    }

    override fun getUserList(offset: Long, limit: Long): ArrayList<User> {
        val list: List<User>  = this.mUserQueries.selectAll(limit, offset, mapper = {id, name, sex, avatar -> User(id, name, SexType.valueOf(sex.toInt()), avatar) }).executeAsList();

        return ArrayList(list)
    }

    override fun insertUser(user: User): Unit {
        this.mUserQueries.insertItem(user.name, user.sex.value.toShort(), user.avatar!!)
    }
}