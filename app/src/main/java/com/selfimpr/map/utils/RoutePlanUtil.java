package com.selfimpr.map.utils;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.util.Log;

import com.selfimpr.map.MApplication;
import com.selfimpr.map.R;

import java.util.List;

/**
 * description：   <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2017/9/13 下午1:47<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class RoutePlanUtil {
    private static RoutePlanUtil instance;
    public static final String PACKAGE_BAI_DU = "com.baidu.BaiduMap";
    public static final String PACKAGE_GAO_DE = "com.autonavi.minimap";
    public static final String PACKAGE_TENCENT = "com.tencent.map";
    public static final String PACKAGE_GOOGLE = "com.google.android.apps.maps";

    public static final int CUSTOM_ROUTE_PLAN = 0;
    public static final int FLAG_BAIDU = 1;
    public static final int FLAG_GAODE = 2;
    public static final int FLAG_TENCENT = 3;
    public static final int FLAG_GOOGLE = 4;

    public static RoutePlanUtil newInstance() {
        if (instance == null) {
            instance = new RoutePlanUtil();
        }
        return instance;
    }

    public void startRoutePlan(int flag) {
        switch (flag) {
            case FLAG_BAIDU:
                goToBaiduMap("39.987045", "116.50489", "老王家", "39.980171", "116.44487", "隔壁");
                break;
            case FLAG_GAODE:
                goToAutonaviMap("39.987045", "116.50489", "老王家", "39.980171", "116.44487", "隔壁");
                break;
            case FLAG_TENCENT:
                geToTencentMap("39.987045", "116.50489", "老王家", "39.980171", "116.44487", "隔壁");
                break;
            case FLAG_GOOGLE:
                goToGoogleMap("39.987045", "116.50489", "老王家", "39.980171", "116.44487", "隔壁");
                break;
        }
    }

    /**
     * 高德地图 路线规划 http://lbs.amap.com/api/amap-mobile/guide/android/route
     *
     * @param slat  起点纬度。如果不填写此参数则自动将用户当前位置设为起点纬度。
     * @param slon  起点经度。如果不填写此参数则自动将用户当前位置设为起点经度。
     * @param sname 起点名称
     * @param dlat  终点纬度
     * @param dlon  终点名称
     * @param dname 终点名称
     */
    private void goToAutonaviMap(String slat, String slon, String sname, String dlat, String dlon, String dname) {
        try {
            String uri = "amapuri://route/plan/?slat=" + slat + "&slon=" + slon + "&sname=" + sname + "&dlat=" + dlat + "&dlon=" + dlon + "&dname=" + dname + "&dev=0&t=0";
            Intent intent = new Intent();
            intent.setData(Uri.parse(uri));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage(RoutePlanUtil.PACKAGE_GAO_DE);
            MApplication.getApplication().startActivity(intent);
        } catch (Exception e) {
            Log.e("wjc", e.toString());
        }
    }

    /**
     * 腾讯地图 路线规划 坐标先纬度，后经度
     *
     * @param slat  起点纬度。如果不填写此参数则自动将用户当前位置设为起点纬度。
     * @param slon  起点经度。如果不填写此参数则自动将用户当前位置设为起点经度。
     * @param sname 起点名称
     * @param dlat  终点纬度
     * @param dlon  终点经度
     * @param dname 终点名称
     */
    private void geToTencentMap(String slat, String slon, String sname, String dlat, String dlon, String dname) {  //这里 from 和 to 不能不写或者空白符
        try {
            String tencnetUri = "qqmap://map/routeplan?type=drive&from=" + sname + "&fromcoord=" + slat + "," + slon + "&to=" + dname + "&tocoord=" + dlat + "," + dlon + "&policy=0&referer=" + MApplication.getApplication().getResources().getString(R.string.app_name);
            Intent intent = new Intent();
            intent.setData(Uri.parse(tencnetUri));
            intent.setPackage(RoutePlanUtil.PACKAGE_TENCENT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MApplication.getApplication().startActivity(intent);
        } catch (Exception e) {
            Log.e("wjc", e.toString());
        }
    }

    /**
     * 百度地图 路线规划  http://lbsyun.baidu.com/index.php?title=uri/api/android
     *
     * @param slat  起点纬度。如果不填写此参数则自动将用户当前位置设为起点纬度。
     * @param slon  起点经度。如果不填写此参数则自动将用户当前位置设为起点经度。
     * @param sname 起点名称
     * @param dlat  终点纬度
     * @param dlon  终点经度
     * @param dname 终点名称
     */
    private void goToBaiduMap(String slat, String slon, String sname, String dlat, String dlon, String dname) {
        try {
            Intent intent = new Intent();
            // 公交路线规划
            intent.setData(Uri.parse("baidumap://map/direction?origin=name:" + sname + "|latlng:" + slat + "," + slon + "&destination=latlng:" + dlat + "," + dlon + "|name:" + dname + "&mode=driving&sy=5&index=0"));
            intent.setPackage(RoutePlanUtil.PACKAGE_BAI_DU);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MApplication.getApplication().startActivity(intent);
        } catch (Exception e) {
            Log.e("wjc", e.toString());
        }
    }

    /**
     * @param slat  起点纬度。如果不填写此参数则自动将用户当前位置设为起点纬度。
     * @param slon  起点经度。如果不填写此参数则自动将用户当前位置设为起点经度。
     * @param sname 起点名称
     * @param dlat  终点纬度
     * @param dlon  终点经度
     * @param dname 终点名称
     */
    private void goToGoogleMap(String slat, String slon, String sname, String dlat, String dlon, String dname) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d&saddr=" + slat + "," + slon + "&daddr=" + dlat + "," + dlon + "&hl=zh"));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setPackage(RoutePlanUtil.PACKAGE_TENCENT);
            MApplication.getApplication().startActivity(intent);
        } catch (Exception e) {
            Log.e("wjc", e.toString());
        }
    }

    public static boolean isInstalledBaidu() {
        Intent intent = new Intent();
        intent.setData(Uri.parse("baidumap://map/direction"));
        intent.setPackage(PACKAGE_BAI_DU);
        ResolveInfo resolveInfo = MApplication.getApplication().getPackageManager().resolveActivity(intent, 0);
        return resolveInfo != null;
    }

    public static boolean isInstalledGaode() {
        Intent intent = new Intent();
        intent.setData(Uri.parse("amapuri://route/plan/"));
        intent.setPackage(PACKAGE_GAO_DE);
        ResolveInfo resolveInfo = MApplication.getApplication().getPackageManager().resolveActivity(intent, 0);
        return resolveInfo != null;
    }

    public static boolean isInstalledTencent() {
        Intent intent = new Intent();
        intent.setData(Uri.parse("qqmap://map/routeplan"));
        intent.setPackage(PACKAGE_TENCENT);
        ResolveInfo resolveInfo = MApplication.getApplication().getPackageManager().resolveActivity(intent, 0);
        return resolveInfo != null;
    }

    public static boolean isInstalledGoogle() {
        Intent intent = new Intent();
        intent.setData(Uri.parse("http://ditu.google.cn/maps"));
        intent.setPackage(PACKAGE_GOOGLE);
        ResolveInfo resolveInfo = MApplication.getApplication().getPackageManager().resolveActivity(intent, 0);
        return resolveInfo != null;
    }

    /**
     * todo ：使用这种方式需要获取应用列表权限
     *
     * @param packageName：应用包名
     * @return 检查手机上是否安装了指定的软件, 有TRUE，没有FALSE
     */
    public static boolean isAvilible(String packageName) {
        //获取packagemanager
        final PackageManager packageManager = MApplication.getApplication().getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        if (packageInfos != null && packageInfos.size() > 0) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                if (packageName.equals(packName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
