package com.example.ecochef.screens

import android.content.Context.MODE_PRIVATE
import android.content.res.Resources.Theme
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecochef.R

@Composable
fun ProfileScreen(activity: ComponentActivity){
    // create list of preference option
    val radioOptions = listOf("No Preference","Vegetarian","Vegan","Pescetarian")
    // store selected option
    var (selectedOption, onOptionSelected) = remember { mutableStateOf(loadPrefSelection(activity))}
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_700))
            .wrapContentSize(Alignment.Center)
    ){
        Row {
        Text(
            text = "Profile Screen",
//            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontSize = 40.sp
        )
        }
        radioOptions.forEach { text ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedOption == text,
                        onClick = { onOptionSelected(text)
                            savePrefSelection(activity,text)
                        }
                    )
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text)
                              savePrefSelection(activity,text)
                    }
                )
                Text(
                    text = text,
                    style = TextStyle(
                        fontSize = 24.sp),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

private fun savePrefSelection(activity: ComponentActivity,selectedOption:String) {
    val sharedPref = activity.getSharedPreferences("myPref", MODE_PRIVATE)
    val editor =sharedPref.edit()
    editor.putString("selectedOption",selectedOption)
    editor.apply()
}

private fun loadPrefSelection(activity: ComponentActivity) : String {
    val sharedPref = activity.getSharedPreferences("myPref", MODE_PRIVATE)
    return sharedPref.getString("selectedOption", "No Preferences") ?: "No Preferences"
}