package com.example.incentivetimer.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.incentivetimer.ui.IconKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "rewards")
@Parcelize
data class Reward(
    val iconKey: IconKey,
    val name: String,
    val changeInPercent: Int,
    val isUnlocked: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Long = 0
) : Parcelable
