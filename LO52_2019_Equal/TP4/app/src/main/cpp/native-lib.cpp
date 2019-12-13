#include <jni.h>
#include <string>

int random_a(){
    return rand() % 11;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_startup42_tp4_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jint JNICALL Java_com_startup42_tp4_MainActivity_readJNI(JNIEnv *env, jobject obj)
{
    int v = random_a();
    return v*v;
}

extern "C" JNIEXPORT jint JNICALL Java_com_startup42_tp4_MainActivity_writeJNI(JNIEnv *env, jobject obj)
{
    int v = random_a();
    return v*v*v;
}

extern "C" JNIEXPORT jstring JNICALL Java_com_startup42_tp4_MainActivity_directionJNI(
        JNIEnv *env,
        jobject obj,
        jstring button)
{
    char* directionAllemand;
    const char *nativeString = env->GetStringUTFChars(button,0);
    std::string toTest(nativeString);
    if (toTest == "RIGHT") {
        directionAllemand = "Recht";
    } else if (toTest == "LEFT") {
        directionAllemand = "Links";
    } else if (toTest == "UP") {
        directionAllemand = "Haut";
    } else {
        directionAllemand = "Bas";
    }
    env->ReleaseStringUTFChars(button,nativeString);
    return env->NewStringUTF(directionAllemand);
}
