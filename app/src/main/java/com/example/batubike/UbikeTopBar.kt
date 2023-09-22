package com.example.batubike

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UbikeTopBar(showMenu: Boolean, onClickShowDrawer: (Boolean) -> Unit = {}) {

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
            onClickShowDrawer(!showMenu)
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


