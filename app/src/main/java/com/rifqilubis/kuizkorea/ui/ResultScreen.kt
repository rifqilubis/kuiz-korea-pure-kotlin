package com.rifqilubis.kuizkorea.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rifqilubis.kuizkorea.data.Answer

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    answers: List<Answer>,
    total: Int,
    elapsedMs: Long,
    isNewBest: Boolean,
    strings: Strings,
    onPlayAgain: () -> Unit,
    onSettings: () -> Unit,
) {
    val score = answers.count { it.isCorrect }
    val wrong = answers.filter { !it.isCorrect }
    val percent = if (total == 0) 0 else Math.round(score * 100f / total)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        KuizCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "$percent%",
                color = when {
                    percent >= 70 -> KuizColors.success
                    percent >= 40 -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.error
                },
                fontSize = 46.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                text = "$score / $total",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.height(18.dp))
            Text(
                text = strings.resultTitle,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = message(percent, strings),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 14.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            if (isNewBest) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = strings.newBest,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(Modifier.height(22.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                StatBox(strings.statCorrect, score.toString(), KuizColors.success, Modifier.weight(1f))
                StatBox(strings.statWrong, (total - score).toString(), MaterialTheme.colorScheme.error, Modifier.weight(1f))
                StatBox(strings.statTime, formatTime(elapsedMs), MaterialTheme.colorScheme.onSurface, Modifier.weight(1f))
            }

            Spacer(Modifier.height(24.dp))
            PrimaryButton(text = strings.playAgain, modifier = Modifier.fillMaxWidth(), onClick = onPlayAgain)
            Spacer(Modifier.height(10.dp))
            GhostButton(text = strings.changeSettings, modifier = Modifier.fillMaxWidth(), onClick = onSettings)
        }

        if (wrong.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            KuizCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = strings.reviewTitle,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(Modifier.height(14.dp))
                wrong.forEachIndexed { i, answer ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(14.dp),
                    ) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = answer.question.word.ko,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = answer.question.word.rom,
                                color = KuizColors.roman,
                                fontSize = 13.sp,
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "${strings.yourAnswer}: ${answer.chosenText}",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                        )
                        Text(
                            text = "${strings.correctAnswer}: ${answer.question.correctText}",
                            color = KuizColors.success,
                            fontSize = 13.sp,
                            lineHeight = 19.sp,
                        )
                    }
                    if (i != wrong.lastIndex) Spacer(Modifier.height(12.dp))
                }
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun StatBox(label: String, value: String, valueColor: androidx.compose.ui.graphics.Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = value, color = valueColor, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(text = label.uppercase(), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 10.sp, letterSpacing = 1.sp)
    }
}

private fun message(percent: Int, strings: Strings): String = when {
    percent >= 90 -> strings.msgGreat
    percent >= 70 -> strings.msgGood
    percent >= 50 -> strings.msgOk
    else -> strings.msgBad
}

/** Milidetik menjadi "m:ss". */
private fun formatTime(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    return "%d:%02d".format(totalSeconds / 60, totalSeconds % 60)
}
