package com.rifqilubis.kuizkorea

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rifqilubis.kuizkorea.audio.Speaker
import com.rifqilubis.kuizkorea.data.Answer
import com.rifqilubis.kuizkorea.data.Direction
import com.rifqilubis.kuizkorea.data.Question
import com.rifqilubis.kuizkorea.data.Quiz
import com.rifqilubis.kuizkorea.data.UiLang
import com.rifqilubis.kuizkorea.data.WORDS
import com.rifqilubis.kuizkorea.ui.KuizColors
import com.rifqilubis.kuizkorea.ui.KuizTheme
import com.rifqilubis.kuizkorea.ui.QuizScreen
import com.rifqilubis.kuizkorea.ui.ResultScreen
import com.rifqilubis.kuizkorea.ui.StartScreen
import com.rifqilubis.kuizkorea.ui.TopBar
import com.rifqilubis.kuizkorea.ui.stringsFor

/**
 *  Kuiz Korea — versi Kotlin murni.
 *
 *  Tidak ada WebView dan tidak ada aset HTML/JS: seluruh tampilan digambar
 *  dengan Jetpack Compose dan seluruh logika kuis ada di paket `data`.
 *  Aplikasi tidak meminta izin apa pun, jadi sepenuhnya offline.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { KuizRoot() }
    }
}

private enum class Screen { Start, Quiz, Result }

private const val PREFS = "kuiz-korea"
private const val KEY_DARK = "theme-dark"
private const val KEY_LANG = "ui-lang"

@Composable
internal fun KuizRoot() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(PREFS, Context.MODE_PRIVATE) }
    val speaker = remember { Speaker(context) }
    DisposableEffect(Unit) {
        onDispose { speaker.shutdown() }
    }

    var dark by remember { mutableStateOf(prefs.getBoolean(KEY_DARK, true)) }
    var lang by remember { mutableStateOf(if (prefs.getString(KEY_LANG, "id") == "en") UiLang.EN else UiLang.ID) }

    KuizTheme(dark = dark) {
        val strings = stringsFor(lang)

        var screen by remember { mutableStateOf(Screen.Start) }
        var direction by remember { mutableStateOf(Direction.KO_ID) }
        var count by remember { mutableIntStateOf(20) }

        var questions by remember { mutableStateOf<List<Question>>(emptyList()) }
        var index by remember { mutableIntStateOf(0) }
        var chosen by remember { mutableIntStateOf(-1) }
        var answers by remember { mutableStateOf<List<Answer>>(emptyList()) }
        var startedAt by remember { mutableLongStateOf(0L) }
        var elapsedMs by remember { mutableLongStateOf(0L) }
        var isNewBest by remember { mutableStateOf(false) }

        fun bestKey(forDirection: Direction, forCount: Int): String {
            val total = if (forCount <= 0) WORDS.size else minOf(forCount, WORDS.size)
            return "best:${forDirection.name}:$total"
        }

        fun startQuiz() {
            questions = Quiz.build(direction, count, lang)
            index = 0
            chosen = -1
            answers = emptyList()
            isNewBest = false
            startedAt = System.currentTimeMillis()
            elapsedMs = 0L
            screen = if (questions.isEmpty()) Screen.Start else Screen.Quiz
        }

        fun answer(optionIndex: Int) {
            if (chosen >= 0) return
            val current = questions.getOrNull(index) ?: return
            if (optionIndex !in current.options.indices) return
            chosen = optionIndex
            answers = answers + Answer(current, optionIndex)
        }

        fun next() {
            if (index + 1 >= questions.size) {
                elapsedMs = System.currentTimeMillis() - startedAt
                val score = answers.count { it.isCorrect }
                val percent = if (questions.isEmpty()) 0 else Math.round(score * 100f / questions.size)
                val key = bestKey(direction, count)
                val stored = prefs.getInt(key, -1)
                isNewBest = percent > stored
                if (isNewBest) prefs.edit().putInt(key, percent).apply()
                screen = Screen.Result
            } else {
                index += 1
                chosen = -1
            }
        }

        BackHandler(enabled = screen != Screen.Start) { screen = Screen.Start }

        val current = questions.getOrNull(index)
        val modeLabel = if (screen == Screen.Quiz && current != null) {
            strings.mode(if (current.askKorean) Direction.KO_ID else Direction.ID_KO)
        } else {
            strings.mode(direction)
        }

        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.fillMaxSize(),
        ) {
            // Column ini SENGAJA tidak bisa di-scroll. Kalau ia di-scroll,
            // layar di dalamnya yang juga punya verticalScroll (layar hasil)
            // akan diukur dengan tinggi tak terhingga dan Compose melempar
            // "Vertically scrollable component was measured with an infinity
            // maximum height constraints". Setiap layar mengatur scroll-nya
            // sendiri, dibatasi oleh weight(1f) di bawah ini.
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(KuizColors.glow)
                    .padding(horizontal = 18.dp),
            ) {
                Spacer(Modifier.height(18.dp))
                TopBar(
                    modeLabel = modeLabel,
                    langCode = if (lang == UiLang.EN) "EN" else "ID",
                    themeGlyph = if (dark) "☀" else "☾",
                    strings = strings,
                    onTheme = {
                        dark = !dark
                        prefs.edit().putBoolean(KEY_DARK, dark).apply()
                    },
                    onLang = {
                        // Bahasa dikunci selama kuis, sama seperti versi web:
                        // pilihan jawaban sudah dibangun memakai bahasa saat itu.
                        if (screen != Screen.Quiz) {
                            lang = if (lang == UiLang.EN) UiLang.ID else UiLang.EN
                            prefs.edit().putString(KEY_LANG, if (lang == UiLang.EN) "en" else "id").apply()
                        }
                    },
                )
                Spacer(Modifier.height(20.dp))

                when (screen) {
                    Screen.Start -> StartScreen(
                        modifier = Modifier.weight(1f),
                        strings = strings,
                        direction = direction,
                        count = count,
                        best = remember(direction, count, screen) {
                            prefs.getInt(bestKey(direction, count), -1).takeIf { it >= 0 }
                        },
                        onDirection = { direction = it },
                        onCount = { count = it },
                        onStart = { startQuiz() },
                    )

                    Screen.Quiz -> {
                        val question = current
                        if (question != null) {
                            QuizScreen(
                                modifier = Modifier.weight(1f),
                                question = question,
                                index = index,
                                total = questions.size,
                                score = answers.count { it.isCorrect },
                                chosenIndex = chosen,
                                strings = strings,
                                onAnswer = { answer(it) },
                                onNext = { next() },
                                onSpeak = { speaker.speak(question.word.ko) },
                            )
                        }
                    }

                    Screen.Result -> ResultScreen(
                        modifier = Modifier.weight(1f),
                        answers = answers,
                        total = questions.size,
                        elapsedMs = elapsedMs,
                        isNewBest = isNewBest,
                        strings = strings,
                        onPlayAgain = { startQuiz() },
                        onSettings = { screen = Screen.Start },
                    )
                }

                Spacer(Modifier.height(28.dp))
            }
        }
    }
}
