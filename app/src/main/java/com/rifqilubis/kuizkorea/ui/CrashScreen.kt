package com.rifqilubis.kuizkorea.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rifqilubis.kuizkorea.data.UiLang

/**
 * Ditampilkan saat aplikasi dibuka lagi setelah berhenti karena kesalahan.
 * Isinya bisa dipilih dan disalin supaya laporan bisa dikirim apa adanya.
 */
@Composable
fun CrashScreen(
    text: String,
    lang: UiLang,
    onDismiss: () -> Unit,
) {
    val clipboard = LocalClipboardManager.current
    val strings = crashStrings(lang)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(18.dp),
    ) {
        Text(
            text = strings.title,
            color = MaterialTheme.colorScheme.error,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = strings.hint,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp,
            lineHeight = 19.sp,
        )
        Spacer(Modifier.height(16.dp))

        SelectionContainer {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                    .padding(14.dp),
            )
        }

        Spacer(Modifier.height(18.dp))
        PrimaryButton(
            text = strings.copy,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("crash-copy"),
            onClick = { clipboard.setText(AnnotatedString(text)) },
        )
        Spacer(Modifier.height(10.dp))
        GhostButton(
            text = strings.dismiss,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("crash-dismiss"),
            onClick = onDismiss,
        )
        Spacer(Modifier.height(24.dp))
    }
}

/**
 * Teks untuk layar ini sengaja ditaruh di berkasnya sendiri: layar darurat
 * tidak boleh ikut berubah kalau teks antarmuka utama disunting.
 */
private data class CrashStrings(
    val title: String,
    val hint: String,
    val copy: String,
    val dismiss: String,
)

private val IndonesianCrash = CrashStrings(
    title = "Aplikasi berhenti karena kesalahan",
    hint = "Rincian di bawah tersimpan otomatis. Salin dan kirimkan ke pengembang supaya bisa diperbaiki.",
    copy = "Salin rincian",
    dismiss = "Tutup dan hapus",
)

private val EnglishCrash = CrashStrings(
    title = "The app stopped because of an error",
    hint = "The details below were saved automatically. Copy them and send them to the developer so this can be fixed.",
    copy = "Copy details",
    dismiss = "Close and delete",
)

private fun crashStrings(lang: UiLang): CrashStrings =
    if (lang == UiLang.EN) EnglishCrash else IndonesianCrash
