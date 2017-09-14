package com.selfimpr.map;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.tencent.mapsdk.raster.model.BitmapDescriptorFactory;
import com.tencent.mapsdk.raster.model.CameraPosition;
import com.tencent.mapsdk.raster.model.LatLng;
import com.tencent.mapsdk.raster.model.Marker;
import com.tencent.mapsdk.raster.model.MarkerOptions;
import com.tencent.tencentmap.mapsdk.map.MapView;
import com.tencent.tencentmap.mapsdk.map.TencentMap;
import com.tencent.tencentmap.mapsdk.map.UiSettings;

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
    private static final String LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    private double uLatitude = 39.987221;
    private double uLongitude = 116.505203;
    private boolean isMapLoaded = false; //地图是否加载完成
    private MapView mMapView;
    private TencentMap tencentMap;
    private FragmentActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (FragmentActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                if (isMapLoaded && mMapView != null) {
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

    private static boolean checkLocationPermission() {
        int perm = MApplication.getApplication().checkCallingOrSelfPermission(LOCATION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }
}
