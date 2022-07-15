package com.swift.sandhook.testHookers;

import android.util.Log;

import com.swift.sandhook.LogTags;
import com.swift.sandhook.SandHook;
import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.HookMode;
import com.swift.sandhook.annotation.SkipParamCheck;
import com.swift.sandhook.annotation.ThisObject;
import com.swift.sandhook.test.TestClass;

import java.lang.reflect.Method;

@HookClass(TestClass.class)
public class CtrHook {

    @HookMethodBackup("add1")
    @SkipParamCheck
    static Method add1backup;

    @HookMethodBackup("add2")
    @SkipParamCheck
    static Method add2backup;


    @HookMethod("add1")
    @HookMode(HookMode.INLINE)
    public static void onAdd1(TestClass thiz) throws Throwable {
        Log.e(LogTags.HOOK_IN, "[4] ObjectMethod inline mode hook success");
        try {
            SandHook.callOriginByBackup(add1backup, thiz);
            Log.e(LogTags.HOOK_IN, "[20]ObjectMethod inline mode call origin success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @HookMethod("add2")
    public static void onAdd2(TestClass thiz) throws  Throwable {
        Log.e(LogTags.HOOK_IN, "[18]ObjectMethod default mode hook success");
        SandHook.callOriginByBackup(add2backup, thiz);
        Log.e(LogTags.HOOK_IN, "[19]ObjectMethod default mode call origin success");
    }



}
