package com.selfimpr.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;
import com.tencent.map.geolocation.TencentPoi;
import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.CameraPosition;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.tencent.tencentmap.mapsdk.map.UiSettings;

import java.util.List;

/**
 * description：map相关
 * ===============================<br/>
 * creator：Jiacheng<br/>
 * create time：2017/9/13 下午2:27<br/>
 * ===============================<br/>
 * reasons for modification：  <br/>
 * Modifier：  <br/>
 * Modify time：  <br/>
 */
public class MapFragment extends Fragment {
    public static final int TENCENT_MAP_ZOOM = 16;
    private double uLatitude = 39.987221;
    private double uLongitude = 116.505203;
    private boolean isMapLoaded = false; //地图是否加载完成
    private MapView mMapView;
    private TencentMap tencentMap;
    private FragmentActivity activity;

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (FragmentActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissions = {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            if (activity.checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(permissions, 0);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        FrameLayout layoutContainer = (FrameLayout) rootView.findViewById(R.id.container);
        try {
            initTencentMap(layoutContainer);
        } catch (Exception e) {
            Toast.makeText(activity, "地图加载失败", Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    private void obtainLocalPosition() {
        TencentLocationManager mLocationManager = TencentLocationManager.getInstance(activity);
        // 设置坐标系为 gcj-02, 缺省坐标为 gcj-02, 所以通常不必进行如下调用
        //mLocationManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_GCJ02);
        // 创建定位请求
        TencentLocationRequest request = TencentLocationRequest.create()
                // 当定位周期大于0时, 不论是否有得到新的定位结果, 位置监听器都会按定位周期定时被回调;
                // 当定位周期等于0时, 仅当有新的定位结果时, 位置监听器才会被回调(即, 回调时机存在不确定性).
                // 如果需要周期性回调, 建议将 定位周期 设置为 5000-10000ms
                .setInterval(5 * 1000) // 设置定位周期(位置监听器回调周期), 单位为 ms (毫秒).
//                .setAllowGPS(true)  //设置是否允许使用GPS进行定位,默认允许. 如果允许使用GPS，在室外可提升定位精度（约3~10米），GPS首次获取位置较慢且耗电较高
                //.setQQ("1281315018")
                //不同的 request level 得到的定位结果信息完整程度不同. 通常, 定位结果中所含信息越多消耗的流量也越多, 请选择合理的 request level 以节省流量.
//                .setRequestLevel(mLevel) // 设置定位level
                ;
        // 开始定位
        mLocationManager.requestLocationUpdates(request, new TencentLocationListener() {
            @Override
            public void onLocationChanged(TencentLocation location, int error, String reason) {
                String msg;
                if (error == TencentLocation.ERROR_OK) {
                    // 定位成功
                    msg = MapFragment.toString(location, mLevel);
                } else {
                    // 定位失败
                    msg = "定位失败: " + reason;
                }
                Log.e("wjc", "onLocationChanged:" + msg);
            }

            @Override
            public void onStatusUpdate(String s, int i, String s1) {
                Log.e("wjc", "onStatusUpdate:" + s);
            }
        }, Looper.getMainLooper());
    }

    private void initTencentMap(FrameLayout layoutContainer) {
        mMapView = new MapView(activity);
        layoutContainer.addView(mMapView);
        //去除腾讯地图的logo
        mMapView.removeViewAt(2);
        //获取TencentMap实例
        tencentMap = mMapView.getMap();
        //设置地图中心点
        initMapCenter();
        //获取UiSettings实例
        UiSettings uiSettings = mMapView.getUiSettings();
        //显示或隐藏比例尺
        uiSettings.setScaleControlsEnabled(false);
        //地图缩放、平移动画开关
        uiSettings.setAnimationEnabled(true);
        //地图加载完成回调:地图显示范围等涉及到地图初始化问题的状态需要在地图加载完成后才能正常获取
        tencentMap.setOnMapLoadedListener(new TencentMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                Log.e("wjc", "onMapLoaded:地图加载完成！");
                isMapLoaded = true;
            }
        });
        //地图视图变化回调：map移动／放大／缩小监听
        tencentMap.setOnMapCameraChangeListener(new TencentMap.OnMapCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                LatLng latLng = cameraPosition.getTarget();
                if (isMapLoaded) {
                    Log.e("wjc", "onCameraChangeFinish-->lat:" + latLng.getLatitude() + ",lng:" + latLng.getLongitude());
                }
            }
        });
    }

    private void initMapCenter() {
        LatLng latLng = new LatLng(uLatitude, uLongitude);
        tencentMap.setCenter(latLng);
        tencentMap.addMarker(new MarkerOptions()
                .position(latLng)
                .anchor(0.53f, 0.6f)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_current_location))
                .draggable(false));
        //如果不加这行代码，点击marker的时候，会弹一个气泡，很奇怪
        tencentMap.setOnMarkerClickListener(new TencentMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(activity, "click marker", Toast.LENGTH_SHORT).show();
                obtainLocalPosition();
                return false;
            }
        });
        //设置缩放级别
        tencentMap.setZoom(TENCENT_MAP_ZOOM);
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //可在此继续其他操作。
        Log.e("wjc", "onRequestPermissionsResult");
        obtainLocalPosition();
    }
}
