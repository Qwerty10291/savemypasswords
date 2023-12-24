package com.example.savemypasswords

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.savemypasswords.storage.AppStorage
import com.example.savemypasswords.storage.models.dto.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewNoteScreen : NavScreen("new_note") {
    @Composable
    fun Draw(navController: NavController, viewModel: AppStorage) {
        val header = remember { mutableStateOf("") }
        val content = remember { mutableStateOf("") }
        val coroutine = rememberCoroutineScope()
        NoteEditForm(header = header, content = content, onSave = {
            coroutine.launch {
                with(Dispatchers.IO) {
                    viewModel.NewNote(header.value, content.value)
                }
                navController.popBackStack()
            }
        }, onBack = { navController.popBackStack() })
    }
}

class EditNoteScreen : NavScreen("edit_note") {
    @Composable
    fun Draw(navController: NavController, viewModel: AppStorage, note: Note) {
        val header = remember { mutableStateOf(note.header) }
        val content = remember { mutableStateOf(note.content) }
        val coroutine = rememberCoroutineScope()
        NoteEditForm(header = header, content = content, onSave = {
            coroutine.launch {
                with(Dispatchers.IO) {
                    viewModel.UpdateNote(Note(header.value, content.value, note.created, note.id))
                }
                navController.popBackStack()
            }
        }, onBack = { navController.popBackStack() })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NoteEditForm(
    header: MutableState<String>,
    content: MutableState<String>,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, "Назад")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Новая заметка", fontSize = 20.sp)
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onSave) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = null
                )
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            BasicTextField(
                modifier = Modifier.fillMaxWidth(),
                value = header.value,
                onValueChange = { header.value = it },
                textStyle = TextStyle(fontSize = 30.sp, color = MaterialTheme.colorScheme.inverseSurface),
                singleLine = true,
                cursorBrush = SolidColor(Color.White)
            )
            Spacer(modifier = Modifier.height(3.dp).fillMaxWidth().background(Color.White))
            BasicTextField(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
                value = content.value,
                onValueChange = { content.value = it },
                textStyle = TextStyle(fontSize = 20.sp, color = MaterialTheme.colorScheme.inverseSurface),
                cursorBrush = SolidColor(Color.White)
            )
        }
    }

}