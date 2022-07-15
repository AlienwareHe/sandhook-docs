package com.swift.sandhook;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.swift.sandhook.testHookers.ActivityHooker;
import com.swift.sandhook.testHookers.CtrHook;
import com.swift.sandhook.testHookers.LogHooker;
import com.swift.sandhook.wrapper.HookErrorException;
import com.swift.sandhook.xposedcompat.XposedCompat;

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
            SandHookConfig.delayHook = false;

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
                SandHook.addHookClass(
                        CtrHook.class,
                        LogHooker.class,
                        ActivityHooker.class);
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

            HookPass.init();
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
