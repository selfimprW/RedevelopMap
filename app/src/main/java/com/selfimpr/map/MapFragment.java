package com.selfimpr.map;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
    private static final String LOCATION = "android.permission.ACCESS_COARSE_LOCATION";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        Toast.makeText(getActivity(), checkLocationPermission() ? "有位置权限" : "无位置权限", Toast.LENGTH_SHORT).show();
        return rootView;
    }


    private static boolean checkLocationPermission() {
        int perm = MApplication.getApplication().checkCallingOrSelfPermission(LOCATION);
        return perm == PackageManager.PERMISSION_GRANTED;
    }
}
