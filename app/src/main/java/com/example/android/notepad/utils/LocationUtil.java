package com.example.android.notepad.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.text.TextUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.core.app.ActivityCompat;

public class LocationUtil {

    public String getLocation(Context mContext) throws IOException {

        Criteria c = new Criteria();
        //... Criteria 还有其他属性
        LocationManager manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        assert manager != null;
        String bestProvider = manager.getBestProvider(c, true);
        //得到定位信息
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return "ACCESS_FINE_LOCATION 信息不匹配";
        {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)

                return "ACCESS_COARSE_LOCATION 信息不匹配";
        }

        Location location = null;
        if (!TextUtils.isEmpty(bestProvider)) {
            location = manager.getLastKnownLocation(bestProvider);
        }
        if (null == location) {
            //如果没有最好的定位方案则手动配置
            if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } else if (manager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)) {
                location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        if (null == location) {
//            Toast.makeText(mContext, "location为null，获取定位失败", Toast.LENGTH_LONG).show();
            return "location为null，获取定位失败";
        }

        //通过地理编码的到具体位置信息
        Geocoder geocoder = new Geocoder(mContext, Locale.CHINESE);
        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        if (((List) addresses).size() <= 0) {
//            Toast.makeText(mContext, "addresses小于0，获取定位失败", Toast.LENGTH_LONG).show();
            return "addresses小于0，获取定位失败";
        }

        Address address = addresses.get(0);
        String country = address.getCountryName();//得到国家
        String locality = address.getLocality();//得到城市
        return country + " " + locality;

    }

    public String getLastKnownLocation(Context mContext) throws IOException {

        LocationManager manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        assert manager != null;
        List<String> providers = manager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return "获取定位失败";
            }
            Location l = manager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;

            }
        }
        assert bestLocation != null;
        String res = "Latitude"+bestLocation.getLatitude() + " " + "Altitude"+bestLocation.getAltitude();
//        String res = getLocationAddress(mContext, bestLocation);
//        Geocoder geoCoder = new Geocoder(mContext, Locale.CHINESE);
//        List<Address> addresses = geoCoder.getFromLocation(
//                bestLocation.getLatitude(), bestLocation.getLongitude(),
//                1);
//        String res =getLocationAddress(mContext, bestLocation);

        return res;
    }

    /**
     * 将经纬度转换成中文地址
     * 要开VPN
     * Failed to connect to server: java.net.SocketTimeoutException:
     * failed to connect to play.googleapis.com/172.217.160.74 (port 443) from /192.168.232.2 (port 44364) after 10000ms
     * @param location
     * @return
     */
    private String getLocationAddress(Context mContext, Location location) {
        Geocoder gc = new Geocoder(mContext, Locale.getDefault());
        Address address;
        String countryName;
        String locality;
        String addressLine;
        List<Address> locationList = null;
        try {
            locationList = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        address = locationList.get(0);//得到Address实例
//Log.i(TAG, "address =" + address);
        countryName = address.getCountryName();//得到国家名称，比如：中国
//        Log.i(TAG, "countryName = " + countryName);
        locality= address.getLocality();//得到城市名称，比如：北京市
//        Log.i(TAG, "locality = " + locality);
        for (int i = 0; address.getAddressLine(i) != null; i++) {
            addressLine = address.getAddressLine(i);//得到周边信息，包括街道等，i=0，得到街道名称
//            Log.i(TAG, "addressLine = " + addressLine);
        }
        return countryName+" "+locality+" ";
    }

}