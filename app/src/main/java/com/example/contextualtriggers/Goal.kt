package com.example.contextualtriggers

import androidx.room.*

@Entity(tableName = "Goal",
    indices = [Index(value = ["goalName", "targetSteps", "targetSteps"],
    unique = true)])

data class Goal(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "goalId")
    var goalId: Int,

    @ColumnInfo(name = "goalName")
    var goalName: String,

    @ColumnInfo(name = "targetSteps")
    var targetSteps: Int,

    @ColumnInfo(name = "status")
    var status: String,

    @ColumnInfo(name = "editable")
    var editable: String
) {
    constructor(goalName: String, targetSteps: Int, status: String, editable: String) :
    this(0,goalName, targetSteps, status, editable)
}

