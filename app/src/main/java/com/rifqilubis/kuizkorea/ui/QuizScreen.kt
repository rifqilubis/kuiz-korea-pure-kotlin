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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rifqilubis.kuizkorea.data.Question

private val LETTERS = listOf("A", "B", "C", "D")

private enum class OptionVisual { Normal, Correct, Wrong }

/** Layar soal. Menggulung isinya sendiri, sama seperti layar lain. */
@Composable
fun QuizScreen(
    modifier: Modifier = Modifier,
    question: Question,
    index: Int,
    total: Int,
    score: Int,
    chosenIndex: Int,
    strings: Strings,
    onAnswer: (Int) -> Unit,
    onNext: () -> Unit,
    onSpeak: () -> Unit,
) {
    val answered = chosenIndex >= 0
    val isCorrectAnswer = answered && chosenIndex == question.correctIndex

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
    ) {
        StatusRow(
            counter = strings.counter.format(index + 1, total),
            scoreText = strings.scorePill.format(score),
        )
        Spacer(Modifier.height(12.dp))
        KuizProgress(fraction = if (total == 0) 0f else index.toFloat() / total)
        Spacer(Modifier.height(18.dp))

        KuizCard(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = question.prompt,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = if (question.askKorean) 54.sp else 28.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center,
                lineHeight = 62.sp,
                modifier = Modifier.fillMaxWidth(),
            )

            if (question.roman.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = question.roman,
                    color = KuizColors.roman,
                    fontSize = 17.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            Spacer(Modifier.height(22.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = if (question.askKorean) strings.askMeaning else strings.askWord,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center,
                )
                if (question.askKorean) {
                    Spacer(Modifier.width(10.dp))
                    SpeakButton(onSpeak)
                }
            }

            Spacer(Modifier.height(18.dp))

            question.options.forEachIndexed { optionIndex, label ->
                val visual = when {
                    !answered -> OptionVisual.Normal
                    optionIndex == question.correctIndex -> OptionVisual.Correct
                    optionIndex == chosenIndex -> OptionVisual.Wrong
                    else -> OptionVisual.Normal
                }
                OptionRow(
                    tag = "option-$optionIndex",
                    letter = LETTERS.getOrElse(optionIndex) { (optionIndex + 1).toString() },
                    text = label,
                    visual = visual,
                    dimmed = answered && visual == OptionVisual.Normal,
                    enabled = !answered,
                    onClick = { onAnswer(optionIndex) },
                )
                if (optionIndex != question.options.lastIndex) Spacer(Modifier.height(12.dp))
            }

            Spacer(Modifier.height(22.dp))

            if (answered) {
                Text(
                    text = buildString {
                        append(if (isCorrectAnswer) strings.praise.first() else strings.oops.first())
                        append(" ")
                        append(strings.answerIs.format(question.correctText))
                    },
                    color = if (isCorrectAnswer) KuizColors.success else MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                )
                Spacer(Modifier.height(16.dp))
                PrimaryButton(
                    text = if (index == total - 1) strings.finish else strings.next,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("next"),
                    onClick = onNext,
                )
            }
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun SpeakButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outline, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "🔊", fontSize = 15.sp)
    }
}

@Composable
private fun OptionRow(
    tag: String,
    letter: String,
    text: String,
    visual: OptionVisual,
    dimmed: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    val borderColor = when (visual) {
        OptionVisual.Correct -> KuizColors.success
        OptionVisual.Wrong -> MaterialTheme.colorScheme.error
        OptionVisual.Normal -> MaterialTheme.colorScheme.outline
    }
    val background = when (visual) {
        OptionVisual.Correct -> KuizColors.successSurface
        OptionVisual.Wrong -> KuizColors.errorSurface
        OptionVisual.Normal -> MaterialTheme.colorScheme.surfaceVariant
    }
    val textColor = when (visual) {
        OptionVisual.Correct -> KuizColors.successText
        OptionVisual.Wrong -> KuizColors.errorText
        OptionVisual.Normal -> MaterialTheme.colorScheme.onSurface
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(tag)
            .alpha(if (dimmed) 0.45f else 1f)
            .clip(RoundedCornerShape(14.dp))
            .background(background)
            .border(1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, borderColor.copy(alpha = 0.35f), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = letter,
                color = if (visual == OptionVisual.Normal) MaterialTheme.colorScheme.onSurface else textColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(Modifier.width(14.dp))
        Text(
            text = text,
            color = textColor,
            fontSize = 16.sp,
            lineHeight = 22.sp,
            modifier = Modifier.weight(1f),
        )
    }
}
