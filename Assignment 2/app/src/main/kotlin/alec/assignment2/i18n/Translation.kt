package alec.assignment2.i18n

import java.util.*

sealed class Translation(val locale: Locale) {
    abstract val appName: String
    abstract val minutes: (Int) -> String

    companion object {
        operator fun get(locale: Locale): Translation = values.singleOrNull {
            it.locale == locale
        } ?: DEFAULT

        val DEFAULT = EnglishAustralia

        val values = listOf(EnglishAustralia)
    }

    object EnglishAustralia : Translation(Locale.forLanguageTag("en-AU")) {
        override val appName: String = "Text Editor"
        override val minutes: (Int) -> String = { "$it" }
    }
}
