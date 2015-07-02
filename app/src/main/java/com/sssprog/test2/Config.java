package com.sssprog.test2;

import android.os.Build;

public class Config {

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

}
