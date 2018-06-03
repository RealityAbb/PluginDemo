package com.example.plugindemo;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, PluginActivity.class);
                intent.putExtra(Plugin.PLUGIN, true);
                intent.putExtra(Plugin.PACKAGENAME, MainActivity.this.getPackageName());
                intent.putExtra(Plugin.CLASSNAME, PluginActivity.class.getName());
                startActivity(intent);
            }
        });
    }

    private static boolean hooked = false;


    public MainActivity() {
        super();
        if (!hooked) {
            hooked = true;
            hook();
        }

    }


    void hook() {
        try {
            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            currentActivityThreadMethod.setAccessible(true);
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);

            // 拿到原始的 mInstrumentation字段
            Field mInstrumentationField = activityThreadClass.getDeclaredField("mInstrumentation");
            mInstrumentationField.setAccessible(true);
            Instrumentation mInstrumentation = (Instrumentation) mInstrumentationField.get(currentActivityThread);

            //如果没有注入过，就执行替换
            if (!(mInstrumentation instanceof PluginInstrumentation)) {
                PluginInstrumentation pluginInstrumentation = new PluginInstrumentation(mInstrumentation);
                mInstrumentationField.set(currentActivityThread, pluginInstrumentation);
            }
        } catch (Exception e) {

        }
    }

}
