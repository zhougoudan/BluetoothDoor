package com.zhiwailife.www.myapplication;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.popup.QMUIPopup;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.vise.baseble.ViseBle;
import com.vise.baseble.callback.IConnectCallback;
import com.vise.baseble.callback.scan.IScanCallback;
import com.vise.baseble.callback.scan.ScanCallback;
import com.vise.baseble.core.DeviceMirror;
import com.vise.baseble.exception.BleException;
import com.vise.baseble.model.BluetoothLeDevice;
import com.vise.baseble.model.BluetoothLeDeviceStore;
import com.zhiwailife.www.myapplication.ui.BLEManager;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static android.widget.ListPopupWindow.WRAP_CONTENT;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button opendoor;
    ImageView img_user;
    private QMUIPopup mNormalPopup;
    private AppBarConfiguration mAppBarConfiguration;
    private List<BluetoothLeDevice> deviceList = new ArrayList<>();
    BluetoothAdapter mAdapter;
    public static ArrayList<String> data = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_manager, R.id.nav_vip,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //点击事件注册
        FloatingActionButton fab = findViewById(R.id.btn_scan);
        fab.setOnClickListener(this);
        View headview=navigationView.inflateHeaderView(R.layout.nav_header_main);
        ImageView head_iv= (ImageView) headview.findViewById(R.id.imageView_user);
        //侧滑菜单点击头像
        head_iv.setOnClickListener(new View.OnClickListener() {

            @Override public void onClick(View v) {
        Toast.makeText(MainActivity.this, "您点击了头像",Toast.LENGTH_LONG).show();
            }

        });
        //侧滑菜单点击功能
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_manager:
                        dialogOfPassword();
                        break;
                    case R.id.nav_vip:
                        sigleChoiceMenu();
                        break;
                }
                drawer.closeDrawers();
                return true;
            }
        });
        //蓝牙功能实现
        BluetoothManager bluetoothManager =(BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mAdapter = bluetoothManager.getAdapter();
        if (checkSupport()){
            if (mAdapter == null) {
                return;
            }
            if (!mAdapter.isEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, 5000);
            }}
        initBluetooth();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_scan:
                scanAllSubjects();
                if(ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                        &&ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                }else {
                    scanAllSubjects();
                }

                Intent intent = new Intent(MainActivity.this,LIstOfDeviceActivity.class);
//                intent.putExtra("adapter", (Serializable) adapter);
                startActivity(intent);
                break;

        }
    }
    public void dialogOfPassword(){
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(MainActivity.this);
        builder.setTitle("标题")
                .setPlaceholder("在此输入您的昵称")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        String text = builder.getEditText().getText().toString();
                        if (text.equals("123456")) {
                            Intent intent = new Intent(MainActivity.this,ManagerActivity.class);
                            startActivity(intent);
                            Log.d("123456789", "onClick 确定");
                        }else {
                            Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }
    public void pressItem(){



    }
    public void sigleChoiceMenu(){
        final String[] items = new String[]{"微信支付", "支付宝支付"};
        final QMUIDialog.CheckableDialogBuilder builder = new QMUIDialog.CheckableDialogBuilder(MainActivity.this);
        builder.setTitle("请选择")
                .setCheckedIndex(1)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCheckedIndex(which);
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        Toast.makeText(MainActivity.this, "你选择了 " + items[builder.getCheckedIndex()], Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .show();
    }
    public void sigleChoiceMenu(String[] items){
        final QMUIDialog.CheckableDialogBuilder builder = new QMUIDialog.CheckableDialogBuilder(MainActivity.this);
        builder.setTitle("请选择")
                .setCheckedIndex(1)
                .addItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCheckedIndex(which);
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
    public void initBluetooth(){
        ViseBle.config()
                .setScanTimeout(-1)//扫描超时时间，这里设置为永久扫描
                .setConnectTimeout(10 * 1000)//连接超时时间
                .setOperateTimeout(5 * 1000)//设置数据操作超时时间
                .setConnectRetryCount(3)//设置连接失败重试次数
                .setConnectRetryInterval(1000)//设置连接失败重试间隔时间
                .setOperateRetryCount(3)//设置数据操作失败重试次数
                .setOperateRetryInterval(1000)//设置数据操作失败重试间隔时间
                .setMaxConnectCount(3);//设置最大连接设备数量
//蓝牙信息初始化，全局唯一，必须在应用初始化时调用
        ViseBle.getInstance().init(this);
    }
    boolean checkSupport() {
        return getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    scanAllSubjects();
                }else {
                    Toast.makeText(this, "you denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
    public void scanAllSubjects(){
        ViseBle.getInstance().startScan(new ScanCallback(new IScanCallback() {
            @Override
            public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {
                deviceList.add(bluetoothLeDevice);
                data.add(bluetoothLeDevice.getAddress());
            }

            @Override
            public void onScanFinish(BluetoothLeDeviceStore bluetoothLeDeviceStore) {

            }

            @Override
            public void onScanTimeout() {

            }
        }));
    }
    public static ArrayList getSingle(ArrayList list){
        ArrayList newList = new ArrayList();     //创建新集合
        Iterator it = list.iterator();        //根据传入的集合(旧集合)获取迭代器
        while(it.hasNext()){          //遍历老集合
            Object obj = it.next();       //记录每一个元素
            if(!newList.contains(obj)){      //如果新集合中不包含旧集合中的元素
                newList.add(obj);       //将元素添加
            }
        }
        return newList;
    }
    public void connect(String deviceName){
        ViseBle.getInstance().connectByName(deviceName, new IConnectCallback() {
            @Override
            public void onConnectSuccess(DeviceMirror deviceMirror) {

            }

            @Override
            public void onConnectFailure(BleException exception) {

            }

            @Override
            public void onDisconnect(boolean isActive) {

            }
        });
    }
    public void ScanAndConnect(String deviceName){
        ViseBle.getInstance().connectByName(deviceName, new IConnectCallback() {
            @Override
            public void onConnectSuccess(DeviceMirror deviceMirror) {

            }

            @Override
            public void onConnectFailure(BleException exception) {

            }

            @Override
            public void onDisconnect(boolean isActive) {

            }
        });
    }
}
