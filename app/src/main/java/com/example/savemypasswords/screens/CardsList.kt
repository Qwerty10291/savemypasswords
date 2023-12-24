package com.example.savemypasswords.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.savemypasswords.NavItems
import com.example.savemypasswords.storage.AppStorage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class CardsList : Screen("cards", true, true) {
    @Composable
    override fun Draw(
        storage: AppStorage,
        navController: NavHostController,
        padding: PaddingValues,
        searchQuery: String,
        selected: SnapshotStateMap<Int, Unit>
    ) {
        val cards by (if (searchQuery.isNotEmpty()) storage.cardsFiltered(searchQuery) else storage.cards).collectAsState(
            emptyList()
        )
        LazyColumn(
            modifier = Modifier.padding(padding),
        ) {
            items(cards) { card ->
                CreditCardItem(
                    number = card.number,
                    expiry = card.expiry,
                    cvv = card.cvc,
                    holder = card.holder,
                    onClick = {
                        val cardData = Json.encodeToString(card)
                        navController.navigate(NavItems.EditCardScreen.routeId + "?id=${card.id}&card=${cardData}")
                    },
                    onSelectUpdate = {
                        if (it) {
                            selected[card.id] = Unit
                        } else {
                            selected.remove(card.id)
                        }
                    },
                    selectable = selected.isNotEmpty(),
                    selected = selected.contains(card.id)
                )
                Divider(
                    Modifier
                        .height(1.dp)
                        .fillMaxWidth(), color = Color.LightGray
                )
            }
        }
    }

    @Composable
    override fun FloatingButton(storage: AppStorage, navController: NavHostController) {
        FloatingActionButton(onClick = { navController.navigate(NavItems.NewCardScreen.routeId) }) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CreditCardItem(
    number: String, expiry: String, cvv: String, holder: String,
    selectable: Boolean, selected: Boolean,
    onClick: () -> Unit, onSelectUpdate: (Boolean) -> Unit
) {
    var shown by remember {
        mutableStateOf(false)
    }
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val btnColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
    Column(
        modifier = Modifier
            .height(180.dp)
            .clip(RoundedCornerShape(14.dp))
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color(255, 255, 255, if (selected) 32 else 0))
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
            ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .padding(3.dp)
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth()
        ) {
            TextButton(
                modifier = Modifier
                    .background(btnColor)
                    .fillMaxWidth(),
                onClick = { clipboardManager.setText(AnnotatedString(number)) }
            ) {
                Text(
                    text = if (shown) number else "*".repeat(number.length - 4) + number.slice(
                        number.length - 4..number.length - 1
                    )
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .padding(3.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .weight(0.48f)
            ) {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(btnColor),
                    onClick = { clipboardManager.setText(AnnotatedString(expiry)) },
                ) {
                    Text(text = if (shown) expiry else "**/**")
                }
            }
            Spacer(modifier = Modifier.weight(0.04f))
            Box(
                modifier = Modifier
                    .padding(3.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .weight(0.48f)
            ) {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(btnColor),
                    onClick = { clipboardManager.setText(AnnotatedString(cvv)) },
                ) {
                    Text(text = if (shown) cvv else "***")
                }
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .padding(3.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .weight(1f)
            ) {
                TextButton(
                    modifier = Modifier
                        .background(btnColor)
                        .fillMaxWidth(),
                    onClick = { clipboardManager.setText(AnnotatedString(holder)) },
                ) {
                    Text(text = holder)
                }
            }
            IconButton(onClick = { shown = !shown }) {
                Icon(
                    imageVector = if (shown) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = null
                )
            }
            if (selectable) {
                Box(contentAlignment = Alignment.Center,
                    modifier = Modifier
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

}

@Composable
@Preview(showBackground = true)
fun CreditCardFormPreview() {
    CreditCardItem(
        number = "1234123412341234",
        expiry = "03/30",
        cvv = "123",
        holder = "Card Holder",
        onClick = {},
        onSelectUpdate = {},
        selected = false,
        selectable = true
    )
}