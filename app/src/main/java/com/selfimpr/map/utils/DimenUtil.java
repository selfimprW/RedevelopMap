package com.selfimpr.map.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;


/**
 * description：   <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2017/9/15 下午3:03<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class DimenUtil {
    /**
     * @return 获取屏幕宽度（像素）
     */
    public static int getDisplayWidth(Context ctx) {
        DisplayMetrics metric = new DisplayMetrics();
        if (ctx != null) {
            WindowManager winManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
            winManager.getDefaultDisplay().getMetrics(metric);
        }
        return metric.widthPixels;
    }
}
