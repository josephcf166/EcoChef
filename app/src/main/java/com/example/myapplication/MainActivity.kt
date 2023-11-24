package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.bottomnav.BottomNavigationBar
import com.example.myapplication.ui.theme.MyApplicationTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                        BottomNavigationBar()

                }
            }
        }
    }
}


@Composable
fun Greeting(name: Int, modifier: Modifier = Modifier) {
    Surface(color=Color.Cyan) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }
}


@Preview(showBackground = true, name= "My Preview", showSystemUi = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Log.d("test", "make1")
        //val response = response
        Log.d("test", "make1")
        //val jObject = response.body()!!.string()
        Log.d("test", "make")
        //Log.d("test", jObject)
    }
}