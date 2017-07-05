package com.example.administrator.bicycle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.example.administrator.bicycle.update.UpdateManager;
import com.example.administrator.bicycle.util.ContentValuse;
import com.example.administrator.bicycle.util.CustomProgressDialog;
import com.example.administrator.bicycle.util.Dialog;
import com.example.administrator.bicycle.util.NetWorkStatus;
import com.example.administrator.bicycle.util.PermissionUtils;
import com.example.administrator.bicycle.util.SharedPreUtils;
import com.example.administrator.bicycle.util.TOPpopCancel;
import com.example.administrator.bicycle.util.TopPopupWindow;
import com.example.administrator.bicycle.zxing.camera.open.CaptureActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements RouteSearch.OnRouteSearchListener, AMapNaviListener, AMapNaviViewListener, View.OnClickListener {

    private final int DENGDIA = 0X1245141;
    private final int SETADDRESS = 0X1245181;

    private final int LOCATION = 0X12654;
    /**
     * 判断是否点击小于1秒
     */

    private long exitTime = 0;


    private AMap aMap;
    private AMapNavi aMapNavi;
    private MapView mMapView;//地图

    private Button navigation;//导航按钮，，，，，，，，，，，，，，，，，，
    private ImageView code;//二维码按钮
    private LinearLayout imgEnterToPersonalCenter;
    private AMapLocationClientOption mLocationOption;
    private TextView iv_location;
    private TopPopupWindow pop;
    private TOPpopCancel popCancel;
    /**
     * 选择终点Aciton标志位
     */
    private NaviLatLng endLatlng = new NaviLatLng(39.955846, 116.352765);
    private NaviLatLng startLatlng = new NaviLatLng();

    private int second;

    /**
     * 保存当前算好的路线
     */
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<RouteOverLay>();

    private String token;

    /**
     * 路线计算成功标志位
     */
    private boolean calculateSuccess = false;
    private boolean chooseRouteSuccess = false;

    private String road;


    private ImageButton butright;
    private RelativeLayout lay_lift;
    private ImageView iv_lift;

    private NaviLatLng naviLatLng;

    //        //利用Handler更新UI
    final Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case DENGDIA:
                    pop();
                    break;
                case SETADDRESS:
                    String rosaad = (String) msg.obj;
                    if (popCancel != null) {
                        popCancel.setAddress(rosaad);
                    }
                    if (pop != null) {
                        pop.setAddress(rosaad);
                    }

                    break;

                case LOCATION:
                    CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(startLatlng.getLatitude(), startLatlng.getLongitude()), 15, 0, 0));
                    aMap.moveCamera(mCameraUpdate);//把缩放级别放进摄像机
                    dialog.dismiss();
                    break;

            }


        }
    };
    private GeocodeSearch geocoderSearch;

    private CustomProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        //设置全屏显示
        //        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

//        UpdateManager manager = new UpdateManager(MainActivity.this);
//        // 检查软件更新
//        manager.checkUpdate();

        //初始化控件
        init();
        //开启地图
        mMapView.onCreate(savedInstanceState);

//设置定位蓝点
        setBluePoint();
        //初始化定位
        initMapAndstartLocation();


        //设置覆盖物
        setIcon();

        getBluePointLocation();


        CameraUpdate mCameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(34.26984294, 108.94729614), 16, 0, 0));
        aMap.moveCamera(mCameraUpdate);//把缩放级别放进摄像机
//
        UiSettings mUiSettings;//定义一个UiSettings对象
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象
        mUiSettings.setZoomControlsEnabled(false);


        if (!NetWorkStatus.isNetworkAvailable(this)) {
            dialog.dismiss();
            Toast.makeText(this, "网络不可用，请连接网络！", Toast.LENGTH_SHORT).show();
        }




        /*
         * 扫描二维码的按钮
         */
        code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toCapture();
            }
        });

        /*
         * 进入个人中心的点击事件
         */

        imgEnterToPersonalCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//             startActivity(new Intent(MainActivity.this, RegisteredActivity.class));
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                //            startActivity(new Intent(MainActivity.this, PrizeActivity.class));
            }
        });


    }


    //得到蓝点的位置信息 坐标转地址
    public void getBluePointLocation() {
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                //解析result获取地址描述信息

                Message msg = new Message();
                msg.what = SETADDRESS;

                LatLonPoint latLonPoint = regeocodeResult.getRegeocodeAddress().getRoads().get(0).getLatLngPoint();
                toCalculateDistance(latLonPoint, naviLatLng);

                msg.obj = regeocodeResult.getRegeocodeAddress().getRoads().get(0).getName();
                mhandler.sendMessage(msg);
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
    }


    /*
    * 判断是否到达目标点
     */
    private void toCalculateDistance(LatLonPoint latLonPoint, NaviLatLng naviLatLng) {
        LatLng start1latLng = new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
        LatLng endLatlng = new LatLng(naviLatLng.getLatitude(), naviLatLng.getLongitude());
        double distance = Math.floor(AMapUtils.calculateLineDistance(start1latLng, endLatlng));
        if (distance < 50) {
            alertDialog();
        }
    }

    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您已到达自行车附近");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    /*
            * 去二维码扫描
            */
    private void toCapture() {

        if (PermissionUtils.checkPermissionCamera(this)) {
            toCaptureActivity();
        }

    }

    private void toCaptureActivity() {
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] paramArrayOfInt) {

        PermissionUtils.onRequestPermissionsResultPermissions(requestCode, paramArrayOfInt);

        if (PermissionUtils.onRequestPermissionsResultCamera(MainActivity.this, requestCode, permissions, paramArrayOfInt)) {
            toCaptureActivity();
        }

    }


    /**
     * 清除当前地图上算好的路线
     */
    private void clearRoute() {
        for (int i = 0; i < routeOverlays.size(); i++) {
            RouteOverLay routeOverlay = routeOverlays.valueAt(i);
            routeOverlay.removeFromMap();
        }
        routeOverlays.clear();
    }


    /*
     * 初始化定位
     */
    private void initMapAndstartLocation() {

        if (mLocationClient == null) {
            //初始化定位
            mLocationClient = new AMapLocationClient(getApplicationContext());
            //设置定位回调监听
            mLocationClient.setLocationListener(mLocationListener);
            //初始化AMapLocationClientOption对象
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //获取一次定位结果：
            //该方法默认为false。
            mLocationOption.setOnceLocation(true);
            //获取最近3s内精度最高的一次定位结果：
            //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
            mLocationOption.setOnceLocationLatest(true);
            //设置是否允许模拟位置,默认为false，不允许模拟位置
            mLocationOption.setMockEnable(false);
            //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
            mLocationOption.setHttpTimeOut(10000);
            //给定位客户端对象设置定位参数
            mLocationClient.setLocationOption(mLocationOption);
            //启动定位
        }
        mLocationClient.startLocation();
    }


    /*
     * 设置覆盖物
     */
    private void setIcon() {
        aMap = mMapView.getMap();

        //绘制marker
//        Marker marker = aMap.addMarker(new MarkerOptions()
//              .position(new LatLng(startLatlng.getLatitude(), startLatlng.getLongitude()))
//
//                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
//                       .decodeResource(getResources(), R.mipmap.jqdw)))
//                .draggable(true));
//        NaviLatLng naviLatLng = new NaviLatLng(marker.getPosition().latitude, marker.getPosition().longitude);
//
//
//        startLatlng = naviLatLng;


        // 绘制曲线

        //大地曲线
//        aMap.addPolyline((new PolylineOptions())
//                .add(new LatLng(43.828, 87.621), new LatLng(45.808, 126.55))
//                .geodesic(true).color(Color.RED));
        //设置地图中间的覆盖物
        //      marker.setPositionByPixels(360, 640);


        MarkerOptions markerOption = new MarkerOptions();
        markerOption.title("导航").snippet("导航");

        markerOption.draggable(false);//设置Marker不可拖动
        markerOption.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.mipmap.dwmk)));

        // 将Marker设置为贴地显示，可以双指下拉地图查看效果
        markerOption.setFlat(true);//设置marker平贴地图效果   109.109696,34.292366    109.11123,34.292818  109.110983,34.290115


        final LatLng latLng = new LatLng(34.292366, 109.109696);
        LatLng latLng1 = new LatLng(34.292818, 109.11123);
        LatLng latLng2 = new LatLng(34.290115, 109.110983);

//        final Marker marker1 = aMap.addMarker(new MarkerOptions().position(latLng));//.title("导航").snippet("导航")
//        final Marker marker2 = aMap.addMarker(new MarkerOptions().position(latLng1));
//        final Marker marker3 = aMap.addMarker(new MarkerOptions().position(latLng2));

        BitmapDescriptor bt = BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(), R.mipmap.dwmk));

        final Marker marker1 = aMap.addMarker(new MarkerOptions().icon(bt).position(latLng));
        final Marker marker2 = aMap.addMarker(new MarkerOptions().icon(bt).position(latLng1));
        final Marker marker3 = aMap.addMarker(new MarkerOptions().icon(bt).position(latLng2));


        // 定义 Marker 点击事件监听
        AMap.OnMarkerClickListener mapClickListener = new AMap.OnMarkerClickListener() {
            // marker 对象被点击时回调的接口
            // 返回 true 则表示接口已响应事件，否则返回fals
            @Override
            public boolean onMarkerClick(Marker marker) {
                clearRoute();
                naviLatLng = new NaviLatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                if (startLatlng == null && endLatlng == null) {
                    Toast.makeText(MainActivity.this, "当前位置尚未获取成功，请稍后再试", Toast.LENGTH_SHORT).show();
                } else {
                    endLatlng = naviLatLng;
                    aMapNavi.calculateWalkRoute(startLatlng, naviLatLng);

                }
                return true;
            }
        };
        // 绑定 Marker 被点击事件
        aMap.setOnMarkerClickListener(mapClickListener);


        marker1.showInfoWindow();
        marker2.showInfoWindow();
        marker3.showInfoWindow();
    }

    private void popwindow() {
        if (pop == null) {
            pop = new TopPopupWindow(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 隐藏弹出窗口
                    pop.dismiss();
                    MainActivity.this.startActivity(new Intent(MainActivity.this, SubscribeActivity.class));
                }
            });
        }

        pop.setData(road, calculateLineDistance() + "", getMin(second));
        if (!pop.isShowing()) {
            pop.showAsDropDown(findViewById(R.id.tltle));
        }
    }

    private double calculateLineDistance() {
        return Math.floor(AMapUtils.calculateLineDistance(new LatLng(startLatlng.getLatitude(), startLatlng.getLongitude()), new LatLng(endLatlng.getLatitude(), endLatlng.getLongitude())));
    }

    private void pop() {
        if (popCancel == null) {
            popCancel = new TOPpopCancel(this, road, "12312312", 0.5, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popCancel.dismiss();
                    SharedPreUtils.sharedPut(MainActivity.this, ContentValuse.isSubscribe, false);
                }
            });
        }
        if (!popCancel.isShowing()) {
            popCancel.showAsDropDown(findViewById(R.id.tltle));
        }
    }


    /*
     * 初始化控件
     */
    protected void init() {
        mMapView = (MapView) findViewById(R.id.map);//获取地图控件引用


        code = (ImageView) findViewById(R.id.code);//二维码按钮
        imgEnterToPersonalCenter = (LinearLayout) findViewById(R.id.img_EnterToPersonalCenter);
        iv_location = (TextView) findViewById(R.id.iv_location);
        iv_location.setOnClickListener(this);

        findViewById(R.id.tv_zhushou).setOnClickListener(this);
        findViewById(R.id.tv_suaxin).setOnClickListener(this);
        findViewById(R.id.tv_tousu).setOnClickListener(this);
        butright = (ImageButton) findViewById(R.id.btn_Directions);
        butright.setOnClickListener(this);
        iv_lift = (ImageView) findViewById(R.id.iv_lift);
        iv_lift.setOnClickListener(this);
        lay_lift = (RelativeLayout) findViewById(R.id.rl_layout);
        lay_lift.setOnClickListener(this);


        dialog = CustomProgressDialog.createDialog(this);
        dialog.show();

        token = SharedPreUtils.getSharedPreferences(this).getString(ContentValuse.token, null);

    }


    public String getMin(long mss) {
        return mss / 60 + "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_location:
//                dialog.show();
                if (!NetWorkStatus.isNetworkAvailable(this)) {
                    Toast.makeText(this, "请设置网络！", Toast.LENGTH_SHORT).show();
                } else {
                    //设置定位蓝点
                    setBluePoint();
                    initMapAndstartLocation();

                }


                break;
            case R.id.tv_zhushou:
                startActivity(new Intent(MainActivity.this, AssistantActivity.class));
                break;
            case R.id.tv_suaxin:
                startActivity(new Intent(MainActivity.this, PrizeActivity.class));
                break;
            case R.id.tv_tousu:
                if (token == null || token.equals("")) {
                    startActivity(new Intent(MainActivity.this, RegisteredActivity.class));
                } else {
                    startActivity(new Intent(MainActivity.this, ComplaintsActivity.class));
                }

                break;
            case R.id.btn_Directions:
                butright.setVisibility(View.GONE);
                lay_lift.setVisibility(View.VISIBLE);

                break;
            case R.id.rl_layout:
                Intent intent = new Intent(MainActivity.this, WebActivity.class);
                intent.putExtra(ContentValuse.url, "");
                startActivity(intent);

                break;
            case R.id.iv_lift:
                butright.setVisibility(View.VISIBLE);
                lay_lift.setVisibility(View.GONE);

                break;

        }
    }

    /*
     * 设置定位蓝点
     */
    protected void setBluePoint() {
        aMap = mMapView.getMap();
        aMap.setTrafficEnabled(false);//显示实时交通
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(20000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图

        mMapView.onDestroy();

        mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务

        /**
         * 当前页面只是展示地图，activity销毁后不需要再回调导航的状态
         */
        aMapNavi.removeAMapNaviListener(this);
        aMapNavi.destroy();
    }

    private void addAMapNaviListener() {
        aMapNavi = AMapNavi.getInstance(getApplicationContext());
        aMapNavi.addAMapNaviListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PermissionUtils.checkPermissionneedPermissions(this);

        addAMapNaviListener();

        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
        if (SharedPreUtils.sharedGet(this, ContentValuse.isSubscribe, false)) {
            mhandler.sendEmptyMessageDelayed(DENGDIA, 4000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


    //声明AMapLocationClient类对象 29.810044 102.684203
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。获取当前经纬度
                    double latitude = amapLocation.getLatitude();//精度
                    double longitude = amapLocation.getLongitude();//维度

                    MyApplication.latitude = latitude;
                    MyApplication.longitude = longitude;
                    road = amapLocation.getRoad();


                    startLatlng = new NaviLatLng(latitude, longitude);

                    MyApplication.city = amapLocation.getCity();
                    MyApplication.startLatlng = startLatlng;


                    mhandler.sendEmptyMessage(LOCATION);

                    Log.d("a1*", "sa");
                    mLocationClient.stopLocation();

//                    System.out.println("省："+arg0.getProvince());
//                    System.out.println("国家："+arg0.getCountry());
//                    System.out.println("经度"+arg0.getLatitude());
//                    System.out.println("纬度"+arg0.getLongitude());
//                    System.out.println("路是："+arg0.getRoad());

                } else {
                    dialog.dismiss();
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Toast.makeText(MainActivity.this, "定位失败", Toast.LENGTH_SHORT).show();
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };


    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
    }


    @Override
    public void onCalculateRouteSuccess() {
        /**
         * 清空上次计算的路径列表。
         */
        routeOverlays.clear();
        AMapNaviPath path = aMapNavi.getNaviPath();
        second = path.getAllTime();
        /**
         * 单路径不需要进行路径选择，直接传入－1即可
         */
        drawRoutes(-1, path);
        popwindow();
    }

    @Override
    public void onCalculateRouteFailure(int i) {
        calculateSuccess = false;
        Toast.makeText(getApplicationContext(), "计算路线失败，" + i, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

        //清空上次计算的路径列表。
        routeOverlays.clear();
        HashMap<Integer, AMapNaviPath> paths = aMapNavi.getNaviPaths();
        for (int i = 0; i < ints.length; i++) {
            AMapNaviPath path = paths.get(ints[i]);
            if (path != null) {
                drawRoutes(ints[i], path);
            }
        }
    }


    private void drawRoutes(int routeId, AMapNaviPath path) {
        calculateSuccess = true;
        aMap.moveCamera(CameraUpdateFactory.changeTilt(0));
        RouteOverLay routeOverLay = new RouteOverLay(aMap, path, this);
        routeOverLay.setTrafficLine(false);
        routeOverLay.addToMap();
        routeOverlays.put(routeId, routeOverLay);
    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }


    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
////返回当前位置的经纬度坐标。
        NaviLatLng naviLatLng = aMapNaviLocation.getCoord();
        // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(naviLatLng.getLatitude(), naviLatLng.getLongitude()), 200, GeocodeSearch.AMAP);

        geocoderSearch.getFromLocationAsyn(query);


    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }


    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {

    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }
}

