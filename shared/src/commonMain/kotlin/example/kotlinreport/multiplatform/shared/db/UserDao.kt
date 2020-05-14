package example.kotlinreport.multiplatform.shared.db

import example.kotlinreport.multiplatform.shared.MyDatabase
import example.kotlinreport.multiplatform.shared.UserQueries
import example.kotlinreport.multiplatform.shared.createDb
import example.kotlinreport.multiplatform.shared.model.SexType
import example.kotlinreport.multiplatform.shared.model.User

class UserDao {
    private val mMyDatabase: MyDatabase
    private val mUserQueries: UserQueries

    constructor() {
        this.mMyDatabase = createDb()
        this.mUserQueries = this.mMyDatabase.userQueries;
    }

    fun countUser(): Long {
        return this.mUserQueries.countUser().executeAsOne()
    }

    fun getUserList(offset: Long, limit: Long): List<User> {
            return this.mUserQueries.selectAll(limit, offset, mapper = { id, name, sex, avatar -> User(id, name, SexType.valueOf(sex.toInt()), avatar) }).executeAsList();
    }

    fun insertUser(user: User): Unit {
        this.mUserQueries.insertItem(user.name, user.sex.value.toShort(), user.avatar!!)
    }

    fun insertUserx(name: String, sex: String){
        this.mUserQueries.insertItem(name, sex.toShort(), "")
    }
}