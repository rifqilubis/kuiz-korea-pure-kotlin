package com.rifqilubis.kuizkorea

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Uji alur kuis dari awal sampai layar hasil, dijalankan di JVM dengan
 * Robolectric — jadi seluruh layar benar-benar digambar dan diukur.
 *
 * Uji ini dibuat setelah versi 1.0.0 crash tepat saat kuis selesai: Column
 * terluar dan layar hasil sama-sama memakai Modifier.verticalScroll, dan
 * Compose menolak komponen yang bisa di-scroll vertikal secara bersarang
 * ("Vertically scrollable component was measured with an infinity maximum
 * height constraints"). Layar hasil hanya muncul di akhir kuis, itulah sebabnya
 * crash-nya baru terasa di situ.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class QuizFlowTest {

    @get:Rule
    val compose = createComposeRule()

    /** Menyelesaikan satu putaran penuh dan memastikan layar hasil tampil. */
    @Test
    fun menyelesaikanKuisMenampilkanLayarHasil() {
        compose.setContent { KuizRoot() }

        compose.onNodeWithTag("start").performClick()

        // Pengaturan bawaan: 20 soal. Jawab pilihan pertama, lalu lanjut.
        repeat(20) {
            compose.onNodeWithTag("option-0").performClick()
            compose.onNodeWithTag("next").performClick()
        }

        // Kalau tidak ada pengecualian, layar hasil sudah tampil.
        compose.onNodeWithText("Main lagi").assertIsDisplayed()
        compose.onNodeWithText("Ganti pengaturan").assertIsDisplayed()
    }

    /** Layar hasil juga harus bisa dibuka ulang tanpa masalah. */
    @Test
    fun mengulangKuisDariLayarHasilTetapBerjalan() {
        compose.setContent { KuizRoot() }

        compose.onNodeWithTag("start").performClick()
        repeat(20) {
            compose.onNodeWithTag("option-0").performClick()
            compose.onNodeWithTag("next").performClick()
        }

        compose.onNodeWithText("Main lagi").performClick()

        // Kembali ke soal pertama.
        compose.onNodeWithTag("option-0").assertIsDisplayed()
    }

    /** Berpindah ke pengaturan dari layar hasil mengembalikan layar awal. */
    @Test
    fun kembaliKePengaturanDariLayarHasil() {
        compose.setContent { KuizRoot() }

        compose.onNodeWithTag("start").performClick()
        repeat(20) {
            compose.onNodeWithTag("option-0").performClick()
            compose.onNodeWithTag("next").performClick()
        }

        compose.onNodeWithText("Ganti pengaturan").performClick()
        compose.onNodeWithTag("start").assertIsDisplayed()
    }
}
