# Text Editor

A JavaFX GUI text editor with a plugin/scripting API, key mappings specified in a custom DSL, file 
loading/saving with custom encoding, and full i18n for locales `en-AU` (Australian English) and 
`en-PT` (Pirate speak :p).

Implemented in Kotlin, with provided API implementations in Java and Python (See 
[Provided Plugins & Scripts](#provided-plugins--scripts)).

## Compile & Run

### Use default locale

```sh
./gradlew run
```

### Specify a locale

```sh
./gradlew run --args='--locale=en-AU'
```

or

```sh
./gradlew run --args='--locale=en-PT'
```

## Provided Plugins & Scripts

### Find (Java Plugin)

Fully qualified class name `texteditor.FindPlugin`.

Provides button and function key (F3) to search for a string after the caret.

### Date (Java Plugin)

Fully qualified class name `texteditor.DatePlugin`.

Provides a button to insert the current date/time at the caret.

### Emoji (Python Script)

Path [`emoji.py`](emoji.py)

Replaces occurrences of `:-)` with the emoji 😊.
