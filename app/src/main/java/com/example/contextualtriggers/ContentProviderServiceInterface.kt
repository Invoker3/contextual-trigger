package com.example.contextualtriggers

interface ContentProviderServiceInterface {
    fun getContentProviderData(): Boolean
    fun setTimeTrigger()
    class NotificationException(message: String): Exception()
}