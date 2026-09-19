package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = SunnyYellow,
  onPrimary = KidOnSurface,
  secondary = SkyBlue,
  tertiary = BubblePink,
  background = Color(0xFF1E2328),
  surface = Color(0xFF282E33),
  onBackground = Color(0xFFFFFDE7),
  onSurface = Color(0xFFFFFDE7)
)

private val LightColorScheme = lightColorScheme(
  primary = SunnyYellowDark,
  onPrimary = Color.White,
  primaryContainer = SunnyYellowSoft,
  onPrimaryContainer = Color(0xFF5D4037),
  secondary = SkyBlue,
  onSecondary = Color.White,
  secondaryContainer = SkyBlueLight,
  onSecondaryContainer = SkyBlueDark,
  tertiary = BubblePink,
  onTertiary = Color.White,
  tertiaryContainer = BubblePinkLight,
  background = KidBackgroundLight,
  surface = KidSurfaceLight,
  onBackground = KidOnSurface,
  onSurface = KidOnSurface
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Use our cheerful kids palette consistently
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
