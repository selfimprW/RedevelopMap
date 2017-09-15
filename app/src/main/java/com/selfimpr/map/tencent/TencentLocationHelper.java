package com.selfimpr.map.tencent;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.selfimpr.map.MApplication;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.map.geolocation.TencentPoi;

import java.util.List;

/**
 * description：   <br/>
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2017/9/15 下午1:23<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class TencentLocationHelper implements TencentLocationListener {
    private volatile static TencentLocationHelper locationHelper; //使用 volatile 的主要原因是其一个特性：禁止指令重排序优化

    private static final int[] LEVELS = new int[]{
            //定位结果信息级别: 0号定位接口, 仅包含经纬度坐标表示的地位置(经纬度).
            TencentLocationRequest.REQUEST_LEVEL_GEO,
            //定位结果信息级别: 1号定位接口, 包含经纬度, 位置名称, 位置地址.
            TencentLocationRequest.REQUEST_LEVEL_NAME,
            //定位结果信息级别: 3号定位接口, 包含经纬度, 行政区划，位置地址和位置名称.
            TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA,
            //定位结果信息级别: 4号定位接口, 包含经纬度, 行政区划, 附近的POI.
            TencentLocationRequest.REQUEST_LEVEL_POI};
    private static final int DEFAULT = 2;
    private int mLevel = LEVELS[DEFAULT];
    private TencentLocationManager mLocationManager;
    private ObtainLocationInfoListener obtainLocationInfoListener;

    public static TencentLocationHelper getLocationHelper() {
        if (locationHelper == null) {               //Single Checked
            synchronized (TencentLocationHelper.class) {
                if (locationHelper == null) {       //Double Checked
                    locationHelper = new TencentLocationHelper();
                }
            }
        }
        return locationHelper;
    }

    public void refreshLocation(ObtainLocationInfoListener obtainLocationInfoListener) {
        this.obtainLocationInfoListener = obtainLocationInfoListener;
        mLocationManager = TencentLocationManager.getInstance(MApplication.getApplication());
        // 立即启动高精度室内定位. 启动后，当所在的楼宇支持腾讯的高精度室内定位时，会默认切换到室内定位，离开时自动切换回原定位方式
        mLocationManager.startIndoorLocation();
        // 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
        //mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        // 创建定位请求
        TencentLocationRequest request = TencentLocationRequest.create()
                // 当定位周期大于0时, 不论是否有得到新的定位结果, 位置监听器都会按定位周期定时被回调;
                // 当定位周期等于0时, 仅当有新的定位结果时, 位置监听器才会被回调(即, 回调时机存在不确定性).
                // 如果需要周期性回调, 建议将 定位周期 设置为 5000-10000ms
                .setInterval(0) // 设置定位周期(位置监听器回调周期), 单位为 ms (毫秒).
                .setAllowGPS(true)  //设置是否允许使用GPS进行定位,默认允许. 如果允许使用GPS，在室外可提升定位精度（约3~10米），GPS首次获取位置较慢且耗电较高
                //.setQQ("1281315018")
                //不同的 request level 得到的定位结果信息完整程度不同. 通常, 定位结果中所含信息越多消耗的流量也越多, 请选择合理的 request level 以节省流量.
                .setRequestLevel(mLevel) // 设置定位level
                ;
        //开始定位 :强烈建议在主线程中调用本方法, 定位完成后(无论定位成功或失败)都应尽快移除 listener, 否则可能不必要地消耗较多电量
        //由于回调方法在新线程中调用, 回调方法中*不应*执行某些必须在主线程中完成的操作, 比如更新UI.
        // TODO: 2017/9/15 0-成功注册监听器, 1-设备缺少使用腾讯定位服务需要的基本条件, 2-manifest 中配置的 key 不正确, 3-自动加载libtencentloc.so失败
        int status = mLocationManager.requestLocationUpdates(request, this, Looper.getMainLooper());
        Log.e("wjc", "requestLocationUpdates:" + status);
    }

    @Override
    public void onLocationChanged(final TencentLocation location, final int error, final String reason) {
        mLocationManager.removeUpdates(this);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                String msg;
                if (error == TencentLocation.ERROR_OK) {
                    // 定位成功
                    msg = TencentLocationHelper.toString(location, mLevel);
                    if (obtainLocationInfoListener != null) {
                        obtainLocationInfoListener.getLocation(location);
                    }
                } else {
                    // 定位失败
                    msg = "定位失败: " + reason;
                    if (obtainLocationInfoListener != null) {
                        obtainLocationInfoListener.locationFail(error, reason);
                    }
                }
                Log.e("wjc", "onLocationChanged:" + msg);
            }
        });
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }

    /**
     * 获取位置后回调
     */
    public interface ObtainLocationInfoListener {
        void getLocation(TencentLocation location);

        void locationFail(int error, String reason);
    }

    // ===== util method
    private static String toString(TencentLocation location, int level) {
        StringBuilder sb = new StringBuilder();
        sb.append("latitude=").append(location.getLatitude()).append(",");
        sb.append("longitude=").append(location.getLongitude()).append(",");
        sb.append("altitude=").append(location.getAltitude()).append(",");
        sb.append("accuracy=").append(location.getAccuracy()).append(",");

        switch (level) {
            case TencentLocationRequest.REQUEST_LEVEL_GEO:
                break;
            case TencentLocationRequest.REQUEST_LEVEL_NAME:
                sb.append("name=").append(location.getName()).append(",");
                sb.append("address=").append(location.getAddress()).append(",");
                break;
            case TencentLocationRequest.REQUEST_LEVEL_ADMIN_AREA:
            case TencentLocationRequest.REQUEST_LEVEL_POI:
            case 7:
                sb.append("nation=").append(location.getNation()).append(",");
                sb.append("province=").append(location.getProvince()).append(",");
                sb.append("city=").append(location.getCity()).append(",");
                sb.append("district=").append(location.getDistrict()).append(",");
                sb.append("town=").append(location.getTown()).append(",");
                sb.append("village=").append(location.getVillage()).append(",");
                sb.append("street=").append(location.getStreet()).append(",");
                sb.append("streetNo=").append(location.getStreetNo()).append(",");

                if (level == TencentLocationRequest.REQUEST_LEVEL_POI) {
                    List<TencentPoi> poiList = location.getPoiList();
                    int size = poiList.size();
                    for (int i = 0, limit = 3; i < limit && i < size; i++) {
                        sb.append("\n").append("poi[" + i + "]=").append(toString(poiList.get(i))).append(",");
                    }
                }
                break;
            default:
                break;
        }
        return sb.toString();
    }

    private static String toString(TencentPoi poi) {
        StringBuilder sb = new StringBuilder();
        sb.append("name=").append(poi.getName()).append(",");
        sb.append("address=").append(poi.getAddress()).append(",");
        sb.append("catalog=").append(poi.getCatalog()).append(",");
        sb.append("distance=").append(poi.getDistance()).append(",");
        sb.append("latitude=").append(poi.getLatitude()).append(",");
        sb.append("longitude=").append(poi.getLongitude()).append(",");
        return sb.toString();
    }
}
