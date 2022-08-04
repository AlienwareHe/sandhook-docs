package com.swift.sandhook;

import android.app.Activity;
import android.util.Log;

import com.swift.sandhook.test.PendingHookTest;
import com.swift.sandhook.test.TestClass;

import java.util.Random;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

/**
 * @author alienhe
 * @date 2022/8/4
 * @Description
 */
public class HookPass {

    private static boolean isSystemConstructorHooked = false;
    private static boolean isAppConstructorHooked = false;

    public static void init() {
        XposedHelpers.findAndHookMethod(PendingHookTest.class, "test", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.i(LogTags.HOOK_IN, "[12] StaticMethod pendingHookTest hooked success before");
                super.beforeHookedMethod(param);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Log.i(LogTags.HOOK_IN, "[13] StaticMethod pendingHookTest hooked success after");
                param.setResult(true);
            }
        });

        XposedHelpers.findAndHookMethod(Activity.class, "onResume", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Log.e(LogTags.HOOK_IN, "[8] Activity onResume beforeHookedMethod: " + param.method.getName());
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.e(LogTags.HOOK_IN, "[9] Activity onResume afterHookedMethod: " + param.method.getName());
            }
        });

        XposedHelpers.findAndHookMethod(TestClass.class, "testStub", int.class,new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                param.args[0] = 2;
                Log.e(LogTags.HOOK_IN, "[10] MainActivity#testStub beforeHookedMethod: " + param.method.getName());
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.e(LogTags.HOOK_IN, "[11] MainActivity#testStub afterHookedMethod: " + param.method.getName());
            }
        });

        XposedHelpers.findAndHookMethod(Random.class, "nextInt", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(1);
            }
        });

        XposedBridge.hookAllConstructors(Thread.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.i(LogTags.HOOK_IN, "[14] SystemConstructor Thread hooked success before:" + param.method);
                super.beforeHookedMethod(param);
                isSystemConstructorHooked = true;
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.i(LogTags.HOOK_IN, "[15] SystemConstructor Thread hooked success after:" + param.method);
            }
        });

        XposedBridge.hookAllConstructors(TestClass.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.i(LogTags.HOOK_IN, "[16] Constructor TestClass hooked success before:" + param.method);
                super.beforeHookedMethod(param);
                isAppConstructorHooked = true;
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Log.i(LogTags.HOOK_IN, "[17] Constructor TestClass hooked success after:" + param.method);
            }
        });

        XposedHelpers.findAndHookMethod(NativeHookTest.class, "exec", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                param.setResult(true);
            }
        });
    }

    public static boolean getStaticMethodHookResult() {
        try {
            return PendingHookTest.test();
        } catch (Throwable e) {
            return false;
        }
    }

    public static boolean getJniMethodHookResult() {
        return NativeHookTest.exec();
    }

    public static boolean getAppMethodHookResult() {
        return new TestClass().testStub(1) != 1;
    }

    public static boolean getSystemMethodHookResult() {
        return new Random().nextInt() == 1;
    }

    public static boolean getAppConstructorHookResult(){
        return isAppConstructorHooked;
    }

    public static boolean getSystemConstructorHookResult(){
        return isSystemConstructorHooked;
    }

    public static boolean getInstanceMethodInlineResult() {
        return new TestClass().add1();
    }

    public static boolean getInstanceMethodReplaceResult() {
        return new TestClass().add2();
    }
}
