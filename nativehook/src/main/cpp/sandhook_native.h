//
// Created by SwiftGan on 2019/4/15.
//

#pragma once

#include <signal.h>
#include <sys/mman.h>


/**
 * target为android q的行为变更:
 * 从 Android 10 开始，系统二进制文件和库的可执行部分会映射到只执行（不可读取）内存，作为防范代码重用攻击的一种安全强化技术。
 * 如果您的应用针对已标记为只执行的内存段执行读取操作（无论此读取操作是来自错误、漏洞还是有意的内存检查），系统都会向您的应用发送 SIGSEGV 信号。
 *
 * Cause: execute-only (no-read) memory access error; likely due to data in .text.
 *
 * eg.https://github.com/asLody/SandHook/pull/83 On Q devices, some system function is executable only. mprotect it to rwx before hooking.
 */
#define _uintval(p)               reinterpret_cast<uintptr_t>(p)
#define _ptr(p)                   reinterpret_cast<void *>(p)
#define _align_up(x, n)           (((x) + ((n) - 1)) & ~((n) - 1))
#define _align_down(x, n)         ((x) & -(n))
#define _page_size                4096
#define _page_align(n)            _align_up(static_cast<uintptr_t>(n), _page_size)
#define _ptr_align(x)             _ptr(_align_down(reinterpret_cast<uintptr_t>(x), _page_size))
#define _make_rwx(p, n)           ::mprotect(_ptr_align(p), \
                                              _page_align(_uintval(p) + n) != _page_align(_uintval(p)) ? _page_align(n) + _page_size : _page_align(n), \
                                              PROT_READ | PROT_WRITE | PROT_EXEC)

typedef size_t REG;

#define EXPORT  __attribute__ ((visibility ("default")))

#define BreakCallback(callback) bool(*callback)(sigcontext*, void*)

extern "C"
EXPORT void* SandGetModuleBase(const char* so);

extern "C"
EXPORT void* SandGetSym(const char* so, const char* sym);

extern "C"
EXPORT void* SandInlineHook(void* origin, void* replace);

extern "C"
EXPORT void* SandInlineHookSym(const char* so, const char* symb, void* replace);

extern "C"
EXPORT void* SandSingleInstHook(void* origin, void* replace);

extern "C"
EXPORT void* SandSingleInstHookSym(const char* so, const char* symb, void* replace);

extern "C"
EXPORT bool SandBreakPoint(void *origin, void (*callback)(REG[]));

extern "C"
EXPORT bool SandSingleInstBreakPoint(void *origin, BreakCallback(callback));

#if defined(__aarch64__)

#include <asm/sigcontext.h>
extern "C"
EXPORT fpsimd_context* GetSimdContext(sigcontext *mcontext);

#endif