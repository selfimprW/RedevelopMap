package com.selfimpr.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
                if (RoutePlanUtil.isInstalledBaidu()) {
                    RoutePlanUtil.newInstance().startRoutePlan(RoutePlanUtil.FLAG_BAIDU);
                } else {
                    Toast.makeText(getActivity(), "未安装百度地图", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rootView.findViewById(R.id.open_gaode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RoutePlanUtil.isInstalledGaode()) {
                    RoutePlanUtil.newInstance().startRoutePlan(RoutePlanUtil.FLAG_GAODE);
                } else {
                    Toast.makeText(getActivity(), "未安装高德地图", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rootView.findViewById(R.id.open_tencent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RoutePlanUtil.isInstalledTencent()) {
                    RoutePlanUtil.newInstance().startRoutePlan(RoutePlanUtil.FLAG_TENCENT);
                } else {
                    Toast.makeText(getActivity(), "未安装腾讯地图", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rootView.findViewById(R.id.open_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RoutePlanUtil.isInstalledGoogle()) {
                    RoutePlanUtil.newInstance().startRoutePlan(RoutePlanUtil.FLAG_GOOGLE);
                } else {
                    Toast.makeText(getActivity(), "未安装Google地图", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }


}
