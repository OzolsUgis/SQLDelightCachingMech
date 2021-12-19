package com.ugisozols.sqldelightcaching.data.local


import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.ugisozols.sqldelightpersons.PersonsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import personsdatabase.personsdb.DeletedPersonsIdsDB
import personsdatabase.personsdb.Persons

class PersonsDataSourceImpl(
    database: PersonsDatabase
) : PersonsDataSource {

    private val queries = database.personsQueries
    private val deletedQueries = database.deletedPersonsIdsDBQueries


    override suspend fun addPerson(person: Persons) {
        return withContext(Dispatchers.IO) {
            queries.addPerson(
                person.firstName,
                person.lastName,
                person.phoneNumber,
                id = person.id
            )
        }
    }

    override suspend fun deletePerson(id: String) {
        return withContext(Dispatchers.IO) {
            queries.deletePerson(id)
        }
    }

    override suspend fun getPersonById(id: String): Persons? {
        return withContext(Dispatchers.IO) {
            queries.getPersonsById(id).executeAsOneOrNull()
        }
    }

    override fun getAllPersons(): Flow<List<Persons>> {
        return queries.getAllPersons().asFlow().mapToList()
    }

    override suspend fun updatePerson(id: String) {
        return withContext(Dispatchers.IO) {
            queries.updatePerson(id)
        }
    }

    override fun getAllUnsyncedPersons(): List<Persons> {
        return queries.getAllUnsyncedPersons().executeAsList()
    }

    override suspend fun insertDeletedPersonsId(id: String) {
        return withContext(Dispatchers.IO) {
            deletedQueries.insertDeletedPersonsId(id)
        }
    }

    override fun getAllIds(): List<DeletedPersonsIdsDB> {
        return deletedQueries.getAllIds().executeAsList()
    }

    override suspend fun deleteDeletedIds(id: String) {
        return withContext(Dispatchers.IO) {
            deletedQueries.deleteDeletedIds(id)
        }
    }
}