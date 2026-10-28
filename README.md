# Kuiz Korea — Pure Kotlin

Aplikasi kuis kosakata Korea untuk Android, **ditulis 100% dengan Kotlin dan Jetpack Compose**.

Tidak ada WebView. Tidak ada aset HTML, CSS, atau JavaScript. Seluruh tampilan digambar Compose
dan seluruh logika kuis ada di kode Kotlin — termasuk bank kata 885 buah yang disimpan sebagai
daftar `Word(...)` di `Words.kt`.

**885 kata · 4 pilihan · 1 jawaban benar · 100% offline · tanpa satu pun izin**

## Unduh & pasang

1. Buka halaman **[Releases](../../releases)**.
2. Unduh `app-debug.apk`.
3. Buka berkas itu di ponsel → izinkan *Install unknown apps* (sekali saja) → **Install**.

> APK ditandatangani dengan debug key: cukup untuk dipasang sendiri dan dibagikan,
> belum layak untuk Google Play.

## Isi aplikasi

| Layar | Isi |
| --- | --- |
| Awal | pilih arah terjemahan (Korea → Indonesia, Indonesia → Korea, campuran), pilih jumlah soal (10/20/50/100/200/semua), skor terbaik, plus tombol **Kata kerja & sifat saja** (185 kata) yang melewati semua kata benda |
| Kuis | `Soal n / total`, lencana `Benar: n`, bilah progres, kata besar + romanisasi, empat pilihan A–D, umpan balik langsung |
| Hasil | persentase, jumlah benar/salah, waktu, pembahasan setiap jawaban salah, main lagi / ganti pengaturan |

Fitur lain:

- **Pelafalan Korea** memakai `TextToSpeech` bawaan Android (`Locale.KOREAN`).
- **Tema gelap/terang** dan **bahasa antarmuka Indonesia/Inggris** (mengubah bahasa juga
  mengubah bahasa pilihan jawaban), keduanya tersimpan di `SharedPreferences`.
- **Rekor per pengaturan** (arah + jumlah soal) tersimpan di perangkat.
- **Tombol kembali** mengikuti alur aplikasi, bukan menutup paksa.
- **Tanpa izin apa pun** — tidak butuh internet, karena bank kata ada di dalam APK.

## Struktur — semuanya Kotlin

```
kuiz-korea-pure-kotlin/
├─ app/build.gradle.kts             konfigurasi modul (Kotlin DSL)
├─ build.gradle.kts, settings.gradle.kts
└─ app/src/main/java/com/rifqilubis/kuizkorea/
   ├─ MainActivity.kt               Activity + seluruh state kuis (Compose)
   ├─ data/Quiz.kt                  mesin kuis: pengecoh, penyaring ambiguitas, skor
   ├─ data/Words.kt                 885 kata (Kotlin, bukan JSON/JS)
   ├─ audio/Speaker.kt              TextToSpeech Korea
   └─ ui/
      ├─ Theme.kt                   palet gelap & terang
      ├─ Text.kt                    teks Indonesia & Inggris
      ├─ Components.kt              kartu, tombol, bilah progres, bilah atas
      ├─ StartScreen.kt             layar pengaturan
      ├─ QuizScreen.kt              layar soal
      └─ ResultScreen.kt            layar hasil + pembahasan
```

Satu-satunya berkas non-Kotlin adalah berkas konfigurasi dan sumber daya Android
(`AndroidManifest.xml`, `res/values/*.xml`, ikon) — tidak ada kode selain Kotlin.
Perkiraan GitHub Linguist untuk repo ini: **sekitar 90% Kotlin**.

## Aturan "hanya satu jawaban benar"

Pengecoh diambil acak dari kata lain, tetapi dua pilihan yang saling mengandung tidak pernah
dipasangkan dalam satu soal — misalnya `rumah` vs `rumah sakit`, `merah` vs `merah muda`,
atau `문` vs `창문`. Logikanya ada di `Quiz.tooSimilar`, jadi mustahil ada soal dengan dua
jawaban benar.

## Kata yang sering salah muncul lebih sering

Setiap kata punya bobot yang disimpan di perangkat — tanpa akun, tanpa internet:

- **Salah** → bobot naik 2 (maksimal 10), jadi kata itu lebih sering jadi soal.
- **Benar** → bobot turun 1 sampai netral (1), jadi kata yang sudah dikuasai berhenti mendominasi.
- **Belum pernah muncul** (bobot 0) → selalu dapat sepertiga jatah soal, supaya seluruh bank kata perlahan tercakup.
- **Putaran selesai 100% benar** → semua bobot dihapus, seluruh bank kembali sama rata.

Berlaku untuk semua mode, termasuk tombol *Kata kerja & sifat saja*.

## Membangun sendiri

**Android Studio:** buka folder ini, tunggu Gradle sync, lalu Run.

**Terminal** (butuh JDK 17 + Android SDK):

```bash
./gradlew assembleDebug
# APK: app/build/outputs/apk/debug/app-debug.apk
```

**Tanpa memasang apa pun:** push ke GitHub — workflow di `.github/workflows/android.yml`
membangun APK di server GitHub, dan tag `v*` otomatis membuat Release berisi APK-nya.

## Hubungan dengan repo lain

Bank kata di `Words.kt` adalah padanan Kotlin dari `js/data.js` di repo web
[korean-quiz-deepseek-harness-version](https://github.com/rifqilubis/korean-quiz-deepseek-harness-version),
jadi isi kuisnya selalu sama dengan versi web dan versi WebView
([korean-quiz-android](https://github.com/rifqilubis/korean-quiz-android)).

## Catatan jujur

Kode ini diverifikasi lewat build GitHub Actions, bukan lewat emulator — tidak ada Android SDK
maupun perangkat di tempat proyek ini dibuat. Yang sudah dipastikan: APK terbangun, isinya
lengkap, dan kompilasi Kotlin/Compose-nya lolos. Yang belum diuji di perangkat: perilaku
Compose saat dijalankan dan ketersediaan suara TTS Korea di ponselmu.

## Kalau aplikasi berhenti karena kesalahan

Versi 1.1.0 ke atas mencatat sendiri kesalahan yang tidak tertangani:

1. Aplikasi berhenti seperti biasa.
2. Buka aplikasinya lagi — laporan kesalahan langsung tampil di layar.
3. Ketuk **Salin rincian**, lalu kirimkan isinya ke pengembang.
4. Ketuk **Tutup dan hapus** kalau sudah tidak diperlukan.

Laporan itu memuat ponsel, versi Android, nomor versi aplikasi, dan jejak lengkap
kesalahannya — tanpa perlu komputer, kabel, atau adb.

## Uji otomatis

```bash
./gradlew testDebugUnitTest
```

`QuizFlowTest` menjalankan alur kuis penuh (mulai → 20 soal → layar hasil) langsung di JVM
dengan Robolectric, sehingga layar hasil benar-benar digambar dan diukur.
`QuizFlowApiLevelTest` menjalankan alur yang sama di Android 7, 8, 10, 12, dan 13 sekaligus.
CI menjalankan kedua uji ini **sebelum** membangun APK, jadi alur yang rusak tidak akan
pernah sampai ke halaman Releases.

