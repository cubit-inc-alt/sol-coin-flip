package core.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import core.database.DatabaseConfig.DATABASE_NAME


fun getDatabaseBuilder(ctx: Context): RoomDB {
    val dbFile = ctx.getDatabasePath(DATABASE_NAME)
    return Room.databaseBuilder(ctx, RoomDB::class.java, dbFile.absolutePath).setDriver(BundledSQLiteDriver())
        .fallbackToDestructiveMigration(true).allowMainThreadQueries()
        .build()
}
