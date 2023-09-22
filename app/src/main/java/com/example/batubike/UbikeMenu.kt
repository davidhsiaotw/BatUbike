package com.example.batubike

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.batubike.viewmodel.MenuViewModel

@Composable
fun UbikeMenu(viewModel: MenuViewModel, show: Boolean, onClickShowDrawer: () -> Unit) {
    var selectedId by rememberSaveable { mutableStateOf(2) }
    val menuList by viewModel.menuState.collectAsState()
    if (show) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(171, 195, 62))
        ) {
            Spacer(modifier = Modifier.height(56.dp))
            LazyColumn {
                items(menuList) { menu ->
                    ClickableText(
                        text = AnnotatedString(menu.text), modifier = Modifier.padding(24.dp),
                        style = TextStyle(
                            color = menu.color, fontSize = 24.sp, fontWeight = FontWeight.Bold
                        ),
                        onClick = {
                            viewModel.onClickUpdate(selectedId, menu.id)
                            selectedId = menu.id
                            onClickShowDrawer()
                        })
                }
            }
        }
    }
}

data class MenuUiState(
    val id: Int,
    var text: String,
    var color: Color = Color.White,
)

@Preview(showBackground = true)
@Composable
private fun UbikeMenuPreview() {
    MaterialTheme {
        UbikeMenu(MenuViewModel(), true) {}
    }
}