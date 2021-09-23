# Reflection

A super simple example of using Java's reflection api.

## Usage

Where anything between `<>` is user-specified input.

```txt
$ javac *.java
$ java Reflector
Enter a java class name
Example
Enter string to pass into Example constructor
<Any string>
[Example] I'm being constructed with string "<Any string>"
Method names:
  * stringReturn
  * voidReturn
Enter the name of the desired method to call
<stringReturn | voidReturn>
Enter integer parameter for <stringReturn | voidReturn>(int)
<Any integer>
[Example] methodOne called with int "<Any integer>"
The method returned the following
[Example] Return from methodOne
Done! Exiting.
```
