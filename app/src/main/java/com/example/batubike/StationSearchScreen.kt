package com.example.batubike

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.batubike.ui.theme.BatUbikeTheme
import com.example.batubike.viewmodel.StationViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StationSearchScreen(modifier: Modifier) {
    var input by rememberSaveable { mutableStateOf("") }
    var filter by rememberSaveable { mutableStateOf("") }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp), contentAlignment = Alignment.Center
    ) {
        Column {
            Text(text = "")
            Text(
                text = "站點資訊",
                color = Color(171, 195, 62),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            MySearchBar(input, { input = it }, { filter = it })
            Spacer(modifier = Modifier.height(12.dp))
            StationList(filter)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MySearchBar(
    input: String = "", inputChange: (String) -> Unit, onSearch: (String) -> Unit = {}
) {
    var label by rememberSaveable { mutableStateOf("搜尋站點") }
    // https://www.geeksforgeeks.org/how-to-clear-focus-of-textfield-in-android-using-jetpack-compose/
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    OutlinedTextField(
        value = input,
        onValueChange = { //input = it
            inputChange(it)
        },
        label = {
            Text(text = label, color = Color(164, 164, 164))
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                if (it.isFocused) label = ""
                else {
                    if (input.isEmpty())
                        coroutineScope.launch {
                            delay(150L) // wait for clearFocus() complete
                            label = "搜尋站點"
                        }
                }
            },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color(164, 164, 164)
            )
        },
        keyboardActions = KeyboardActions(onDone = {
            onSearch(input)
            focusManager.clearFocus()
        }),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color(245, 245, 245),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun StationList(input: String) {
    val viewModel = StationViewModel()
    val stations = viewModel.getStations(input).collectAsLazyPagingItems()
    var isWhite by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color(171, 195, 62))
        ) {
            TableCell(text = "縣市", textColor = Color.White)
            TableCell(text = "區域", textColor = Color.White)
            TableCell(text = "站點名稱", textColor = Color.White)
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // https://stackoverflow.com/a/76685212/22598753
            items(
                count = stations.itemCount,
            ) { i ->
                val item = stations[i]
                item?.let {
                    if (i % 2 != 0) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .background(Color(164, 164, 164))
                        ) {
                            TableCell(text = "台北市")
                            TableCell(text = it.area)
                            TableCell(text = it.name)
                        }
                        isWhite = false
                    } else {
                        Row(
                            Modifier.fillMaxWidth()
                        ) {
                            TableCell(text = "台北市")
                            TableCell(text = it.area)
                            TableCell(text = it.name)
                        }
                        isWhite = true
                    }


                }

            }
        }
    }
}

@Composable
private fun TableCell(
    text: String,
    textColor: Color = Color.Black,
) {
    Text(text = text, modifier = Modifier.padding(12.dp), color = textColor)
}

@Preview(showBackground = true)
@Composable
private fun MySearchBarPreview() {
    BatUbikeTheme {
        var input by rememberSaveable { mutableStateOf("") }
        MySearchBar(input, { input = it })
    }
}