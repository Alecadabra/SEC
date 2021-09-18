#include <jni.h>
#include <stdio.h>

// This construct is needed to make the C++ compiler generate C-compatible compiled code.
extern "C" 
{
    // Insert all your native functions here...
    
    JNIEXPORT void JNICALL Java_org_example_ExampleJavaApp_hello(JNIEnv *env, jclass cls)
    {
        printf("Hello world\n");
    }
}
