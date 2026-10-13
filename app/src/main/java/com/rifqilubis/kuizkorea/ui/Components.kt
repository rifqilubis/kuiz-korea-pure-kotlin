package com.rifqilubis.kuizkorea.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Kartu besar dengan sudut membulat, dipakai di semua layar. */
@Composable
fun KuizCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(22.dp))
            .padding(22.dp),
        content = content,
    )
}

/** Tombol utama bergradien. */
@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(AccentBrush)
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    }
}

/** Tombol sekunder bergaris. */
@Composable
fun GhostButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 24.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text, color = MaterialTheme.colorScheme.onBackground, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

/** Tombol bulat kecil untuk tema dan bahasa. */
@Composable
fun RoundIconButton(
    label: String,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
        )
        // contentDescription ditampilkan lewat label tombol; teks di atas sudah
        // cukup jelas untuk pembaca layar dalam konteks kuis ini.
    }
}

/** Bilah progres tipis. */
@Composable
fun KuizProgress(fraction: Float, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(MaterialTheme.colorScheme.outline),
    ) {
        val safe = fraction.coerceIn(0f, 1f)
        if (safe > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(safe.coerceAtLeast(0.02f))
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(3.dp))
                    .background(AccentBrush),
            )
        }
    }
}

/** Lencana hijau "Benar: n". */
@Composable
fun ScorePill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(KuizColors.successSurface)
            .border(1.dp, KuizColors.success.copy(alpha = 0.45f), RoundedCornerShape(999.dp))
            .padding(horizontal = 16.dp, vertical = 6.dp),
    ) {
        Text(
            text = text,
            color = KuizColors.success,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

/** Baris judul di atas semua layar. */
@Composable
fun TopBar(
    modeLabel: String,
    langCode: String,
    themeGlyph: String,
    strings: Strings,
    onTheme: () -> Unit,
    onLang: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = strings.brandKo,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = strings.brandQuiz,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 26.sp,
            fontWeight = FontWeight.ExtraBold,
            maxLines = 1,
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = modeLabel,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )

        Spacer(Modifier.width(10.dp))
        RoundIconButton(label = themeGlyph, contentDescription = strings.themeToggle, onClick = onTheme)
        Spacer(Modifier.width(8.dp))
        RoundIconButton(label = langCode, contentDescription = strings.langToggle, onClick = onLang)
    }
}

/** Baris "Soal n / total" dengan lencana skor. */
@Composable
fun StatusRow(counter: String, scoreText: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = counter,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
        )
        ScorePill(scoreText)
    }
}
