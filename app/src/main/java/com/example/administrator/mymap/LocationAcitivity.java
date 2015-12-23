package com.example.administrator.mymap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.CircleOptions;
import com.amap.api.maps2d.model.GroundOverlayOptions;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import butterknife.Bind;

/**
 * Created by Administrator on 2015/12/22.
 */
public class LocationAcitivity extends BaseMapActivity implements LocationSource, AMapLocationListener {


    private OnLocationChangedListener mListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationClientOption;
    private double latitude = -1000, longtitude = -1000;
    private Circle circle;
    private final double eps = 1e-8;
    @Bind(R.id.Latlng)
    TextView location;
    @Bind(R.id.activity_main_image_view)
    ImageView imageView;

    @Override
    int getLayout() {
        return R.layout.activity_map_view;
    }

    private int fabs(double val) {
        if (val < -eps) {
            return -1;
        }
        return val > eps ? 1 : 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpMap();
    }


    private void setUpMap() {

        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.location_marker));
        myLocationStyle.strokeColor(Color.BLACK);
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));
        myLocationStyle.strokeWidth(1.0f);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);
    }


    @Override
    protected void onPause() {
        super.onPause();
        deactivate();
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);
                double lat = aMapLocation.getLatitude();
                double lon = aMapLocation.getLongitude();
                if (fabs(lat - latitude) != 0 || fabs(lon - longtitude) != 0) {
                    latitude = lat;
                    longtitude = lon;
                    LatLng latLng = new LatLng(latitude + 0.5, longtitude);
                    drawCircle(latitude, longtitude);
                    drawCircle(latitude - 1, longtitude);
                    drawCircle(latitude + 1, longtitude);
                    drawCircle(latitude, longtitude + 1);
                    drawCircle(latitude, longtitude - 1);
                    drawMarker(latitude, longtitude + 0.3);
                    addGroundOverlay(latLng);
                }
                location.setText(String.format("%f %f", latitude, longtitude));
            } else {
                String errText = "locate error " + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }

    }

    private void addGroundOverlay(LatLng latLng) {
//        CircleOptions circleOptions = new CircleOptions().center(latLng)
//                .radius(4000).strokeColor(Color.argb(50, 1, 1, 1))
//                .fillColor(Color.argb(50, 1, 1, 1)).strokeWidth(25);

        LatLngBounds latLngBounds = new LatLngBounds.Builder().include(latLng).build();
        // aMap.addCircle(circleOptions);
        // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom()
        GroundOverlayOptions groundOverlayOptions = new GroundOverlayOptions().anchor(0.5f, 0.5f).
                image(BitmapDescriptorFactory.fromResource(R.mipmap.badge_sa))
                .positionFromBounds(latLngBounds);
        aMap.addGroundOverlay(groundOverlayOptions);

    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this);
            mLocationClientOption = new AMapLocationClientOption();
            mLocationClient.setLocationListener(this);
            mLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationClient.setLocationOption(mLocationClientOption);
            mLocationClient.startLocation();
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    private void drawCircle(double latitude, double longtitude) {
        LatLng latLng = new LatLng(latitude, longtitude);
        CircleOptions circleOptions = new CircleOptions().center(latLng)
                .radius(4000).strokeColor(Color.argb(50, 1, 1, 1))
                .fillColor(Color.argb(50, 1, 1, 1)).strokeWidth(25);
        circle = aMap.addCircle(circleOptions);
    }

    private void drawMarker(final double latitude, final double longtitude) {

        String URL = "http://img2.3lian.com/img2007/19/33/001.jpg";
        ImageLoader.getInstance().displayImage(URL, imageView);
        ImageLoader.getInstance().loadImage(URL, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                LatLng latLng = new LatLng(latitude, longtitude);
                //bitmap.set
                //bitmap.setHeight(20);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(stream.toByteArray()))
                        .createScaledBitmap(bitmap, 100, 100, false);
                bitmap = getRoundedCornerBitmap(bitmap);
                MarkerOptions markerOptions = new MarkerOptions().anchor(0.5f, 0.5f).position(latLng)
                        .draggable(true).icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                aMap.addMarker(markerOptions);
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });
        //  BitmapDescriptorFactory.f
        //  BitmapDescriptor bitmapDescriptor = new BitmapDescriptor(getBitmapFromURL(URL));

    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final int borderWidth = 5;
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final Paint paint = new Paint();
        final Paint strokePaint = new Paint();
        final Rect rect = new Rect(borderWidth, borderWidth, width - borderWidth, height - borderWidth);
        final RectF rectF = new RectF(rect);
        final float roundPx = width * 0.5f;
        final int radius = (width - borderWidth * 2) >> 1;


        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);


        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(borderWidth);
        strokePaint.setStrokeCap(Paint.Cap.ROUND);
        strokePaint.setColor(Color.RED);
        canvas.drawCircle(width >> 1, height >> 1, radius, strokePaint);

        //canvas.drawPaint(strokePaint);

        return output;
    }
}
