package com.example.ecochef.screens

import android.content.Context.MODE_PRIVATE
import androidx.activity.ComponentActivity
import androidx.compose.material3.Checkbox
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecochef.R


@Composable
fun ProfileScreen(activity: ComponentActivity, onRecipePage: MutableState<Boolean>){
    onRecipePage.value = false

    // create list of preference option
    val context = LocalContext.current
    val dietOptions = context.resources.getStringArray(R.array.diets).toList()
    val allergyOptions = context.resources.getStringArray(R.array.allergies).toList()
    // store selected option

    val (selectedOption, onOptionSelected) = remember { mutableStateOf(loadPrefSelection(activity))}

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 7.dp)
    ){
        Row(
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .align(Alignment.CenterHorizontally)
                .padding(8.dp)) {
            Text(
                text = "Preferences",
                fontSize = 40.sp,
                color= Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
        Divider(color = colorResource(R.color.black),
            thickness = 2.dp,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 5.dp))
        Row(
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = "Dietary Options:",
                fontSize = 20.sp,
                color=Color.Black,
                fontWeight = FontWeight.Bold
            )}
        Row(
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .align(Alignment.CenterHorizontally)
                .padding(4.dp)
        ) {
            Text(text = "(select one)",
                fontSize = 20.sp,
                modifier = Modifier.alpha(0.4f),
                color=Color.Black
            )
        }
        dietOptions.forEach { text ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selectedOption == text,
                        onClick = {
                            onOptionSelected(text)
                            savePrefSelection(activity, text)
                        }
                    )
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = { onOptionSelected(text)
                              savePrefSelection(activity,text)
                    },
                    colors = RadioButtonDefaults.colors(selectedColor = colorResource(id = R.color.spotify_green)),
                )
                Text(
                    text = text,
                    style = TextStyle(
                        fontSize = 24.sp),
                    modifier = Modifier.padding(start = 16.dp),
                    color=Color.Black
                )
            }
        }
        Divider(color = colorResource(R.color.black),
            thickness = 2.dp,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(bottom = 5.dp))
        Row(
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .align(Alignment.CenterHorizontally)
                .padding(1.dp)
        ) {
            Text(
                text = "Allergen Options:",
                fontSize = 20.sp,
                color=Color.Black,
                fontWeight = FontWeight.Bold
            )}
        Row(
            modifier = Modifier
                .wrapContentSize(Alignment.Center)
                .align(Alignment.CenterHorizontally)
                .padding(4.dp)
        ) {
            Text(text = "(select any)",
                fontSize = 20.sp,
                modifier = Modifier.alpha(0.4f),
                color=Color.Black
            )
        }
        allergyOptions.forEach { option : String ->
            val sharedAllergy = activity.getSharedPreferences("myAllergy", MODE_PRIVATE)
            var isChecked by remember { mutableStateOf(sharedAllergy.getBoolean(option,false)) }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = isChecked,
                        onClick = {
                            isChecked = isChecked.not()
                            saveAllergySelection(activity, option, isChecked)
                        }
                    )
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it
                        saveAllergySelection(activity,option,isChecked)},
                    colors = CheckboxDefaults.colors(
                        checkedColor = colorResource(id = R.color.spotify_green),

                        checkmarkColor = colorResource(id = R.color.ash_grey)

                    ),

                )
                Text(
                    text = option,
                    style = TextStyle(
                        fontSize = 24.sp),
                    modifier = Modifier.padding(start = 16.dp),
                    color=Color.Black
                )
            }
        }
    }
}

private fun saveAllergySelection(activity: ComponentActivity, name : String, checked: Boolean) {
    val sharedAllergy = activity.getSharedPreferences("myAllergy", MODE_PRIVATE)
    val editor = sharedAllergy.edit()
    editor.putBoolean(name,checked)
    editor.apply()
}

private fun savePrefSelection(activity : ComponentActivity,selectedOption : String) {
    val sharedPref = activity.getSharedPreferences("myPref", MODE_PRIVATE)
    val editor =sharedPref.edit()
    editor.putString("selectedOption",selectedOption)
    editor.apply()
}

private fun loadPrefSelection(activity: ComponentActivity) : String {
    val sharedPref = activity.getSharedPreferences("myPref", MODE_PRIVATE)
    return sharedPref.getString("selectedOption", "No Preference") ?: "No Preference"
}

private fun clearAllPreferences(activity: ComponentActivity) {
    val sharedPref = activity.getSharedPreferences("myPref", MODE_PRIVATE)
    with(sharedPref.edit()) {
        clear()
        apply()
    }
    val sharPref = activity.getSharedPreferences("myAllergy", MODE_PRIVATE)
    with(sharPref.edit()) {
        clear()
        apply()
    }
}
