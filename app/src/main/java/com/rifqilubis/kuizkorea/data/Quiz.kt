package com.rifqilubis.kuizkorea.data

import kotlin.random.Random

/** Arah terjemahan kuis. */
enum class Direction { KO_ID, ID_KO, MIXED }

/** Bahasa antarmuka sekaligus bahasa pilihan jawaban. */
enum class UiLang { ID, EN }

/** Arti kata sesuai bahasa antarmuka. */
fun Word.meaning(lang: UiLang): String = if (lang == UiLang.EN) en else id

/** Satu soal: satu kata, empat pilihan, satu jawaban benar. */
data class Question(
    val word: Word,
    val askKorean: Boolean,
    val prompt: String,
    val roman: String,
    val options: List<String>,
    val correctIndex: Int,
) {
    val correctText: String get() = options[correctIndex]
}

/** Jawaban yang sudah dipilih pemain, dipakai untuk layar hasil. */
data class Answer(val question: Question, val chosenIndex: Int) {
    val chosenText: String
        get() = if (chosenIndex in question.options.indices) question.options[chosenIndex] else ""
    val isCorrect: Boolean get() = chosenIndex == question.correctIndex
}

/**
 * Mesin kuis — padanan Kotlin dari `js/app.js` di repo web.
 *
 * Aturan pentingnya sama: pengecoh diambil acak dari kata lain, tetapi dua
 * pilihan yang saling mengandung tidak pernah dipasangkan dalam satu soal
 * (mis. "rumah" vs "rumah sakit", "문" vs "창문"), sehingga tidak mungkin ada
 * soal dengan dua jawaban benar.
 */
object Quiz {

    fun normalize(text: String): String = text
        .lowercase()
        .replace(Regex("[^a-z0-9\\uac00-\\ud7a3 ]"), " ")
        .replace(Regex("\\s+"), " ")
        .trim()

    fun tooSimilar(a: String, b: String): Boolean {
        val x = normalize(a)
        val y = normalize(b)
        if (x.isEmpty() || y.isEmpty()) return true
        return x == y || x.contains(y) || y.contains(x)
    }

    private fun labelOf(word: Word, askKorean: Boolean, lang: UiLang): String =
        if (askKorean) word.meaning(lang) else word.ko

    fun build(
        direction: Direction,
        count: Int,
        lang: UiLang,
        random: Random = Random.Default,
    ): List<Question> {
        val wanted = if (count <= 0) WORDS.size else minOf(count, WORDS.size)
        return WORDS.shuffled(random).take(wanted).map { question(it, direction, lang, random) }
    }

    private fun question(
        word: Word,
        direction: Direction,
        lang: UiLang,
        random: Random,
    ): Question {
        val askKorean = when (direction) {
            Direction.KO_ID -> true
            Direction.ID_KO -> false
            Direction.MIXED -> random.nextBoolean()
        }

        val correct = labelOf(word, askKorean, lang)
        val others = WORDS.filter { it.ko != word.ko }

        val used = mutableListOf(correct)
        val distractors = mutableListOf<String>()

        for (candidate in others.shuffled(random)) {
            if (distractors.size == DISTRACTORS) break
            val label = labelOf(candidate, askKorean, lang)
            if (label.isBlank()) continue
            if (used.any { tooSimilar(it, label) }) continue
            used.add(label)
            distractors.add(label)
        }

        // Jaring pengaman: dengan 885 kata ini praktis tidak pernah terpakai.
        if (distractors.size < DISTRACTORS) {
            for (candidate in others.shuffled(random)) {
                if (distractors.size == DISTRACTORS) break
                val label = labelOf(candidate, askKorean, lang)
                if (label == correct || distractors.contains(label)) continue
                distractors.add(label)
            }
        }

        val options = (distractors + correct).shuffled(random)
        return Question(
            word = word,
            askKorean = askKorean,
            prompt = if (askKorean) word.ko else word.meaning(lang),
            roman = if (askKorean) word.rom else "",
            options = options,
            correctIndex = options.indexOf(correct),
        )
    }

    private const val DISTRACTORS = 3
}
