package com.example.cenitapp.views

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@Composable
fun OnBoardingScreen (navController: NavController){

}

@Composable
fun ContentOnBoardingScreen(){

}

@Preview(showBackground = true)
@Composable
fun OnBoardingScreenPreview() {
    OnBoardingScreen(navController = NavController(LocalContext.current))
}