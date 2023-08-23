package com.example.contextualtriggers

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log

class FitnessContentResolver(context: Context)  {

    private val activityUri = Uri.parse("content://com.demo.user.provider/activity")
    private val goalUri = Uri.parse("content://com.demo.user.provider/goal")

    private val resolver: ContentResolver = context.contentResolver
    private val activityCursor = resolver.query(activityUri, null, null, null, null)
    private val goalCursor = resolver.query(goalUri, null, null, null, null)

    @SuppressLint("Range")
    fun getActivity(): ArrayList<Activity> {
        var activityList: ArrayList<Activity> = ArrayList()
        var activity = Activity(1, 1, 100, "20-07-1997", "Y" )
        activityList.add(activity)
    if (activityCursor != null && activityCursor.moveToFirst()) {
        do {
            activity.id  = activityCursor.getInt(activityCursor.getColumnIndex("id"))
            activity.goalIdActivity = activityCursor.getInt(activityCursor.getColumnIndex("goalIdActivity"))
            activity.stepsAchieved = activityCursor.getInt(activityCursor.getColumnIndex("stepsAchieved"))
            activity.date = activityCursor.getString(activityCursor.getColumnIndex("date"))
            activity.historyRecording = activityCursor.getString(activityCursor.getColumnIndex("historyRecording"))

            activityList.add(activity)

        } while (activityCursor.moveToNext())
    }
    //activityCursor?.close()

    return activityList
    }

    @SuppressLint("Range")
    fun getGoal(): ArrayList<Goal> {
        var goalList: ArrayList<Goal> = ArrayList()
        var goal = Goal(1, "Goal A", 2000, "Active", "Y" )
        goalList.add(goal)
        if (goalCursor != null && goalCursor.moveToFirst()) {
            do {
                goal.goalId  = goalCursor.getInt(goalCursor.getColumnIndex("goalId"))
                goal.goalName = goalCursor.getString(goalCursor.getColumnIndex("goalName"))
                goal.targetSteps = goalCursor.getInt(goalCursor.getColumnIndex("targetSteps"))
                goal.status = goalCursor.getString(goalCursor.getColumnIndex("status"))
                goal.editable = goalCursor.getString(goalCursor.getColumnIndex("editable"))

                goalList.add(goal)

            } while (goalCursor.moveToNext())
        }
        //goalCursor?.close()

        return goalList
    }
}