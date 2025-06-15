package com.target.targetcasestudy.core.ui.components

import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton // Import for IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic // Import for microphone icon
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.target.targetcasestudy.core.ui.theme.DarkRed
import com.target.targetcasestudy.core.ui.theme.Dimens
import com.target.targetcasestudy.core.ui.theme.PrimaryRed
import com.target.targetcasestudy.core.ui.theme.RobotoFontFamily
import java.util.Locale

/**
 * Reusable composable for a search input field with optional speech-to-text functionality.
 */
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Search",
    placeholder: String = "Enter search term",
    onSpeechResult: ((String) -> Unit)? = null // New callback for speech-to-text result
) {
    // Launcher for the speech recognition activity
    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val spokenText: String? = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.let { results ->
                    if (results.isNotEmpty()) results[0] else null
                }
            spokenText?.let {
                onSpeechResult?.invoke(it) // Invoke the callback with the recognized text
            }
        }
    }

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = {
            Text(
                label,
                style = TextStyle(
                    fontFamily = RobotoFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = Dimens.TextMedium
                )
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.PaddingMedium),
        placeholder = {
            if (query.isEmpty()) {
                Text(placeholder)
            }
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon",
                tint = DarkRed
            )
        },
        trailingIcon = {
            // Only show the microphone icon if onSpeechResult callback is provided
            onSpeechResult?.let {
                IconButton(onClick = {
                    // Create and launch the speech recognition intent
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toLanguageTag())
                        putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...") // User prompt
                    }
                    speechRecognizerLauncher.launch(intent)
                }) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Speech to text",
                        tint = DarkRed
                    )
                }
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.LightGray,
            unfocusedBorderColor = Color.LightGray,
            cursorColor = DarkRed,
            focusedLabelColor = PrimaryRed
        )
    )
}
