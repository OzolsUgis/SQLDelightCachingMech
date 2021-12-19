package com.ugisozols.sqldelightcaching.presentation.main_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.ugisozols.sqldelightcaching.utilities.SynchronizationEventHandler
import com.ugisozols.sqldelightcachingmechanism.presentation.main_screen.MainScreenViewModel
import com.ugisozols.sqldelightcaching.utilities.DataResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import personsdatabase.personsdb.Persons

@ExperimentalCoroutinesApi
@Composable
fun MainScreen(
    scaffoldState: ScaffoldState,
    viewModel: MainScreenViewModel = hiltViewModel()

) {

    val content = viewModel.allPersons.collectAsState(
        initial = SynchronizationEventHandler(
            DataResource.Loading(
                emptyList()
            )
        )
    )


    val listOfPersons = content.value.getContent()


    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "Add new friend") },
                onClick = { viewModel.onDialogIsOpenChange(true) },
                icon = {
                    Icon(imageVector = Icons.Filled.Add, "Add new friend")
                }
            )
        }
    ) {

        if (viewModel.showErrorSnackbar && viewModel.errorState != "") {
            LaunchedEffect(key1 = true) {
                scaffoldState.snackbarHostState.showSnackbar(
                    viewModel.errorState,
                    duration = SnackbarDuration.Short
                )
            }
        }
        if (viewModel.dialogIsOpen) {
            ShowAlertDialog(
                onDismissRequest = { viewModel.onDialogIsOpenChange(false) },
                viewModel
            )
        }
        listOfPersons.let { result ->
            when (result) {
                is DataResource.Error -> {
                    content.value.getIfNotSynchronized()?.let { error ->
                        error.message?.let {
                            viewModel.onErrorStateChange(it)
                            viewModel.onShowErrorSnackbarChange(true)
                        }
                    }
                    ShowList(list = result.data!!, viewModel = viewModel)
                }
                is DataResource.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(100.dp),
                            color = Color.Blue,
                            strokeWidth = 4.dp
                        )
                    }
                }
                is DataResource.Success -> {
                    content.value.getContent().let {
                        ShowList(list = it.data!!, viewModel = viewModel)
                    }
                }
            }
        }
    }
}


@Composable
fun ShowAlertDialog(
    onDismissRequest: () -> Unit,
    viewModel: MainScreenViewModel
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        text = {
            Column {
                TextField(
                    value = viewModel.firstName,
                    onValueChange = {
                        viewModel.onFirstNameChange(it)
                    },
                    placeholder = {
                        Text(text = "First name")
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = viewModel.lastName,
                    onValueChange = { newName ->
                        viewModel.onLastNameChange(newName)
                    },
                    placeholder = {
                        Text(text = "Last name")
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = viewModel.phoneNumber,
                    onValueChange = { newPhoneNumber ->
                        viewModel.onPhoneNumberChange(newPhoneNumber)
                    },
                    placeholder = {
                        Text(text = "Phone number")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = {
                    viewModel.onInsertFriendClick()
                }
                ) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = "Add new")
                }
            }
        }
    )
}


@Composable
fun FriendListItem(
    persons: Persons,
    onFriendClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    var isSyncedColor by remember {
        mutableStateOf(Color.Red)
    }
    isSyncedColor = if (persons.isSynced) {
        Color.Green
    } else {
        Color.Red
    }
    Row(
        modifier = modifier
            .clickable { onFriendClick() }
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Text(text = persons.firstName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = persons.lastName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Done,
                "IsSynced",
                Modifier.size(30.dp),
                tint = isSyncedColor
            )
            IconButton(onClick = { onDeleteClick() }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete friend ")

            }
        }


    }
}

@Composable
fun ShowList(list: List<Persons>, viewModel: MainScreenViewModel) {
    Box{
        LazyColumn(Modifier.fillMaxSize()) {
            items(list) { person ->
                FriendListItem(
                    modifier = Modifier.padding(
                        start = 16.dp,
                        top = 8.dp,
                        end = 16.dp,
                        bottom = 8.dp
                    ),
                    persons = person,
                    onFriendClick = {
                        viewModel.getFriendById(person.id)
                    },
                    onDeleteClick = {
                        viewModel.deleteFriend(person.id)
                    }
                )
            }
        }
        viewModel.person?.let { person ->
            Dialog(onDismissRequest = viewModel::onFriendDialogDismissClick) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "${person.firstName} ${person.lastName} - ${person.phoneNumber}")
                }
            }
        }
    }
}
