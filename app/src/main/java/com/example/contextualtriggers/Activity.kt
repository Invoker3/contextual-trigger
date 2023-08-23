package com.example.contextualtriggers

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Activity",
    foreignKeys = [ForeignKey(
        entity = Goal::class,
        childColumns = ["goalIdActivity"],
        parentColumns = ["goalId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Activity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "goalIdActivity", index = true)
    var goalIdActivity: Int,

    @ColumnInfo(name = "stepsAchieved")
    var stepsAchieved: Int,

    @ColumnInfo(name = "date")
    var date: String?,

    @ColumnInfo(name = "historyRecording")
    var historyRecording: String?
) {
    constructor(goalIdActivity: Int, stepsAchieved: Int, date: String, historyRecording: String) :
            this(0, goalIdActivity, stepsAchieved, date, historyRecording)

}

