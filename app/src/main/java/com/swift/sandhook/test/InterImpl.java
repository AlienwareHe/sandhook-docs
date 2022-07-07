package com.swift.sandhook.test;

import android.util.Log;

import com.swift.sandhook.LogTags;

public class InterImpl implements Inter {
    @Override
    public void dosth() {
        Log.e(LogTags.ORIGIN, "sasa");
    }
}
