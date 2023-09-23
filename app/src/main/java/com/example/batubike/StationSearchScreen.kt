package com.example.batubike

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
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
    var isSearchBarFocused by rememberSaveable { mutableStateOf(false) }
    var isSame by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        Column {
            Text(text = "")
            Text(
                text = "站點資訊", color = Color(171, 195, 62),
                fontSize = 18.sp, fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            MySearchBar(
                input, inputChange = {
                    input = it
                    isSearchBarFocused = true   // keep keyword list shown when input changes
                },
                isSame = isSame,
                onSearch = { filter = it },
                onFocusChange = { isSearchBarFocused = it })
            Spacer(modifier = Modifier.height(12.dp))
            Box {
                StationList(filter)
                RecommendKeywordList(
                    input = input, show = isSearchBarFocused,
                    onClick = {
                        input = it
                        isSame = false
                        isSearchBarFocused = false
                    }, onMatch = { isSame = it })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MySearchBar(
    input: String = "", inputChange: (String) -> Unit, onSearch: (String) -> Unit = {},
    isSame: Boolean = false, onFocusChange: (Boolean) -> Unit = {}
) {
    var label by rememberSaveable { mutableStateOf("搜尋站點") }
    // https://www.geeksforgeeks.org/how-to-clear-focus-of-textfield-in-android-using-jetpack-compose/
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val matchTextColor = if (isSame) Color(171, 195, 62) else Color.Black
    val matchIconColor = if (isSame) Color(171, 195, 62) else Color(164, 164, 164)

    OutlinedTextField(
        value = input,
        onValueChange = { //input = it
            inputChange(it)
        }, textStyle = TextStyle(color = matchTextColor),
        label = {
            Text(text = label, color = Color(164, 164, 164))
        },
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                onFocusChange(it.isFocused)
                if (it.isFocused) {
                    label = ""
                } else {
                    if (input.isEmpty())
                        coroutineScope.launch {
                            delay(150L) // wait for clearFocus() complete
                            label = "搜尋站點"
                        }
                }
            },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Search, contentDescription = "Search Icon",
                tint = matchIconColor
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecommendKeywordList(
    input: String, show: Boolean, onClick: (String) -> Unit = {}, onMatch: (Boolean) -> Unit = {}
) {
    if (show) {
        var defaultKeywords = taipeiAreas.filter {
            it.matches("^.*$input.*$".toRegex())
        }
        if (defaultKeywords.size > 6) defaultKeywords = defaultKeywords.slice(0..5)

        Spacer(modifier = Modifier.height(12.dp))
        Column(modifier = Modifier.clip(RoundedCornerShape(12.dp))) {
            defaultKeywords.forEach { area ->
                Card(
                    onClick = { onClick(area) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape,
                    colors = CardDefaults.cardColors(containerColor = Color(245, 245, 245))
                ) {
                    if (input.matches(area.toRegex())) {
                        Text(
                            text = area, modifier = Modifier.padding(12.dp),
                            color = Color(171, 195, 62), fontSize = 24.sp
                        )
                        onMatch(true)
                    } else {
                        Text(text = area, modifier = Modifier.padding(12.dp), fontSize = 24.sp)
                        onMatch(false)
                    }
                }
            }
        }
    }
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
                .background(Color(171, 195, 62)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val modifier = Modifier.padding(horizontal = 18.dp, vertical = 24.dp)
            TableCell(
                text = "  縣市 ", modifier = modifier, fontSize = 18.sp,
                textColor = Color.White
            )
            TableCell(
                text = " 區域 ", modifier = modifier, fontSize = 18.sp,
                textColor = Color.White
            )
            TableCell(
                text = "          站點名稱       ", modifier = modifier, fontSize = 18.sp,
                textColor = Color.White
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // https://stackoverflow.com/a/76685212/22598753
            items(
                count = stations.itemCount,
            ) { i ->
                val item = stations[i]
                val modifier = Modifier.padding(horizontal = 18.dp, vertical = 24.dp)
                item?.let {
                    if (i % 2 != 0) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .background(Color(245, 245, 245)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TableCell(text = "台北市", modifier = modifier)
                            TableCell(text = it.area, modifier = modifier)
                            TableCell(text = it.name, modifier = modifier)
                        }
                        isWhite = false
                    } else {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TableCell(text = "台北市", modifier = modifier)
                            TableCell(text = it.area, modifier = modifier)
                            TableCell(text = it.name, modifier = modifier)
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
    text: String, modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified, textColor: Color = Color.Unspecified
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text, fontSize = fontSize, modifier = modifier,
            color = textColor
        )
    }

}

private val taipeiAreas: List<String> = listOf(
    "松山區", "信義區", "大安區", "中山區", "中正區", "大同區", "萬華區", "文山區", "南港區",
    "內湖區", "士林區", "北投區"
)

@Preview(showBackground = true)
@Composable
private fun MySearchBarPreview() {
    BatUbikeTheme {
        var input by rememberSaveable { mutableStateOf("") }
        MySearchBar(input, { input = it })
    }
}

@Preview(showBackground = true)
@Composable
private fun TableCellsPreview() {
    BatUbikeTheme {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color(171, 195, 62)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val modifier = Modifier.padding(horizontal = 18.dp, vertical = 24.dp)
                TableCell(
                    text = "  縣市 ", modifier = modifier, fontSize = 18.sp,
                    textColor = Color.White
                )
                TableCell(
                    text = " 區域 ", modifier = modifier, fontSize = 18.sp,
                    textColor = Color.White
                )
                TableCell(
                    text = "          站點名稱       ", modifier = modifier, fontSize = 18.sp,
                    textColor = Color.White
                )
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(Color(245, 245, 245)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val modifier = Modifier.padding(horizontal = 18.dp, vertical = 24.dp)
                TableCell(text = "台北市", modifier)
                TableCell(text = "大安區", modifier)
                TableCell(text = "YouBike2.0_捷運黃金便便小樓站", modifier)
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun RecommendKeywordListPreview() {
    BatUbikeTheme {
        RecommendKeywordList("", true)
    }
}