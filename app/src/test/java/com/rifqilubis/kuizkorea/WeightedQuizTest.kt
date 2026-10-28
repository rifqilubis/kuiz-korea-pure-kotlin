package com.rifqilubis.kuizkorea

import com.rifqilubis.kuizkorea.data.Direction
import com.rifqilubis.kuizkorea.data.Quiz
import com.rifqilubis.kuizkorea.data.UiLang
import com.rifqilubis.kuizkorea.data.WORDS
import com.rifqilubis.kuizkorea.data.WordWeights
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.random.Random

/**
 * Bobot kata: salah menaikkan bobot, benar menurunkannya, kata yang belum
 * pernah muncul tetap dapat jatah, dan putaran 100% mengembalikan semuanya.
 */
class WeightedQuizTest {

    @Test
    fun salahMenaikkanBobotDanBenarMenurunkannya() {
        val w = WordWeights()
        assertEquals("belum pernah muncul", 0, w.weightOf("가다"))

        w.markWrong("가다")
        assertEquals(2, w.weightOf("가다"))
        w.markWrong("가다")
        assertEquals(4, w.weightOf("가다"))

        w.markCorrect("가다")
        assertEquals(3, w.weightOf("가다"))
        repeat(10) { w.markCorrect("가다") }
        assertEquals("tidak pernah kembali ke 0", 1, w.weightOf("가다"))
    }

    @Test
    fun putaranSempurnaMenghapusSemuaBobot() {
        val w = WordWeights()
        w.markWrong("가다")
        w.markWrong("먹다")
        w.markWrong("가다")

        w.finishRound(20, 20)
        assertEquals(0, w.weightOf("가다"))
        assertEquals(0, w.weightOf("먹다"))
        assertEquals("", w.encode())

        w.markWrong("가다")
        w.finishRound(19, 20)
        assertEquals("putaran tidak sempurna tidak mereset", 2, w.weightOf("가다"))
    }

    @Test
    fun kataSeringSalahJauhLebihSeringMuncul() {
        val w = WordWeights()
        repeat(3) { w.markWrong("가다") } // bobot 6
        val rng = Random(11)

        var muncul = 0
        repeat(100) {
            val round = Quiz.build(Direction.KO_ID, 10, UiLang.ID, rng, WORDS, w::weightOf)
            if (round.any { it.word.ko == "가다" }) muncul += 1
        }
        assertTrue("kata berbobot tinggi harus hampir selalu muncul, dapat $muncul", muncul >= 90)
    }

    @Test
    fun kataBaruTetapDapatJatahWalauAdaKataSeringSalah() {
        val w = WordWeights()
        repeat(5) { w.markWrong("가다") }

        val round = Quiz.build(Direction.KO_ID, 9, UiLang.ID, Random(3), WORDS, w::weightOf)
        val baru = round.count { w.weightOf(it.word.ko) == 0 }

        assertEquals(9, round.size)
        assertTrue("sepertiga jatah untuk kata baru, dapat $baru", baru >= 3)
        assertTrue("tidak ada kata dobel", round.map { it.word.ko }.toSet().size == round.size)
    }

    @Test
    fun modeKataKerjaDanSifatIkutMemakaiBobot() {
        val w = WordWeights()
        repeat(3) { w.markWrong("먹다") }

        var muncul = 0
        val rng = Random(5)
        repeat(50) {
            val round = Quiz.build(Direction.KO_ID, 6, UiLang.ID, rng, com.rifqilubis.kuizkorea.data.VERBS_ADJECTIVES, w::weightOf)
            if (round.any { it.word.ko == "먹다" }) muncul += 1
        }
        assertTrue("bobot berlaku juga di mode kata kerja & sifat, dapat $muncul", muncul >= 45)
    }

    @Test
    fun encodeDecodeBertahan() {
        val w = WordWeights()
        w.markWrong("가다")
        w.markWrong("좌식 문화")
        w.markCorrect("오다")

        val copy = WordWeights.decode(w.encode())
        assertEquals(2, copy.weightOf("가다"))
        assertEquals(2, copy.weightOf("좌식 문화"))
        assertEquals(1, copy.weightOf("오다"))
        assertEquals(0, copy.weightOf("먹다"))
        assertEquals(0, WordWeights.decode(null).weightOf("가다"))
    }
}
