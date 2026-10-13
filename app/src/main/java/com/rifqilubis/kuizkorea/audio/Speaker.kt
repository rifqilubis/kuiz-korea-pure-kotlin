package com.rifqilubis.kuizkorea.audio

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import java.util.Locale

/**
 * Pelafalan kata Korea memakai TextToSpeech bawaan Android.
 *
 * Kalau perangkat tidak punya data suara Korea, mesin tetap hidup tetapi
 * `speak` tidak menghasilkan apa-apa — aplikasi tidak boleh gagal karenanya.
 */
class Speaker(context: Context) {

    private var tts: TextToSpeech? = null
    private var ready = false

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ready = true
                // setLanguage mengembalikan Int, jadi dipanggil sebagai metode.
                tts?.setLanguage(Locale.KOREAN)
            }
        }
    }

    fun speak(text: String) {
        if (!ready || text.isBlank()) return
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, Bundle(), UTTERANCE_ID)
    }

    fun shutdown() {
        ready = false
        tts?.stop()
        tts?.shutdown()
        tts = null
    }

    private companion object {
        const val UTTERANCE_ID = "kuiz-korea"
    }
}
