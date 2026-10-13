package com.rifqilubis.kuizkorea.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

private val LocalDark = staticCompositionLocalOf { true }

private val DarkScheme = darkColorScheme(
    primary = Color(0xFF6C5CF6),
    onPrimary = Color(0xFFFFFFFF),
    background = Color(0xFF08080F),
    onBackground = Color(0xFFF2F3F8),
    surface = Color(0xFF16162E),
    onSurface = Color(0xFFF2F3F8),
    surfaceVariant = Color(0xFF1C1C38),
    onSurfaceVariant = Color(0xFF8F92AD),
    outline = Color(0xFF2B2B4D),
    error = Color(0xFFFF5C7A),
)

private val LightScheme = lightColorScheme(
    primary = Color(0xFF5B6CFF),
    onPrimary = Color(0xFFFFFFFF),
    background = Color(0xFFF4F5FB),
    onBackground = Color(0xFF14162E),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF14162E),
    surfaceVariant = Color(0xFFF6F7FC),
    onSurfaceVariant = Color(0xFF6C7090),
    outline = Color(0xFFE3E5F1),
    error = Color(0xFFD63A5C),
)

/** Warna tambahan yang tidak punya peran sendiri di Material 3. */
object KuizColors {
    val dark: Boolean
        @Composable get() = LocalDark.current

    val success: Color
        @Composable get() = if (LocalDark.current) Color(0xFF2EE08A) else Color(0xFF17915C)

    val successText: Color
        @Composable get() = if (LocalDark.current) Color(0xFF6FF0B0) else Color(0xFF17915C)

    val successSurface: Color
        @Composable get() = success.copy(alpha = 0.13f)

    val errorText: Color
        @Composable get() = if (LocalDark.current) Color(0xFFFFA9BB) else Color(0xFFD63A5C)

    val errorSurface: Color
        @Composable get() = MaterialTheme.colorScheme.error.copy(alpha = 0.13f)

    val roman: Color
        @Composable get() = if (LocalDark.current) Color(0xFF8FA7C9) else Color(0xFF5B6F96)

    val glow: Brush
        @Composable get() = if (LocalDark.current) {
            Brush.linearGradient(listOf(Color(0xFF12122A), Color(0xFF08080F)))
        } else {
            Brush.linearGradient(listOf(Color(0xFFECEDF9), Color(0xFFF4F5FB)))
        }
}

@Composable
fun KuizTheme(dark: Boolean, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalDark provides dark) {
        MaterialTheme(
            colorScheme = if (dark) DarkScheme else LightScheme,
            content = content,
        )
    }
}

/** Gradien aksen yang dipakai tombol utama dan bilah progres. */
val AccentBrush: Brush
    get() = Brush.linearGradient(listOf(Color(0xFF5B6CFF), Color(0xFF8B5CF6)))
