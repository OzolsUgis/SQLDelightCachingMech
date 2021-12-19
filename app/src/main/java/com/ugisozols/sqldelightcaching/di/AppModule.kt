package com.ugisozols.sqldelightcaching.di

import android.app.Application
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.ugisozols.sqldelightcaching.data.local.PersonsDataSource
import com.ugisozols.sqldelightcaching.data.local.PersonsDataSourceImpl
import com.ugisozols.sqldelightpersons.PersonsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSQLDriver(app : Application) : SqlDriver {
        return AndroidSqliteDriver(
            PersonsDatabase.Schema,
            context = app,
            name = "persons.db"
        )
    }

    @Provides
    @Singleton
    fun providePersonsDataSource(driver: SqlDriver) : PersonsDataSource{
        return  PersonsDataSourceImpl(PersonsDatabase(driver))
    }
}