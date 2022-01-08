//
// Created by alienhe on 2022/1/7.
//

#ifndef SANDHOOK_LOGGING_H
#define SANDHOOK_LOGGING_H

#endif //SANDHOOK_LOGGING_H


#include <android/log.h>

#define ZIP_LOG_TAG "SandHook"

#define ALOGV(...) __android_log_print(ANDROID_LOG_INFO, ZIP_LOG_TAG ,__VA_ARGS__)
#define ALOGI(...) __android_log_print(ANDROID_LOG_INFO, ZIP_LOG_TAG ,__VA_ARGS__)
#define ALOGD(...) __android_log_print(ANDROID_LOG_DEBUG, ZIP_LOG_TAG ,__VA_ARGS__)
#define ALOGW(...) __android_log_print(ANDROID_LOG_WARN, ZIP_LOG_TAG ,__VA_ARGS__)
#define ALOGE(...) __android_log_print(ANDROID_LOG_ERROR, ZIP_LOG_TAG ,__VA_ARGS__)