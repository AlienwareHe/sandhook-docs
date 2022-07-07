package com.swift.sandhook.test;

import android.util.Log;

import com.swift.sandhook.LogTags;
import com.swift.sandhook.MyApp;

public class PendingHookTest {

    static {
        if (!MyApp.initedTest) {
            throw new RuntimeException("PendingHookTest.class may can not init this time!");
        }
    }

    public static void test() {
        Log.e(LogTags.ORIGIN, "hook failure!");
    }

}
