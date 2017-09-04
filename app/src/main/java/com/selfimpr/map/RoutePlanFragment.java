package com.selfimpr.map;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:启动第三方地图app进行"路线规划"
 * Created by Jiacheng on 2017/8/29.
 */
public class RoutePlanFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_route_plan, container, false);
        rootView.findViewById(R.id.open_baidu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAvilible("com.baidu.BaiduMap")) {
                    goToBaiduMap("39.987045", "116.50489", "老王家", "39.980171", "116.44487", "隔壁");
                } else {
                    Toast.makeText(getActivity(), "未安装百度地图", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rootView.findViewById(R.id.open_gaode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAvilible("com.autonavi.minimap")) {
                    goToAutonaviMap("39.987045", "116.50489", "老王家", "39.980171", "116.44487", "隔壁");
                } else {
                    Toast.makeText(getActivity(), "未安装高德地图", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rootView.findViewById(R.id.open_tencent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAvilible("com.tencent.map")) {
                    geToTencentMap("39.987045", "116.50489", "老王家", "39.980171", "116.44487", "隔壁");
                } else {
                    Toast.makeText(getActivity(), "未安装腾讯地图", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rootView.findViewById(R.id.open_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAvilible("com.google.android.apps.maps")) {
                    goToGoogleMap("39.987045", "116.50489", "老王家", "39.980171", "116.44487", "隔壁");
                } else {
                    Toast.makeText(getActivity(), "未安装Google地图", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
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
            intent.setPackage("com.autonavi.minimap");
            startActivity(intent);
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
            String tencnetUri = "qqmap://map/routeplan?type=drive&from=" + sname + "&fromcoord=" + slat + "," + slon + "&to=" + dname + "&tocoord=" + dlat + "," + dlon + "&policy=0&referer=" + getResources().getString(R.string.app_name);
            Intent intent = new Intent();
            intent.setData(Uri.parse(tencnetUri));
            intent.setPackage("com.tencent.map");
            startActivity(intent);
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
            intent.setPackage("com.baidu.BaiduMap");
            startActivity(intent);
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
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d&saddr=" + slat + "," + slon + "&daddr=" + dlat + "," + dlon + "&hl=zh"));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            i.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(i);
        } catch (Exception e) {
            Log.e("wjc", e.toString());
        }
    }

    /**
     * @param packageName：应用包名
     * @return 检查手机上是否安装了指定的软件, 有TRUE，没有FALSE
     */
    public boolean isAvilible(String packageName) {
        if (getActivity() == null) {
            return false;
        }
        //获取packagemanager
        final PackageManager packageManager = getActivity().getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if (packageInfos != null && packageInfos.size() > 0) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }
}
