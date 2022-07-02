package com.hanmajid.android.tiramisu.perapplanguagepreferences

import android.annotation.SuppressLint
import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

@SuppressLint("InlinedApi")
class MainActivity : AppCompatActivity() {
    private var localeManager: LocaleManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            localeManager =
                getSystemService(Context.LOCALE_SERVICE) as LocaleManager
        }

        findViewById<Button>(R.id.button_lang_en).setOnClickListener {
            localeManager?.applicationLocales = LocaleList(Locale.forLanguageTag("en"))
        }
        findViewById<Button>(R.id.button_lang_id).setOnClickListener {
            localeManager?.applicationLocales = LocaleList(Locale.forLanguageTag("id-ID"))
        }
        findViewById<Button>(R.id.button_lang_reset).setOnClickListener {
            localeManager?.applicationLocales = LocaleList.getEmptyLocaleList()
        }
    }

    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val language = when (localeManager?.applicationLocales?.toLanguageTags()) {
                "en" -> "English"
                "id-ID" -> "Indonesian"
                else -> "Not Set"
            }
            Log.wtf("LANG", localeManager?.applicationLocales?.toLanguageTags())
            findViewById<TextView>(R.id.text_current_language).text =
                "Current In-App Language: $language"
        }
    }
}