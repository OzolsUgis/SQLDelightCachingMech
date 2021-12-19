package com.ugisozols.sqldelightcaching.data.local

import personsdatabase.personsdb.DeletedPersonsIdsDB

interface DeletedPersonsIdsDataSource {
    suspend fun insertDeletedPersonsId(id : String)

    fun getAllIds(): List<DeletedPersonsIdsDB>

    suspend fun deleteDeletedIds(id : String)

}