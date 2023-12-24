package com.example.savemypasswords.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.savemypasswords.NavItems
import com.example.savemypasswords.storage.AppStorage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class NotesList : Screen("notes_list", true, true) {
    @Composable
    override fun Draw(
        storage: AppStorage,
        navController: NavHostController,
        padding: PaddingValues,
        searchQuery: String,
        selected: SnapshotStateMap<Int, Unit>
    ) {
        val notes by (if (searchQuery.isNotEmpty()) storage.notesFiltered(searchQuery) else storage.notes).collectAsState(
            emptyList()
        )
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            modifier = Modifier.padding(padding)
        ) {
            items(notes) {note ->
                NoteItem(
                    header = note.header, text = note.content,
                    onClick = {
                        val noteData = Json.encodeToString(note)
                        navController.navigate(NavItems.EditNoteScreen.routeId + "?id=${note.id}&note=${noteData}")
                    },
                    onSelectUpdate = {
                        if (it) {
                            selected[note.id] = Unit
                        } else {
                            selected.remove(note.id)
                        }
                    },
                    selectable = selected.isNotEmpty(),
                    selected = selected.contains(note.id)
                )
            }
        }
    }

    @Composable
    override fun FloatingButton(storage: AppStorage, navController: NavHostController) {
        FloatingActionButton(onClick = { navController.navigate(NavItems.NewNoteScreen.routeId) }) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NoteItem(
    header: String, text: String,
    selectable: Boolean, selected: Boolean,
    onClick: () -> Unit, onSelectUpdate: (Boolean) -> Unit
) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
                .padding(10.dp)
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        if (selectable) {
                            onSelectUpdate(!selected)
                        } else {
                            onClick()
                        }
                    },
                    onLongClick = {
                        if (!selectable) {
                            onSelectUpdate(true)
                        }
                    }
                )
        ) {
            Text(
                text = header,
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.inverseSurface
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = text,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.inverseSurface
                ),
                maxLines = 5,
                overflow = TextOverflow.Ellipsis
            )
        }
        if (selectable) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 5.dp, bottom = 5.dp)
                    .size(30.dp)
                    .border(1.dp, color = Color.LightGray, CircleShape)
                    .clickable {
                        onSelectUpdate(!selected)
                    }
            ) {
                if (selected) {
                    Icon(Icons.Rounded.Check, "checked")
                }
            }
        }
    }
}

@Preview
@Composable
private fun NoteItemPreview() {
    NoteItem(
        header = "Test Header",
        text = "qkjln\nweijnd\njhnqw\nqwoiuhd\nqoiwjdio\nqwoiefj\nawepofgj\n",
        true,
        selected = false,
        onClick = {},
        onSelectUpdate = {}
    )
}