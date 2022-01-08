package com.example.incentivetimer.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bathtub
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.incentivetimer.di.ApplicationScope
import com.example.incentivetimer.ui.IconKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Reward::class], version = 1)
abstract class ITDatabase : RoomDatabase() {

    abstract fun rewardDao(): RewardDao

    class Callback @Inject constructor(
        private val database: Provider<ITDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope

    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            //DB is lazy instantiated that's the purpose of Provider<ITDatabase>, above
            val rewardDao = database.get().rewardDao()

            applicationScope.launch {
                rewardDao.insertReward(
                    Reward(
                        iconKey = IconKeys.CAKE,
                        name = "1 piece of cake",
                        changeInPercent = 5
                    )
                )
                rewardDao.insertReward(
                    Reward(
                        iconKey = IconKeys.BATH_TUB,
                        name = "Take a bath",
                        changeInPercent = 7
                    )
                )
                rewardDao.insertReward(
                    Reward(
                        iconKey = IconKeys.TV,
                        name = "Watch 1 episode of my favorite show",
                        changeInPercent = 10
                    )
                )
            }

        }
    }
}