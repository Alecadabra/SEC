package org.example;

public class ExampleJavaApp
{
    static
    {
        System.loadLibrary("example_c_library");
    }

    public static void main(String[] args)
    {
        hello();
    }

    public static native void hello();
}
