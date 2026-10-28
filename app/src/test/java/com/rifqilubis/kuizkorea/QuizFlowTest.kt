package com.rifqilubis.kuizkorea

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
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
 *
 * Catatan: setiap ketukan didahului performScrollTo(), karena layar kuis memang
 * bisa digulung dan tombol di bawah bisa berada di luar area tampilan.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], qualifiers = "w411dp-h891dp")
class QuizFlowTest {

    @get:Rule
    val compose = createComposeRule()

    private fun tap(tag: String) {
        compose.onNodeWithTag(tag).performScrollTo().performClick()
    }

    private fun tapText(text: String) {
        compose.onNodeWithText(text).performScrollTo().performClick()
    }

    /** Menyelesaikan satu putaran penuh (20 soal) sampai layar hasil. */
    private fun selesaikanSatuPutaran() {
        tap("start")
        repeat(20) {
            tap("option-0")
            tap("next")
        }
    }

    @Test
    fun menyelesaikanKuisMenampilkanLayarHasil() {
        compose.setContent { KuizRoot() }

        selesaikanSatuPutaran()

        // Kalau tidak ada pengecualian, layar hasil sudah tampil.
        compose.onNodeWithText("Main lagi").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Ganti pengaturan").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun mengulangKuisDariLayarHasilTetapBerjalan() {
        compose.setContent { KuizRoot() }

        selesaikanSatuPutaran()
        tapText("Main lagi")

        // Kembali ke soal pertama.
        compose.onNodeWithTag("option-0").performScrollTo().assertIsDisplayed()
    }

    @Test
    fun kembaliKePengaturanDariLayarHasil() {
        compose.setContent { KuizRoot() }

        selesaikanSatuPutaran()
        tapText("Ganti pengaturan")

        compose.onNodeWithTag("start").performScrollTo().assertIsDisplayed()
    }

    /** Tombol baru: mode kata kerja & sifat harus benar-benar membuka kuis. */
    @Test
    fun tombolKataKerjaDanSifatMembukaKuis() {
        compose.setContent { KuizRoot() }

        tap("start-verbs")

        compose.onNodeWithTag("option-0").performScrollTo().assertIsDisplayed()
    }
}
