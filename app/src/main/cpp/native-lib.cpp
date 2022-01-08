//
// Created by alienhe on 2022/1/6.
//
#include <jni.h>
#include "sandhook_native.h"
#include "logging.h"


int (*old__system_property_get)(const char *__name, char *__value);

//我们之前从native反射到java，有很大问题。华为有一个机型直接卡白屏。目前原因未知，我们将属性替换重心逻辑由java层迁移到native层
//这样会损失一些灵活性，因为不能回调java的handler了。所有替换项需要提前设置
int new__system_property_get(const char *__name, char *__value) {
    int result = old__system_property_get(__name, __value);
    if (result < 0) {
        //failed
        return result;
    }

    ALOGD("__system_property_get origin value:%s,replaced value:%s",__name,__value);
    return result;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_swift_sandhook_NativeHookTest_hook(JNIEnv *env, jclass clazz) {

    // test hook __system_property_get
    void *symbol = SandGetSym("/apex/com.android.runtime/lib64/bionic/libc.so","__system_property_get");
    void *origin_back_up_method = SandSingleInstHook(symbol,(void *) &new__system_property_get);
    ALOGI("sand single inst hook libc __system_property_get :%d",
          origin_back_up_method == nullptr);
    if (origin_back_up_method == nullptr) {
        ALOGE("sand single inst hook libc __system_property_get failed!!!");
    } else {
        old__system_property_get = reinterpret_cast<int (*)(const char *,char *)>(origin_back_up_method);
    }
}