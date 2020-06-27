package com.example.android.notepad.utils;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

import androidx.core.app.ActivityCompat;

/*
 * 获取当前的位置，并通过短信发送位置到指定号码
 */
public class LocationService extends Service {

    private LocationManager lm;
    private MyLocationListener listener;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        listener = new MyLocationListener();
        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true); // 是否允许使用付费
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 获取位置的精度
        String provider = lm.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(provider, 0, 0, (LocationListener) listener);
    }

    class MyLocationListener implements LocationListener {
        // 位置改变时获取经纬度
        @Override
        public void onLocationChanged(Location location) {
            String j = "jingdu:" + location.getLongitude();
            String w = "weidu:" + location.getLatitude();
            SharedPreferences mPref = getSharedPreferences("config",
                    MODE_PRIVATE);
            mPref.edit().putString("location", j + ";" + w).apply();
            String savedSafeNumber = mPref.getString("safeNumber", "");

//            double[] locationDouble=LocationUtils.standardToChina(location.getLongitude(), location.getLatitude());
            SmsManager.getDefault().sendTextMessage(savedSafeNumber, null,
                    "Phone's location:" + j + ";" + w, null, null); // 把回复的短信内容由中文修改为英文，中文在模拟器上显示为乱码
            stopSelf(); // 获取到经纬度以后，停止该service
        }


        // 状态改变时
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }


        // 提供者可以使用时
        @Override
        public void onProviderEnabled(String provider) {

        }

        // 提供者不可以使用时
        @Override
        public void onProviderDisabled(String provider) {

        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lm.removeUpdates(listener); // 停止所有的定位服务
    }

}