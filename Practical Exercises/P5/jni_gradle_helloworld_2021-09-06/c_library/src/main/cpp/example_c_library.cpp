#include <jni.h>
#include <stdio.h>
#include <string.h>

// This construct is needed to make the C++ compiler generate C-compatible compiled code.
extern "C" 
{
    JNIEXPORT jdouble JNICALL Java_org_example_ExampleJavaApp_read(
        JNIEnv *env, jobject jobj, jdouble defaultValue
    ) {
        double read;

        // Read from scanf and store number of successful reads
        int numRead = scanf("%lf", &read);

        // If no reads, use default value
        if (numRead != 1) {
            read = defaultValue;
        }

        return read;
    }

    JNIEXPORT void JNICALL Java_org_example_ExampleJavaApp_printStr(
        JNIEnv *env, jobject jobj, jstring javaString
    ) {
        // Convert the java string to C string
        const char *string = env->GetStringUTFChars(javaString, NULL);

        // Print it
        printf("%s\n", string);
    }

    JNIEXPORT void JNICALL Java_org_example_ExampleJavaApp_printList(
        JNIEnv *env, jobject jobj, jobject list
    ) {
        // Get reference to the List class
        jclass listClass = env->GetObjectClass(list);
        // Get reference to the List.toString() method
        jmethodID listToString = env->GetMethodID(
            listClass, "toString", "()Ljava/lang/String;"
        );
        // Call list.toString() and get the java string
        jstring javaString = (jstring)env->CallObjectMethod(list, listToString);

        // Convert java string to c string
        const char* string = env->GetStringUTFChars(javaString, NULL);

        // Count length of string
        size_t stringLength = strlen(string);

        // Print substring to remove the "[" and "]"
        printf("%.*s\n", stringLength - 2, string + 1);
    }

}
