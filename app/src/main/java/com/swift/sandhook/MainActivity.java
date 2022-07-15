package com.swift.sandhook;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.swift.sandhook.test.Inter;
import com.swift.sandhook.test.InterImpl;
import com.swift.sandhook.test.PendingHookTest;
import com.swift.sandhook.test.TestClass;

import java.lang.reflect.Field;

import de.robv.android.xposed.XposedHelpers;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "SandHookTest";

    Inter inter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "MainActivity onCreate");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        StringBuilder hookTestResult = new StringBuilder();
        hookTestResult.append("静态方法Hook：").append(HookPass.getStaticMethodHookResult()).append("\r\n");
        hookTestResult.append("JNI方法Hook：").append(HookPass.getJniMethodHookResult()).append("\r\n");
        hookTestResult.append("App实例方法Hook：").append(HookPass.getAppMethodHookResult()).append("\r\n");
        hookTestResult.append("系统类实例方法Hook：").append(HookPass.getSystemMethodHookResult()).append("\r\n");
        hookTestResult.append("APP类构造方法Hook：").append(HookPass.getAppConstructorHookResult()).append("\r\n");
        hookTestResult.append("系统类构造方法Hook：").append(HookPass.getSystemConstructorHookResult()).append("\r\n");
        hookTestResult.append("实例方法Inline模式Hook：").append(HookPass.getInstanceMethodInlineResult()).append("\r\n");
        hookTestResult.append("实例方法Replace模式Hook：").append(HookPass.getInstanceMethodReplaceResult()).append("\r\n");
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(hookTestResult);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        inter = new Inter() {
            @Override
            public void dosth() {
                Log.e("dosth", hashCode() + "");
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            new TestClass(1).add2();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

