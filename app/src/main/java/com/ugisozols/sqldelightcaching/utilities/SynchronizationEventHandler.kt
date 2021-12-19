package com.ugisozols.sqldelightcaching.utilities

open class SynchronizationEventHandler<T>(private val content: T) {
    var hasBeenSynchronized = false
        private set

    fun getIfNotSynchronized() = if(hasBeenSynchronized){
        null
    }else{
        hasBeenSynchronized = true
        content
    }

    fun getContent() = content
}