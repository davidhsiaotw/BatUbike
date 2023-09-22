package com.example.batubike

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.batubike.viewmodel.MenuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UbikeTopBar(onClickShowDrawer: (Boolean) -> Unit = {}) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(title = {
        Row(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = "https://upload.wikimedia.org/wikipedia/zh/thumb/5/51/YouBike.svg/1920px-YouBike.svg.png",
                contentDescription = "Ubike Logo", modifier = Modifier
                    .size(140.dp, 50.dp)
                    .padding(6.dp)
            )
        }
    }, modifier = Modifier.shadow(3.dp), actions = {
        IconButton(onClick = {
            showMenu = !showMenu
            onClickShowDrawer(showMenu)
        }, modifier = Modifier.padding(24.dp)) {
            if (!showMenu)
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = Color(171, 195, 62)
                )
            else {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color(171, 195, 62)
                )
            }
        }
    })
}

@Composable
fun UbikeTopBarDrawer(show: Boolean) {
    if (show) {
        val viewModel = MenuViewModel()
        var selectedId by rememberSaveable { mutableStateOf(2) }
        val menuList by viewModel.menuState.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(171, 195, 62))
        ) {
            Spacer(modifier = Modifier.height(56.dp))
            LazyColumn {
                items(menuList) { menu ->
                    ClickableText(
                        text = AnnotatedString(menu.text),
                        Modifier.padding(24.dp),
                        style = TextStyle(
                            color = menu.color,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        onClick = {
                            viewModel.onClickUpdate(selectedId, menu.id)
                            selectedId = menu.id
                        })
                }
            }

        }
    }
}

data class MenuUiState(
    val id: Int,
    var text: String,
    var color: Color = Color.Black,
)

@Preview(showBackground = true)
@Composable
private fun UbikeTopBarDrawerPreview() {
    MaterialTheme {
        UbikeTopBarDrawer(true)
    }
}


