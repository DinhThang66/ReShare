package com.example.reshare.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val DarkPurple = Color(0xff3b1a69)
val LightPurple = Color(0xffeaeaf4)
val LightGray = Color(0xFFF5F5F5)

val DarkGreen = Color(0xff077242)
val LightGreen = Color(0xffdfeee7)

val BlueGray = Color(0xFF334155)
val LightBlueWhile = Color(0xFFF1F5F9)
val DarkBlue = Color(0xFF1E293B)

val BlueD = Color(0xff25154b)
val YellowD = Color(0xffffdc01)
val OrangeM = Color(0xffee5206)
val MilkM = Color(0xfffcfce4)
val DarkYellow = Color(0xffeac129)

val ColorScheme.focusedTextFieldText
    @Composable
    get() = if (isSystemInDarkTheme()) Color.White else Color.Black

val ColorScheme.unfocusedTextFieldText
    @Composable
    get() = if (isSystemInDarkTheme())
        Color(0xFF94A3B8) else Color(0xFF475569)

val ColorScheme.textFieldTextContainer
    @Composable
    get() = if (isSystemInDarkTheme()) BlueGray.copy(0.6f) else LightBlueWhile