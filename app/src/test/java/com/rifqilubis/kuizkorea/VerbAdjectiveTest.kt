package com.rifqilubis.kuizkorea

import com.rifqilubis.kuizkorea.data.Direction
import com.rifqilubis.kuizkorea.data.Quiz
import com.rifqilubis.kuizkorea.data.UiLang
import com.rifqilubis.kuizkorea.data.VERBS_ADJECTIVES
import com.rifqilubis.kuizkorea.data.WORDS
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

/**
 * Bank kata mode "kata kerja & sifat": pastikan penyaringnya tidak
 * memasukkan kata benda, dan kuisnya benar-benar hanya memakai kata itu.
 */
class VerbAdjectiveTest {

    @Test
    fun bankHanyaKataKerjaDanKataSifat() {
        assertTrue("bank tidak boleh kosong", VERBS_ADJECTIVES.isNotEmpty())
        assertTrue(
            "semua entri harus berbentuk kamus (-다)",
            VERBS_ADJECTIVES.all { it.ko.endsWith("다") },
        )
        assertFalse("바다 adalah kata benda", VERBS_ADJECTIVES.any { it.ko == "바다" })
        assertFalse(
            "bentuk -니다 adalah kalimat, bukan kata dasar",
            VERBS_ADJECTIVES.any { it.ko.endsWith("니다") },
        )
        assertTrue("harus lebih sedikit dari seluruh bank", VERBS_ADJECTIVES.size < WORDS.size)
    }

    @Test
    fun kuisHanyaBerisiKataItu() {
        val allowed = VERBS_ADJECTIVES.map { it.ko }.toSet()
        val questions = Quiz.build(Direction.MIXED, 0, UiLang.ID, Random(7), VERBS_ADJECTIVES)

        assertEquals("tanpa batas jumlah, semua kata terpakai", VERBS_ADJECTIVES.size, questions.size)
        assertTrue(questions.all { it.word.ko in allowed })
        assertTrue(questions.all { it.options.size == 4 })
        assertTrue("tidak boleh ada pilihan ganda", questions.all { q -> q.options.toSet().size == q.options.size })
        assertTrue(questions.all { q -> q.options.contains(q.correctText) })
    }
}
