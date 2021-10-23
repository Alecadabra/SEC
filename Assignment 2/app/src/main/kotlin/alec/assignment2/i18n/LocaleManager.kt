package alec.assignment2.i18n

import java.util.*

/**
 * Handles parsing the locale from command line arguments.
 */
object LocaleManager {
    /**
     * Parses the locale from the command line args, where `"--name=value"` is converted to a map
     * [args] of `name: value`.
     *
     * Takes the arg `locale` into [Locale.forLanguageTag] or if not present uses
     * [Locale.getDefault].
     *
     * Also sets the default locale for this JVM as a side effect.
     */
    fun parseLocale(args: Map<String, String>): Locale {
        val tag = args["locale"]
        val locale = if (tag != null) Locale.forLanguageTag(tag) else Locale.getDefault()
        Locale.setDefault(locale)
        return locale
    }
}