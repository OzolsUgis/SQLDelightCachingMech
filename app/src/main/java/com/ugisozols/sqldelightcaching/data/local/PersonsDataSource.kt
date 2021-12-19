package com.ugisozols.sqldelightcaching.data.local

import kotlinx.coroutines.flow.Flow
import personsdatabase.personsdb.DeletedPersonsIdsDB
import personsdatabase.personsdb.Persons

interface PersonsDataSource {
    suspend fun addPerson(person : Persons)

    suspend fun deletePerson(id : String)

    suspend fun getPersonById(id : String) : Persons?

    fun getAllPersons() : Flow<List<Persons>>

    suspend fun updatePerson(id : String)

    fun getAllUnsyncedPersons() : List<Persons>

    suspend fun insertDeletedPersonsId(locallyDeletedId : String)

    fun getAllIds(): List<DeletedPersonsIdsDB>

    suspend fun deleteDeletedIds(locallyDeletedId : String)
}