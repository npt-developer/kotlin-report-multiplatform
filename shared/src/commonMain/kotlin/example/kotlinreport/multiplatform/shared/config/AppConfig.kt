package example.kotlinreport.multiplatform.shared.config

object AppConfig {
    public const val APP_DEBUG: Boolean = true
    public const val DATABASE_NAME: String = "db"


    object Pagination {
        public const val PAGE_SIZE: Long = 10
    }

    object User {
        public const val AVATAR_FOLDER_NAME: String = "avatar"
    }
}