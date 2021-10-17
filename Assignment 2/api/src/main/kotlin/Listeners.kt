interface Listeners {
    fun button(text: String, callback: () -> Unit)

    fun functionKey(functionKey: FunctionKey, callback: () -> Unit)

    fun textDialog(prompt: String, callback: (String?) -> Unit)
}