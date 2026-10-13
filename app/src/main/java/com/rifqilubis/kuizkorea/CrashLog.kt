package com.rifqilubis.kuizkorea

import android.content.Context
import android.os.Build
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Perekam crash sederhana.
 *
 * Kalau aplikasi berhenti karena kesalahan, jejaknya disimpan ke berkas di
 * penyimpanan internal aplikasi. Saat aplikasi dibuka lagi, jejak itu
 * ditampilkan di layar sehingga bisa dibaca atau disalin — tanpa perlu
 * komputer, kabel, atau adb.
 */
object CrashLog {

    private const val FILE_NAME = "crash-terakhir.txt"

    /** Dipanggil paling awal di onCreate, sebelum tampilan disiapkan. */
    fun install(context: Context) {
        val previous = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, error ->
            runCatching { file(context).writeText(describe(thread, error)) }
            previous?.uncaughtException(thread, error)
        }
    }

    fun read(context: Context): String? =
        file(context).takeIf { it.exists() }?.readText()?.takeIf { it.isNotBlank() }

    fun clear(context: Context) {
        runCatching { file(context).delete() }
    }

    private fun file(context: Context) = File(context.filesDir, FILE_NAME)

    private fun describe(thread: Thread, error: Throwable): String = buildString {
        appendLine("Kuiz Korea — laporan kesalahan")
        appendLine("waktu    : " + SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date()))
        appendLine("ponsel   : " + Build.MANUFACTURER + " " + Build.MODEL)
        appendLine("android  : API " + Build.VERSION.SDK_INT + " (" + Build.VERSION.RELEASE + ")")
        appendLine("aplikasi : versi " + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")")
        appendLine("utas     : " + thread.name)
        appendLine()
        appendLine(Log.getStackTraceString(error))
    }
}
