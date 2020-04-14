package example.kotlinreport.multiplatform.application

import android.app.Application
import example.kotlinreport.shared.config.AppConfig
import com.facebook.stetho.Stetho

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // for debug
        if (AppConfig.APP_DEBUG) {
            // debug network + sqlite, ...
            Stetho.initializeWithDefaults(this)

        }
    }
}