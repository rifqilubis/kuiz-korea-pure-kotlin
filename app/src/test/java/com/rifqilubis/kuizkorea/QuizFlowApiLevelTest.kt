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
 * Alur kuis yang sama, tetapi dijalankan di beberapa versi Android sekaligus.
 *
 * Versi 1.0.0 berhenti tepat saat kuis selesai di ponsel, sementara uji di satu
 * versi Android saja tidak menemukan sebabnya. Karena itu alur ini diuji di
 * beberapa API level: kalau masalahnya bergantung versi Android, di sini akan
 * terlihat pada versi mana.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [24, 26, 29, 31, 33], qualifiers = "w411dp-h891dp")
class QuizFlowApiLevelTest {

    @get:Rule
    val compose = createComposeRule()

    private fun tap(tag: String) {
        compose.onNodeWithTag(tag).performScrollTo().performClick()
    }

    @Test
    fun alurPenuhSampaiLayarHasil() {
        compose.setContent { KuizRoot() }

        tap("start")
        repeat(20) {
            tap("option-0")
            tap("next")
        }

        compose.onNodeWithText("Main lagi").performScrollTo().assertIsDisplayed()
        compose.onNodeWithText("Ganti pengaturan").performScrollTo().assertIsDisplayed()
    }
}
