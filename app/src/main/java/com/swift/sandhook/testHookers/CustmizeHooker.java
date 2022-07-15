package com.swift.sandhook.testHookers;

import android.util.Log;

import com.swift.sandhook.LogTags;
import com.swift.sandhook.MainActivity;
import com.swift.sandhook.annotation.HookClass;
import com.swift.sandhook.annotation.HookMethod;
import com.swift.sandhook.annotation.HookMethodBackup;
import com.swift.sandhook.annotation.MethodParams;

import java.lang.reflect.Method;

@HookClass(MainActivity.class)
public class CustmizeHooker {

    @HookMethodBackup("methodBeHooked")
    @MethodParams({int.class, int.class})
    static Method backup;

    @HookMethod("methodBeHooked")
    @MethodParams({int.class, int.class})
    public static int staticMethodHooked(int a, int b) {
        Log.e(LogTags.HOOK_IN, "[3] StaticMethod methodBeHooked hook success");
        try {
            return (int) backup.invoke(null, a, b);
        } catch (Exception e) {
            Log.e(LogTags.HOOK_IN, "StaticMethod methodBeHooked call origin failed:", e);
        }
        return 0;
    }

}
