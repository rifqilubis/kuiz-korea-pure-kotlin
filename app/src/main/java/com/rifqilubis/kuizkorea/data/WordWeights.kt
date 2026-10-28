package com.rifqilubis.kuizkorea.data

/**
 * Bobot tiap kata, disimpan di perangkat supaya kebiasaan salah ikut terbawa
 * ke sesi berikutnya.
 *
 * 0 = belum pernah muncul di kuis, 1 = netral, lebih dari 1 = sering salah.
 * Semakin tinggi bobotnya, semakin sering kata itu dipilih menjadi soal.
 */
class WordWeights(private val values: MutableMap<String, Int> = mutableMapOf()) {

    fun weightOf(ko: String): Int = values[ko] ?: 0

    /** Benar: bobot turun perlahan sampai netral (1), tidak pernah ke 0. */
    fun markCorrect(ko: String) {
        val now = weightOf(ko)
        values[ko] = if (now > 1) now - 1 else 1
    }

    /** Salah: bobot naik, dibatasi supaya satu kata tidak mendominasi. */
    fun markWrong(ko: String) {
        values[ko] = (weightOf(ko) + 2).coerceAtMost(MAX)
    }

    /**
     * Dipanggil sekali di akhir putaran. Putaran yang sempurna menghapus semua
     * catatan, jadi seluruh bank kembali sama rata.
     */
    fun finishRound(score: Int, total: Int) {
        if (total > 0 && score == total) reset()
    }

    fun reset() = values.clear()

    fun encode(): String = values.entries
        .filter { it.value > 0 }
        .joinToString("\n") { "${it.key}=${it.value}" }

    companion object {
        private const val MAX = 10

        fun decode(raw: String?): WordWeights {
            val map = mutableMapOf<String, Int>()
            raw?.lineSequence()?.forEach { line ->
                val parts = line.split('=', limit = 2)
                if (parts.size != 2) return@forEach
                val value = parts[1].toIntOrNull() ?: return@forEach
                if (value > 0) map[parts[0]] = value
            }
            return WordWeights(map)
        }
    }
}
