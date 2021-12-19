package com.ugisozols.sqldelightcaching.utilities

import kotlinx.coroutines.flow.*

inline fun <LOCALDATA , REMOTEAPIDATA> dataBinding(
    crossinline query : () -> Flow<LOCALDATA>,
    crossinline fetch : suspend () -> REMOTEAPIDATA,
    crossinline addFetchedResultToLocalData : suspend (REMOTEAPIDATA) -> Unit,
    crossinline onFetchFailed : (Throwable) -> Unit = {},
    crossinline shouldFetch : (LOCALDATA) -> Boolean = {true}
) = flow {
    val dataFromLocalDatabase = query().first()

    val flowResult =
        if (shouldFetch(dataFromLocalDatabase)) {
            emit(DataResource.Loading(dataFromLocalDatabase))
            try {
                val dataFromRemoteApi = fetch()
                addFetchedResultToLocalData(dataFromRemoteApi)
                query().map { data ->
                    DataResource.Success(data)
                }
            } catch (t: Throwable) {
                onFetchFailed(t)
                query().map { data ->
                    DataResource.Error(data, "Server connection error")
                }
            }
        } else {
            query().map { data ->
                DataResource.Success(data)
            }
        }
    emitAll(flowResult)
}