package com.ugisozols.sqldelightcachingmechanism.presentation.main_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ugisozols.sqldelightcaching.domain.repository.PersonsRepository
import com.ugisozols.sqldelightcaching.utilities.SynchronizationEventHandler
import com.ugisozols.sqldelightcachingmechanism.util.DataResource

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import personsdatabase.personsdb.Persons
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val repository: PersonsRepository
) : ViewModel() {



    var firstName by mutableStateOf("")
        private set


    var lastName by mutableStateOf("")
        private set

    var phoneNumber by mutableStateOf("")
        private set

    fun onFirstNameChange(name: String) {
        firstName = name
    }
    var errorState by mutableStateOf("")
    private set


    var dialogIsOpen by mutableStateOf(false)
    private set

    fun onErrorStateChange(text : String){
        errorState = text
    }

    var showErrorSnackbar by mutableStateOf(false)
    private set

    fun onShowErrorSnackbarChange(snackbarState : Boolean){
        showErrorSnackbar = snackbarState
    }

    fun onDialogIsOpenChange(state : Boolean){
        dialogIsOpen = state
    }



    fun onLastNameChange(name: String) {
        lastName = name
    }

    fun onPhoneNumberChange(number: String) {
        phoneNumber = number
    }

    fun onInsertFriendClick() {
        if (firstName.isBlank() || lastName.isBlank() || phoneNumber.isBlank()) {
            return
        }
        viewModelScope.launch {
            repository.insertPerson(
                Persons(
                    firstName,
                    lastName,
                    phoneNumber,
                    isSynced = true,
                    id = UUID.randomUUID().toString()
                )
            )
            firstName = ""
            lastName = ""
            phoneNumber = ""
        }
    }


    @ExperimentalCoroutinesApi
    private val _allPersons= repository.getAllPersons().map {
        SynchronizationEventHandler(it)
    }

    @ExperimentalCoroutinesApi
    val allPersons: Flow<SynchronizationEventHandler<DataResource<List<Persons>>>> = _allPersons


    var person by mutableStateOf<Persons?>(null)
        private set

    fun getFriendById(id: String) {
        viewModelScope.launch {
            person = repository.getPersonById(id)
        }
    }

    fun onFriendDialogDismissClick() {
        person = null
    }

    fun deleteFriend(personId: String) = viewModelScope.launch {
        repository.deletePerson(personId = personId)
    }


}