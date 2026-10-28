package com.rifqilubis.kuizkorea.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rifqilubis.kuizkorea.data.Direction
import com.rifqilubis.kuizkorea.data.VERBS_ADJECTIVES
import com.rifqilubis.kuizkorea.data.WORDS

/**
 * Layar pengaturan.
 *
 * Layar inilah yang menggulung isinya sendiri; Column di MainActivity sengaja
 * tidak bisa di-scroll, karena dua komponen yang bisa di-scroll vertikal secara
 * bersarang membuat Compose melempar pengecualian saat diukur.
 */
@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    strings: Strings,
    direction: Direction,
    count: Int,
    best: Int?,
    onDirection: (Direction) -> Unit,
    onCount: (Int) -> Unit,
    onStart: () -> Unit,
    onStartVerbs: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        KuizCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = strings.startTitle,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = strings.startSub.format(WORDS.size),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                lineHeight = 21.sp,
            )

            Spacer(Modifier.height(24.dp))
            SectionLabel(strings.directionLabel)
            Spacer(Modifier.height(10.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                    .padding(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Segment("Korea → Indonesia", direction == Direction.KO_ID) { onDirection(Direction.KO_ID) }
                Segment("Indonesia → Korea", direction == Direction.ID_KO) { onDirection(Direction.ID_KO) }
                Segment(strings.modeMixed, direction == Direction.MIXED) { onDirection(Direction.MIXED) }
            }

            Spacer(Modifier.height(24.dp))
            SectionLabel(strings.countLabel)
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Chip("10", count == 10) { onCount(10) }
                Chip("20", count == 20) { onCount(20) }
                Chip("50", count == 50) { onCount(50) }
            }
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Chip("100", count == 100) { onCount(100) }
                Chip("200", count == 200) { onCount(200) }
                Chip(strings.countAll, count == 0) { onCount(0) }
            }

            Spacer(Modifier.height(26.dp))
            PrimaryButton(
                text = strings.start,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("start"),
                onClick = onStart,
            )

            // Mode khusus: hanya kata kerja dan kata sifat, tanpa kata benda.
            Spacer(Modifier.height(10.dp))
            GhostButton(
                text = strings.verbAdjStart.format(VERBS_ADJECTIVES.size),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("start-verbs"),
                onClick = onStartVerbs,
            )

            Spacer(Modifier.height(18.dp))
            Text(
                text = best?.let { strings.bestLine.format(it) } ?: strings.bestNone,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = strings.hint,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text.uppercase(),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 11.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.6.sp,
    )
}

@Composable
private fun Segment(text: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (selected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 13.dp, horizontal = 14.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = text,
            color = if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun Chip(text: String, selected: Boolean, onClick: () -> Unit) {
    val border = if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
    else MaterialTheme.colorScheme.outline
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(
                if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)
                else MaterialTheme.colorScheme.surfaceVariant
            )
            .border(1.dp, border, RoundedCornerShape(999.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 10.dp),
    ) {
        Text(
            text = text,
            color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
