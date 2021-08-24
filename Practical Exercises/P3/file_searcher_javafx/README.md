# File Searcher

This is to demonstrate the use of multithreaded data structures and thread
pools in Java, by adding multithreading to a single-threaded JavaFX file search
app.

To run, you need Java 11 and some environment that JavaFX can handle, it's
always a gamble but I've had luck on plain old Ubuntu.

```bash
$ ./gradlew run
```

Enter a search term and the app will search the contents of all files under
the specified path.