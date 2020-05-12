package example.kotlinreport.multiplatform.shared.db

import com.squareup.sqldelight.drivers.ios.NativeSqliteDriver
import example.kotlinreport.multiplatform.shared.MyDatabase
import example.kotlinreport.multiplatform.shared.config.AppConfig

actual fun createDb(): MyDatabase {
    return MyDatabase(NativeSqliteDriver(MyDatabase.Schema, AppConfig.DATABASE_NAME))
}