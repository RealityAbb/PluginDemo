package com.example.plugindemo;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import java.lang.reflect.Method;

/**
 * Created by reality on 2018/6/3.
 */

public class PluginInstrumentation extends Instrumentation {
    private Instrumentation mBase;
    public PluginInstrumentation(Instrumentation base) {
        mBase = base;
    }
    @Override
    public Activity newActivity(ClassLoader cl, String className,
                                Intent intent)
            throws InstantiationException, IllegalAccessException,
            ClassNotFoundException {
        if (intent.getBooleanExtra(Plugin.PLUGIN, false)) {
            String packageName = intent.getStringExtra(Plugin.PACKAGENAME);
            className = intent.getStringExtra(Plugin.CLASSNAME);
            intent.setComponent(new ComponentName(packageName,className));
        }
        return super.newActivity(cl, className, intent);
    }
    public ActivityResult execStartActivity(
            Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) {
        if (intent.getBooleanExtra(Plugin.PLUGIN, false)){
            String packageName = intent.getStringExtra(Plugin.PACKAGENAME);
            String className = intent.getStringExtra(Plugin.CLASSNAME);
            ComponentName componentName = new ComponentName(packageName, SubActivity.class.getName());
            intent.setComponent(componentName);
        }
        try {
            // 由于这个方法是隐藏的,因此需要使用反射调用;首先找到这个方法
            Method execStartActivity = Instrumentation.class.getDeclaredMethod(
                    "execStartActivity", Context.class, IBinder.class, IBinder.class,
                    Activity.class, Intent.class, int.class, Bundle.class);
            execStartActivity.setAccessible(true);
            return (ActivityResult) execStartActivity.invoke(mBase, who,
                    contextThread, token, target, intent, requestCode, options);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("do not support!!!" + e.getMessage());
        }
    }

}
