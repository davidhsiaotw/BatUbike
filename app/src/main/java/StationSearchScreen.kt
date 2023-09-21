import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.batubike.ui.theme.BatUbikeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StationSearchScreen(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        MySearchBar()
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
            Text(text = label, color = Color.Gray)
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
            Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
        },
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
        }),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Color.LightGray,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun MySearchBarPreview() {
    BatUbikeTheme {
        MySearchBar()
    }
}