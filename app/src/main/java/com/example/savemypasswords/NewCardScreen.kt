package com.example.savemypasswords

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.savemypasswords.storage.AppStorage
import com.example.savemypasswords.storage.models.dto.Card
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewCardScreen : NavScreen("new_card") {

    @Composable
    fun Draw(navController: NavController, viewModel: AppStorage) {
        val cardNumber = remember { mutableStateOf("") }
        val expiryDate = remember { mutableStateOf("") }
        val cvv = remember { mutableStateOf("") }
        val cardHolder = remember { mutableStateOf("") }
        val isCvvVisible = remember { mutableStateOf(false) }
        val coroutine = rememberCoroutineScope()
        CardEditForm(
            number = cardNumber,
            expiryDate = expiryDate,
            cvv = cvv,
            cardHolder = cardHolder,
            isCvvVisible = isCvvVisible,
            onSave = {
                coroutine.launch {
                    with(Dispatchers.IO) {
                        viewModel.NewCard(
                            cardNumber.value,
                            expiryDate.value,
                            cvv.value,
                            cardHolder.value
                        )
                    }
                    navController.popBackStack()
                }
            },
            onBack = { navController.popBackStack() })
    }
}

class EditCardScreen : NavScreen("update_card") {
    @Composable
    fun Draw(navController: NavController, viewModel: AppStorage, card: Card) {
        val cardNumber = remember { mutableStateOf(card.number) }
        val expiryDate = remember { mutableStateOf(card.expiry) }
        val cvv = remember { mutableStateOf(card.cvc) }
        val cardHolder = remember { mutableStateOf(card.holder) }
        val isCvvVisible = remember { mutableStateOf(false) }
        val coroutine = rememberCoroutineScope()
        CardEditForm(
            number = cardNumber,
            expiryDate = expiryDate,
            cvv = cvv,
            cardHolder = cardHolder,
            isCvvVisible = isCvvVisible,
            onSave = {
                coroutine.launch {
                    with(Dispatchers.IO) {
                        viewModel.UpdateCard(
                            Card(
                                cardNumber.value,
                                expiryDate.value,
                                cvv.value,
                                cardHolder.value,
                                id = card.id
                            )
                        )
                    }
                    navController.popBackStack()
                }
            },
            onBack = { navController.popBackStack() })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardEditForm(
    number: MutableState<String>,
    expiryDate: MutableState<String>,
    cvv: MutableState<String>,
    cardHolder: MutableState<String>,
    isCvvVisible: MutableState<Boolean>,
    onSave: () -> Unit, onBack: () -> Unit
) {
    var isNumberError by remember {
        mutableStateOf(false)
    }
    var isExpiryError by remember {
        mutableStateOf(false)
    }
    var isCvvError by remember {
        mutableStateOf(false)
    }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                isNumberError = number.value.length < 16
                isExpiryError = expiryDate.value.length != 4
                isCvvError = cvv.value.length != 3
                if (!(isExpiryError || isNumberError || isCvvError)) {
                    onSave()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = null
                )
            }
        },
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.primaryContainer
                    )
                    .fillMaxWidth()
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, "Назад")
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Новая карта", fontSize = 20.sp)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = number.value,
                onValueChange = { number.value = it },
                label = { Text(text = "number") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                isError = isNumberError
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                OutlinedTextField(
                    value = expiryDate.value,
                    onValueChange = { expiryDate.value = it },
                    label = { Text(text = "mm/yy") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    isError = isExpiryError
                )

                OutlinedTextField(
                    value = cvv.value,
                    onValueChange = { cvv.value = it },
                    label = { Text(text = "cvv") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { isCvvVisible.value = !isCvvVisible.value }) {
                            Icon(
                                imageVector = if (isCvvVisible.value) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = null
                            )
                        }
                    },
                    visualTransformation = if (isCvvVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier
                        .weight(1f),
                    isError = isCvvError
                )
            }

            OutlinedTextField(
                value = cardHolder.value,
                onValueChange = { cardHolder.value = it },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            )
        }
    }
}