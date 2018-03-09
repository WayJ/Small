package net.wequick.small.util;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;

import net.wequick.small.R;
import net.wequick.small.Small;

import java.lang.reflect.Field;


public class ResourceUtils {

    public static int findThemeResId(Activity activity) {
        int themeId = 0;
        try {
            Field f = ContextThemeWrapper.class.getDeclaredField("mThemeResource");
            f.setAccessible(true);
            themeId = (int) f.get(activity);
        } catch (Exception ignored) {
        }
        return themeId;
    }


    public static void replaceThemeV7(Activity activity) {
        if(!(activity instanceof AppCompatActivity))
            return;

        Resources.Theme theme = Small.getContext().getResources().newTheme();
        theme.applyStyle(findThemeResId(activity), true);

        Field mThemeField = null;
        Class clazz = activity.getClass();
        findTheme:
        do {
            try {
                mThemeField = clazz.getDeclaredField("mTheme");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                clazz = clazz.getSuperclass();
                if (clazz.getName().contains("Object"))
                    break findTheme;
            }
        } while (mThemeField == null);
        mThemeField.setAccessible(true);
        try {
            mThemeField.set(activity, theme);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void replaceResources(Activity activity) {
        Field resField = null;
        Class clazz = activity.getClass();
        findResource:
        do {
            try {
                resField = clazz.getDeclaredField("mResources");
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                clazz = clazz.getSuperclass();
                if (clazz.getName().contains("Object"))
                    break findResource;
            }
        } while (resField == null);
        resField.setAccessible(true);
        try {
            resField.set(activity, Small.getContext().getResources());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
