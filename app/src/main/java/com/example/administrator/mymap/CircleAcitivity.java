package com.example.administrator.mymap;

import android.app.Activity;
import android.os.Bundle;

import com.amap.api.maps2d.model.Circle;

/**
 * Created by Administrator on 2015/12/22.
 */
public class CircleAcitivity extends BaseMapActivity {


    private Circle circle;

    @Override
    int getLayout() {
        return R.layout.activity_map_view;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpMap();
    }

    private void setUpMap() {


    }
}
