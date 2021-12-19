package com.ugisozols.sqldelightcaching.data.remote

import com.ugisozols.sqldelightcaching.data.remote.request.DeletePerson
import com.ugisozols.sqldelightcaching.data.remote.response.MainResponse
import okhttp3.ResponseBody
import personsdatabase.personsdb.Persons
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface PersonsApi {
    @POST("/person/add")
    suspend fun insertPerson(
        @Body person : Persons
    ) : Response<MainResponse>

    @POST("/person/delete")
    suspend fun deletePerson(
        @Body deleteRequest: DeletePerson
    ): Response<ResponseBody>

    @GET("/person/getPersons")
    suspend fun getPersons() : Response<List<Persons>>

    companion object{
        // This is url for testing on Android Emulator
        const val BASE_URL = "http://10.0.2.2:8081"
    }
}