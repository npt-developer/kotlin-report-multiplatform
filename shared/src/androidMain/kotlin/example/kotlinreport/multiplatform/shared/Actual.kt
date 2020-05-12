package example.kotlinreport.multiplatform.shared

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import example.kotlinreport.multiplatform.shared.config.AppConfig

lateinit var myAppContext: Context

actual fun createDb(): MyDatabase {
    return MyDatabase(AndroidSqliteDriver(MyDatabase.Schema,
        myAppContext, AppConfig.DATABASE_NAME))
}