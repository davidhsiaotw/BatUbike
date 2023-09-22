package com.example.batubike

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.batubike.ui.theme.BatUbikeTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var showDrawer by remember { mutableStateOf(false) }
            BatUbikeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { UbikeTopBar(onClickShowDrawer = { showDrawer = it }) }
                ) { contentPadding ->
                    StationSearchScreen(Modifier.padding(contentPadding))
                    UbikeTopBarDrawer(showDrawer)
                }
            }
        }
    }
}