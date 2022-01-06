package com.example.incentivetimer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.incentivetimer.ui.theme.IncentiveTimerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IncentiveTimerTheme {

            }
        }
    }
}

@Composable
private fun ScreenContent() {
    val navController = rememberNavController()

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    IncentiveTimerTheme {

    }
}