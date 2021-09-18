Example JNI 'Hello World' Application
=====================================

This is an example Gradle project with two subprojects: c_library and java_app. 

Running/Distributing
--------------------

The build logic up library paths automatically, so you can simply write your Java and C code, and 
type './gradlew run'.

You should also be able to run './gradlew build', to obtain a working distributable zip file in
java_app/build/distributions. This will contain both the compiled C library and .jar file. However,
it *won't* be platform independent.


Setting Key Details
-------------------

Change the following as needed:

(1) java_app/build.gradle sets the fully-qualified name of the main class:

    application {
        mainClass.set 'org.example.ExampleJavaApp'
    }
    
(2) c_library/build.gradle sets the native library name:

    library {
        baseName = 'example_c_library'
        ...
    }
    
(3) The same native library name must appear when loading the library from Java:
    
    static
    {
        System.loadLibrary("example_c_library");
    }
    
(4) Other Gradle constructs, including repositories, dependencies, etc. can be added to either 
    build.gradle file.
    

How it Works
------------

java_app/build.gradle sets up dependencies on parts of the c_library subproject. In order to run
code, we simply need to know where the 'debug' version of the compiled C library is. In order to
create a distributable .zip file, we need to find and copy the 'release' version of the C library.

The c_library subproject uses the 'cpp-library' plugin. This is the 'new' way of developing 
native libraries for C++. However, there are some things to note:

* It creates separate 'debug' and 'release' versions by applying different compiler/linker options.
* It cannot be used alongside the 'java' plugin, which is why we have two separate subprojects.
* There's no equivalent 'new' C plugin, but we can simply put our C code in a .cpp file.


Troubleshooting
---------------

`No tool chain is available to build C++ for host operating system...`

You need a C++ compiler, and it looks like you don't have one installed. Unfortunately this is one
thing that Gradle itself can't just fetch for you; you do need to install it yourself.

You'll need one of the following: gcc, clang, Visual Studio (if using Windows) or Xcode (if using 
MacOS). There is a build of gcc available for Windows called "MinGW-W64", and this may be the 
simplest Windows-based option.


`c_library/src/main/cpp/example_c_library.cpp:1:10: fatal error: jni.h: No such file or directory`

The C++ compiler couldn't find your JDK installation. Among other things, the JDK installation
contains 'include/jni.h' and related header files that give the C++ compiler access to the JNI 
declarations. 

In theory, this shouldn't happen because the 'hello world' example should find the JDK installation
automatically. But if you get this error anyway, you may need to set the JAVA_HOME environment 
variable to the directory containing your JDK installation. 


`Exception in thread "main" java.lang.UnsatisfiedLinkError: no <library-name> in java.library.path: [<library-path>]`

One of two things has gone wrong:
1. You're calling your native library different things in your Java code vs c_library/build.gradle.
   Note: when calling System.loadLibrary(), do not include any 'lib' prefix or '.so' or '.dll' 
   extension; just the 'base name'.

2. Your library path is incorrect. In theory this shouldn't happen, and may indicate a bug in this
   build logic. If you're trying './gradlew run', then instead try unzipping and running 
   distribution .zip file, or the other way around. 

As a last resort, and a very quick-and-dirty hack, you could try calling System.load(), which takes
an absolute filename, instead of System.loadLibrary(). (This will destroy portability, but it may 
at least help you get something working.)
