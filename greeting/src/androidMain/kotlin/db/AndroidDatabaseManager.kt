package org.greeting.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.util.Log
import org.greeting.model.SexType
import org.greeting.model.User

public class AndroidDatabaseManager: DatabaseManager {
    private val mContext: Context

    companion object {
        @JvmStatic
        private var mDbInstance: DatabaseHelper? = null
        @JvmStatic
        public fun getDbInstance(context: Context): DatabaseHelper? {
            synchronized(AndroidDatabaseManager::class) {
                if (mDbInstance == null) {
                    mDbInstance = DatabaseHelper(context.applicationContext)
                }
                return mDbInstance
            }
        }
    }

    constructor(context: Context): super() {
        this.mContext = context.applicationContext
    }

    override fun countUser(): Long {
        synchronized(AndroidDatabaseManager::class) {
            return DatabaseUtils.queryNumEntries(getDbInstance(this.mContext)!!.readableDatabase, User.TABLE_NAME)
        }
    }

    override fun getUserList(offset: Long, limit: Long): ArrayList<User> {
        synchronized(AndroidDatabaseManager::class) {
            var list: ArrayList<User> = ArrayList()

            var db = getDbInstance(this.mContext)!!.readableDatabase
            val columns: Array<String> = arrayOf(
                    User.COLUMN_ID_NAME,
                    User.COLUMN_NAME_NAME,
                    User.COLUMN_SEX_NAME,
                    User.COLUMN_AVATAR_NAME
            )
            var cursor: Cursor = db.query(
                    User.TABLE_NAME,
                    columns,
                    null,
                    null,
                    null,
                    null,
                    "${User.COLUMN_ID_NAME} DESC",
                    "${offset},${limit}"
            )
            if (cursor.moveToFirst()) {
                do {
                    list.add(User(
                            cursor.getLong(cursor.getColumnIndex(User.COLUMN_ID_NAME)),
                            cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME_NAME)),
                            SexType.valueOf(cursor.getInt(cursor.getColumnIndex(User.COLUMN_SEX_NAME))),
                            cursor.getString(cursor.getColumnIndex(User.COLUMN_AVATAR_NAME))
                    ))
                } while (cursor.moveToNext())
            }
            cursor.close()
            return list
        }
    }

    override fun insertUser(user: User): Long {
        Log.d("DatabaseManager", "Insert user")
        synchronized(DatabaseManager::class) {
            var db = getDbInstance(this.mContext)!!.writableDatabase
            db.beginTransaction()

            var contentValues: ContentValues = ContentValues()
            contentValues.put(User.COLUMN_NAME_NAME, user.name)
            contentValues.put(User.COLUMN_SEX_NAME, user.sex.value.toString())
            contentValues.put(User.COLUMN_AVATAR_NAME, user.avatar)

            val result: Long = db.insert(User.TABLE_NAME, null, contentValues)
            Log.d("DatabaseManager", "result ${result}")

            db.setTransactionSuccessful()
            db.endTransaction()
            return result
        }
    }
}