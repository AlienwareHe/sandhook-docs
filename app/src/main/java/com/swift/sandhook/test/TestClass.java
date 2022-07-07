package com.swift.sandhook.test;

import android.util.Log;

import com.swift.sandhook.LogTags;
import com.swift.sandhook.MainActivity;

public class TestClass {

    public int a = 1;
    int b = 2;

    public TestClass(int a) {
        this.a = a + 1;
    }

    public void add1 () {
        a++;
        b++;
        throw new RuntimeException("test exception");
    }

    public void add2 () {
        a++;
        b++;
        Log.i(LogTags.ORIGIN,"add2 result:" + (a+b));
    }

    public void testNewHookApi (MainActivity activity, int x) {
        x++;
        a++;
        b++;
    }

    public native void jni_test();

}
