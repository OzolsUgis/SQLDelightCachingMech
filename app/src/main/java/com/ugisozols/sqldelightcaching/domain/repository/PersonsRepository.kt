package com.ugisozols.sqldelightcaching.domain.repository

import android.app.Application
import com.ugisozols.sqldelightcaching.data.local.PersonsDataSource
import com.ugisozols.sqldelightcaching.data.remote.PersonsApi
import com.ugisozols.sqldelightcaching.data.remote.request.DeletePerson
import com.ugisozols.sqldelightcaching.utilities.checkInternetConnectivity
import com.ugisozols.sqldelightcaching.utilities.dataBinding
import com.ugisozols.sqldelightcaching.utilities.DataResource
import kotlinx.coroutines.flow.Flow
import personsdatabase.personsdb.Persons
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class PersonsRepository @Inject constructor(
    private val personsApi: PersonsApi,
    private val personsDataSource: PersonsDataSource,
    private val context: Application
) {
    suspend fun insertPerson(person: Persons) {
        val insertPerson = try {
            personsApi.insertPerson(person)
        } catch (e: Exception) {
            null
        }
        if (insertPerson != null && insertPerson.isSuccessful) {
            personsDataSource.addPerson(person)
            personsDataSource.updatePerson(person.id)

        } else {
            personsDataSource.addPerson(person)

        }
    }

    private suspend fun insertAllPersons(listOfPersons: List<Persons>) {
        listOfPersons.forEach { person ->
            insertPerson(person)
        }
    }

    suspend fun getPersonById(id: String): Persons? {
        return personsDataSource.getPersonById(id)
    }


    suspend fun deletePerson(personId: String) {
        val deletePerson = try {
            personsApi.deletePerson(DeletePerson(personId))
        } catch (e: Exception) {
            null
        }

        personsDataSource.deletePerson(personId)
        if (deletePerson == null || !deletePerson.isSuccessful) {
            personsDataSource.insertDeletedPersonsId(personId)
        } else {
            deleteLocallyDeletedPersonId(personId)

        }
    }

    suspend fun deleteLocallyDeletedPersonId(personId: String) {
        personsDataSource.deleteDeletedIds(personId)
    }

    private var currPersonsList: Response<List<Persons>>? = null

    suspend fun synchronizePersons() {
        val unsyncedPersons = personsDataSource.getAllUnsyncedPersons()
        unsyncedPersons.forEach { person ->
            personsApi.insertPerson(person)
        }
        val locallyDeletedPersons = personsDataSource.getAllIds()
        locallyDeletedPersons.forEach { personsId -> deletePerson(personsId.locallyDeletedId!!) }

        currPersonsList = personsApi.getPersons()
        currPersonsList?.body()?.let { persons ->
            insertAllPersons(persons)
        }
    }


    fun getAllPersons(): Flow<DataResource<List<Persons>>> {
        return dataBinding(
            query = {
                personsDataSource.getAllPersons()
            },
            fetch = {
                synchronizePersons()
                currPersonsList
            },
            addFetchedResultToLocalData = { response ->
                response?.body()?.let { fetchedResult ->
                    insertAllPersons(fetchedResult)
                }
            },
            shouldFetch = {
                checkInternetConnectivity(context)
            }
        )
    }
}