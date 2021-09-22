package org.example;

import java.util.Arrays;
import java.util.List;

public class ExampleJavaApp
{
    static
    {
        System.loadLibrary("example_c_library");
    }

    public static void main(String[] args)
    {
        System.out.println("Input a number for read()");
        double readVal = read(-1);
        System.out.println("read() read in " + readVal);

        printStr("This is using printStr()");

        printList(Arrays.asList("This", "is", "using", "printStr()"));
    }

    // Reads in and returns a double, or default value if failed
    public native static double read(double defaultValue);

    // Prints a given string to stdin with a newline
    public native static void printStr(String text);

    // Prints a given list separated by commas with a newline
    public native static void printList(List<String> list);
}
