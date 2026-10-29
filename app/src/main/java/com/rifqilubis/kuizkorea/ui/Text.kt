package com.rifqilubis.kuizkorea.ui

import com.rifqilubis.kuizkorea.data.UiLang

/**
 * Seluruh teks antarmuka. Dua bahasa: Indonesia (bawaan) dan Inggris.
 *
 * Placeholder memakai gaya String.format, mis. "%1\$d". Tanda dolar wajib
 * di-escape, kalau tidak Kotlin membacanya sebagai template string.
 */
data class Strings(
    val brandKo: String,
    val brandQuiz: String,

    val modeKoId: String,
    val modeIdKo: String,
    val modeMixed: String,

    val counter: String,          // Soal 1 / 20
    val scorePill: String,        // Benar: 2
    val askMeaning: String,
    val askWord: String,
    val praise: List<String>,
    val oops: List<String>,
    val answerIs: String,         // Jawabannya: ...
    val next: String,
    val finish: String,

    val startTitle: String,
    val startSub: String,         // ... n kata
    val directionLabel: String,
    val countLabel: String,
    val countAll: String,
    val start: String,
    val verbAdjStart: String,     // tombol khusus kata kerja & sifat
    val nonVerbStart: String,     // tombol khusus selain kata kerja & sifat
    val bestLine: String,         // n persen
    val bestNone: String,
    val hint: String,

    val resultTitle: String,
    val msgGreat: String,
    val msgGood: String,
    val msgOk: String,
    val msgBad: String,
    val statCorrect: String,
    val statWrong: String,
    val statTime: String,
    val newBest: String,
    val reviewTitle: String,
    val yourAnswer: String,
    val correctAnswer: String,
    val playAgain: String,
    val changeSettings: String,

    val speak: String,
    val themeToggle: String,
    val langToggle: String,
) {
    fun mode(direction: com.rifqilubis.kuizkorea.data.Direction): String = when (direction) {
        com.rifqilubis.kuizkorea.data.Direction.KO_ID -> modeKoId
        com.rifqilubis.kuizkorea.data.Direction.ID_KO -> modeIdKo
        com.rifqilubis.kuizkorea.data.Direction.MIXED -> modeMixed
    }
}

private val Indonesian = Strings(
    brandKo = "한국어",
    brandQuiz = "Quiz",
    modeKoId = "Kosakata Korea → Bahasa Indonesia",
    modeIdKo = "Kosakata Indonesia → Korea",
    modeMixed = "Campuran Korea ↔ Indonesia",
    counter = "Soal %1\$d / %2\$d",
    scorePill = "Benar: %1\$d",
    askMeaning = "APA ARTI KATA INI?",
    askWord = "APA KATA KOREA DARI INI?",
    praise = listOf("Tepat sekali!", "Benar!", "Keren, betul!", "Mantap, benar!"),
    oops = listOf("Belum tepat.", "Salah.", "Masih keliru."),
    answerIs = "Jawabannya: %1\$s",
    next = "Soal berikutnya",
    finish = "Lihat hasil",
    startTitle = "Tebak arti kosakata Korea",
    startSub = "Satu kata muncul dan ada empat pilihan jawaban. Hanya satu yang benar. Bank soal berisi %1\$d kata.",
    directionLabel = "Arah terjemahan",
    countLabel = "Jumlah soal",
    countAll = "Semua",
    start = "Mulai kuis",
    verbAdjStart = "Kata kerja & sifat saja (%1\$d)",
    nonVerbStart = "Selain kata kerja & sifat (%1\$d)",
    bestLine = "Skor terbaik untuk pengaturan ini: %1\$d%%",
    bestNone = "Belum ada rekor untuk pengaturan ini.",
    hint = "Ketuk tombol pengeras suara untuk mendengar pelafalan Korea.",
    resultTitle = "Hasil kuis",
    msgGreat = "Luar biasa! Kosakata Korea-mu sudah kuat.",
    msgGood = "Bagus! Sedikit lagi menuju sempurna.",
    msgOk = "Lumayan. Terus berlatih ya.",
    msgBad = "Jangan menyerah, ulangi dan hafalkan lagi.",
    statCorrect = "Benar",
    statWrong = "Salah",
    statTime = "Waktu",
    newBest = "🏆 Rekor baru!",
    reviewTitle = "Pembahasan jawaban yang salah",
    yourAnswer = "Jawabanmu",
    correctAnswer = "Jawaban benar",
    playAgain = "Main lagi",
    changeSettings = "Ganti pengaturan",
    speak = "Dengarkan pelafalan",
    themeToggle = "Ganti tema terang/gelap",
    langToggle = "Ganti bahasa antarmuka",
)

private val English = Strings(
    brandKo = "한국어",
    brandQuiz = "Quiz",
    modeKoId = "Korean → Indonesian Vocabulary",
    modeIdKo = "Indonesian → Korean Vocabulary",
    modeMixed = "Mixed Korean ↔ Indonesian",
    counter = "Question %1\$d / %2\$d",
    scorePill = "Correct: %1\$d",
    askMeaning = "WHAT DOES THIS WORD MEAN?",
    askWord = "WHAT IS THE KOREAN WORD FOR THIS?",
    praise = listOf("Exactly!", "Correct!", "Nice, that's right!", "Well done!"),
    oops = listOf("Not quite.", "Wrong.", "Still off."),
    answerIs = "The answer is: %1\$s",
    next = "Next question",
    finish = "See results",
    startTitle = "Guess the meaning of Korean words",
    startSub = "One word appears with four answer choices. Only one is correct. The question bank holds %1\$d words.",
    directionLabel = "Translation direction",
    countLabel = "Number of questions",
    countAll = "All",
    start = "Start quiz",
    verbAdjStart = "Verbs & adjectives only (%1\$d)",
    nonVerbStart = "Nouns & everything else (%1\$d)",
    bestLine = "Best score for these settings: %1\$d%%",
    bestNone = "No record yet for these settings.",
    hint = "Tap the speaker button to hear the Korean pronunciation.",
    resultTitle = "Quiz result",
    msgGreat = "Outstanding! Your Korean vocabulary is strong.",
    msgGood = "Nice work! Almost perfect.",
    msgOk = "Not bad. Keep practising.",
    msgBad = "Don't give up — review and try again.",
    statCorrect = "Correct",
    statWrong = "Wrong",
    statTime = "Time",
    newBest = "🏆 New record!",
    reviewTitle = "Review of wrong answers",
    yourAnswer = "Your answer",
    correctAnswer = "Correct answer",
    playAgain = "Play again",
    changeSettings = "Change settings",
    speak = "Hear the pronunciation",
    themeToggle = "Switch light/dark theme",
    langToggle = "Switch interface language",
)

fun stringsFor(lang: UiLang): Strings = if (lang == UiLang.EN) English else Indonesian
