package com.example.savemypasswords.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.savemypasswords.NavItems
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.savemypasswords.storage.AppStorage


class CardsList() : Screen("cards", true, true) {
    @Composable
    override fun Draw(
        storage: AppStorage,
        navController: NavHostController,
        padding: PaddingValues,
        searchQuery: String,
        selected: SnapshotStateMap<Int, Unit>
    ) {
        val scroll = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(scroll)
        ) {
            CreditCardItem(
                number = "1234123412341234",
                expiry = "03/30",
                cvv = "123",
                holder = "Card Holder"
            )
            Divider(
                Modifier
                    .height(1.dp)
                    .fillMaxWidth(), color = Color.LightGray
            )
            CreditCardItem(
                number = "1234123412341234",
                expiry = "03/30",
                cvv = "123",
                holder = "Card Holder"
            )
            Divider(
                Modifier
                    .height(1.dp)
                    .fillMaxWidth(), color = Color.LightGray
            )
            CreditCardItem(
                number = "1234123412341234",
                expiry = "03/30",
                cvv = "123",
                holder = "Card Holder"
            )
            Divider(
                Modifier
                    .height(1.dp)
                    .fillMaxWidth(), color = Color.LightGray
            )
            CreditCardItem(
                number = "1234123412341234",
                expiry = "03/30",
                cvv = "123",
                holder = "Card Holder"
            )
            Divider(
                Modifier
                    .height(1.dp)
                    .fillMaxWidth(), color = Color.LightGray
            )
            CreditCardItem(
                number = "1234123412341234",
                expiry = "03/30",
                cvv = "123",
                holder = "Card Holder"
            )
            Divider(
                Modifier
                    .height(1.dp)
                    .fillMaxWidth(), color = Color.LightGray
            )
            CreditCardItem(
                number = "1234123412341234",
                expiry = "03/30",
                cvv = "123",
                holder = "Card Holder"
            )
            Divider(
                Modifier
                    .height(1.dp)
                    .fillMaxWidth(), color = Color.LightGray
            )
            CreditCardItem(
                number = "1234123412341234",
                expiry = "03/30",
                cvv = "123",
                holder = "Card Holder"
            )
            Divider(
                Modifier
                    .height(1.dp)
                    .fillMaxWidth(), color = Color.LightGray
            )
            CreditCardItem(
                number = "1234123412341234",
                expiry = "03/30",
                cvv = "123",
                holder = "Card Holder"
            )
        }
    }

    @Composable
    override fun FloatingButton(storage: AppStorage, navController: NavHostController) {
        FloatingActionButton(onClick = { navController.navigate(NavItems.NewCardScreen.routeId) }) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
        }
    }
}

@Composable
fun CreditCardItem(number: String, expiry: String, cvv: String, holder: String) {
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
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = Modifier
            .padding(3.dp)
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()) {
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
            Box(modifier = Modifier
                .padding(3.dp)
                .clip(RoundedCornerShape(10.dp))
                .weight(0.48f)) {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(btnColor),
                    onClick = { clipboardManager.setText(AnnotatedString(number)) },
                ) {
                    Text(text = if (shown) expiry else "**/**")
                }
            }
            Spacer(modifier = Modifier.weight(0.04f))
            Box(modifier = Modifier
                .padding(3.dp)
                .clip(RoundedCornerShape(10.dp))
                .weight(0.48f)) {
                TextButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(btnColor),
                    onClick = { clipboardManager.setText(AnnotatedString(number)) },
                ) {
                    Text(text = if (shown) cvv else "***")
                }
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            Box(modifier = Modifier
                .padding(3.dp)
                .clip(RoundedCornerShape(10.dp))
                .weight(.8f)) {
                TextButton(
                    modifier = Modifier
                        .background(btnColor)
                        .fillMaxWidth(),
                    onClick = { clipboardManager.setText(AnnotatedString(number)) },
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
        holder = "Card Holder"
    )
}