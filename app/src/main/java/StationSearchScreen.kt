import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.example.batubike.ui.theme.BatUbikeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StationSearchScreen(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            Text(text = "")
            Text(
                text = "站點資訊",
                color = Color(171, 195, 62),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            MySearchBar()
            Spacer(modifier = Modifier.height(12.dp))
            StationList()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MySearchBar() {
    var input by rememberSaveable { mutableStateOf("") }
    var label by rememberSaveable { mutableStateOf("搜尋站點") }
    // https://www.geeksforgeeks.org/how-to-clear-focus-of-textfield-in-android-using-jetpack-compose/
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()

    OutlinedTextField(
        value = input,
        onValueChange = { input = it },
        label = {
            Text(text = label, color = Color(164, 164, 164))
        },
        modifier = Modifier.onFocusChanged {
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
private fun StationList() {
    LazyColumn(modifier = Modifier.clip(RoundedCornerShape(12.dp))) {

    }
}

@Preview(showBackground = true)
@Composable
private fun MySearchBarPreview() {
    BatUbikeTheme {
        MySearchBar()
    }
}