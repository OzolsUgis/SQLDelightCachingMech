package com.ugisozols.sqldelightcaching.utilities

sealed class DataResource<T>(val data : T?, val message : String? = null) {
    class Success<T>(data: T?) : DataResource<T>(data)
    class Loading<T>(data: T?) : DataResource<T>(data)
    class Error<T>(data: T? = null, message: String) : DataResource<T>(data, message)
}