#include "HelloWorld.h"
#include <stdio.h>
#include <jni.h>

JNIEXPORT void JNICALL Java_HelloWorld_print(JNIEnv *,jobject){
    printf("hahah !");
    return;
}
