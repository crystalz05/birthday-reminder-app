package com.tyro.birthdayreminder.ui.screen

import com.tyro.birthdayreminder.R
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.tyro.birthdayreminder.ui.screen.scaffold_contents.ScaffoldAction
import com.tyro.birthdayreminder.ui.screen.scaffold_contents.ScaffoldNavigation
import com.tyro.birthdayreminder.ui.screen.scaffold_contents.ScaffoldTitle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactScreen() {

    val context = LocalContext.current

    var contacts by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }
    var searchKeyword by remember { mutableStateOf("") }

    val groupedContacts = contacts
        .filter { it.first.contains(searchKeyword, ignoreCase = true) }
        .sortedBy { it.first }
        .groupBy { it.first.first().uppercaseChar() }

    Column(modifier = Modifier) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LaunchedEffect(Unit) {
                contacts = getContacts(context)
            }
            LazyColumn() {
                item {
                    TextField(modifier = Modifier.fillMaxWidth().padding(24.dp),
                        leadingIcon = { Icon(painter = painterResource(id = R.drawable.baseline_search_24), contentDescription = "search", tint = MaterialTheme.colorScheme.primary) },
                        value = searchKeyword, onValueChange = {searchKeyword = it},
                        label = { Text("Search Contact", color = MaterialTheme.colorScheme.onSurface.copy(0.2f)) },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Gray,
                            unfocusedIndicatorColor = Color.LightGray,
                            disabledIndicatorColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.LightGray.copy(0.2f)
                        ),
                        shape = RoundedCornerShape(0.dp)
                    )
                }
                groupedContacts.forEach{ (letter, contactsInGroup) ->
                    item {
                        Column(Modifier.background(color = Color.Gray.copy(0.5f)).padding(horizontal = 24.dp).fillMaxWidth()) {
                            Text(letter.toString(), style = MaterialTheme.typography.titleMedium)
                        }
                    }
                    items(contactsInGroup) { (name, number) ->
                        Column(Modifier.padding(horizontal = 24.dp, vertical = 12.dp)) {
                            Text(name)
                            Text(number, color = MaterialTheme.colorScheme.onSurface.copy(0.5f))
                        }
                        HorizontalDivider(Modifier.padding(horizontal = 16.dp))
                    }
                }
            }
        } else {
            ContactPermissionRequester(
                onPermissionGranted = {
                    contacts = getContacts(context)
                },
                onPermissionDenied = {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

}

fun getContacts(context: Context): List<Pair<String, String>>{
    val contacts = mutableListOf<Pair<String, String>>()
    val resolver = context.contentResolver
    val cursor = resolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER),
        null, null,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
    )

    cursor?.use {
        val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

        while(it.moveToNext()){
            val name = it.getString(nameIndex)
            val number = it.getString(numberIndex)
            contacts.add(name to number)
        }
    }
    return contacts
}

@Composable
fun ContactPermissionRequester(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }
    Button(onClick = {
        launcher.launch(Manifest.permission.READ_CONTACTS)
    }) {
        Text("Request Contacts Permission")
    }
}