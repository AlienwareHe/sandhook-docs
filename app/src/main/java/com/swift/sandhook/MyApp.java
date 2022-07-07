package com.swift.sandhook;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.swift.sandhook.annotation.HookMode;
import com.swift.sandhook.test.PendingHookTest;
import com.swift.sandhook.test.TestClass;
import com.swift.sandhook.testHookers.ActivityHooker;
import com.swift.sandhook.testHookers.CtrHook;
import com.swift.sandhook.testHookers.CustmizeHooker;
import com.swift.sandhook.testHookers.JniHooker;
import com.swift.sandhook.testHookers.LogHooker;
import com.swift.sandhook.testHookers.NewAnnotationApiHooker;
import com.swift.sandhook.testHookers.ObjectHooker;
import com.swift.sandhook.wrapper.HookErrorException;
import com.swift.sandhook.xposedcompat.XposedCompat;

import java.lang.reflect.Member;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public class MyApp extends Application {

    static {
        System.loadLibrary("app");
    }

    //for test pending hook case
    public volatile static boolean initedTest = false;

    @Override
    public void onCreate() {
        super.onCreate();
        try {

            NativeHookTest.hook();

            SandHookConfig.DEBUG = true;
            SandHookConfig.delayHook =false;

            Log.i("SandHook", "current sdk int:" + Build.VERSION.SDK_INT + ",preview sdk int:" + getPreviewSDKInt());
            if (Build.VERSION.SDK_INT == 29 && getPreviewSDKInt() > 0) {
                // Android R preview
                SandHookConfig.SDK_INT = 30;
            }

            SandHook.disableVMInline();
            SandHook.tryDisableProfile(getPackageName());
            SandHook.disableDex2oatInline(false);
            SandHook.forbidUseNterp();

            if (SandHookConfig.SDK_INT >= Build.VERSION_CODES.P) {
                SandHook.passApiCheck();
            }

            try {
                SandHook.addHookClass(JniHooker.class,
                        CtrHook.class,
                        LogHooker.class,
                        CustmizeHooker.class,
                        ActivityHooker.class,
                        ObjectHooker.class,
                        NewAnnotationApiHooker.class);
            } catch (HookErrorException e) {
                e.printStackTrace();
            }

            //for xposed compat(no need xposed comapt new)
            XposedCompat.cacheDir = getCacheDir();

            //for load xp module(sandvxp)
            XposedCompat.context = this;
            XposedCompat.classLoader = getClassLoader();
            XposedCompat.isFirstApplication = true;
            XposedCompat.useInternalStub = true;

            XposedHelpers.findAndHookMethod(Activity.class, "onResume", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    Log.e(LogTags.HOOK_IN, "Activity onResume beforeHookedMethod: " + param.method.getName());
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Log.e(LogTags.HOOK_IN, "Activity onResume afterHookedMethod: " + param.method.getName());
                }
            });


            XposedHelpers.findAndHookMethod(MainActivity.class, "testStub", TestClass.class, int.class, String.class, boolean.class, char.class, String.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    param.args[1] = 2;
                    Log.e(LogTags.HOOK_IN, "MainActivity#testStub beforeHookedMethod: " + param.method.getName());
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Log.e(LogTags.HOOK_IN, "MainActivity#testStub afterHookedMethod: " + param.method.getName());
                }
            });

            XposedHelpers.findAndHookMethod(PendingHookTest.class, "test", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Log.i(LogTags.HOOK_IN, "pendingTest hooked before");
                    super.beforeHookedMethod(param);
                    param.returnEarly = true;
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                }
            });

            XposedBridge.hookAllConstructors(Thread.class, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Log.i(LogTags.HOOK_IN, "thread constructor hooked before:" + param.method);
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Log.i(LogTags.HOOK_IN, "thread constructor hooked after:" + param.method);
                }
            });
        } catch (Throwable e) {
            Log.e("SandHook", "init sandhook test error:", e);
        }
    }

    public static int getPreviewSDKInt() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                return Build.VERSION.PREVIEW_SDK_INT;
            } catch (Throwable e) {
                // ignore
            }
        }
        return 0;
    }
}
