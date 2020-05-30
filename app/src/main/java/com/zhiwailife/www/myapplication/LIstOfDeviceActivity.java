package com.zhiwailife.www.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.vise.baseble.ViseBle;
import com.vise.baseble.callback.IConnectCallback;
import com.vise.baseble.callback.scan.IScanCallback;
import com.vise.baseble.callback.scan.ScanCallback;
import com.vise.baseble.core.DeviceMirror;
import com.vise.baseble.exception.BleException;
import com.vise.baseble.model.BluetoothLeDevice;
import com.vise.baseble.model.BluetoothLeDeviceStore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LIstOfDeviceActivity extends AppCompatActivity {
    private List<BluetoothLeDevice> deviceList = new ArrayList<>();
    BluetoothAdapter mAdapter;
    private  ArrayList<String> data = new ArrayList<String>();
    private ArrayAdapter<String> adapter=null;
    private ListView listView;
    private ArrayList<String> bledata_compare = null; //用于比较
    private ArrayList<String> bledata = null;    //用于存储
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_device);
        this.bledata_compare = new ArrayList<>();
        this.bledata = new ArrayList<>();
        BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mAdapter = bluetoothManager.getAdapter();
        //蓝牙扫描与显示
        if (checkSupport()){
            if (mAdapter == null) {
                return;
            }
            if (!mAdapter.isEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, 5000);
            }}
        initBluetooth();
        scanAllSubjects();
        if(ContextCompat.checkSelfPermission(LIstOfDeviceActivity.this,
            Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED
            &&ContextCompat.checkSelfPermission(LIstOfDeviceActivity.this,
            Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(LIstOfDeviceActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
            ActivityCompat.requestPermissions(LIstOfDeviceActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            scanAllSubjects();
        }
    this.adapter = new ArrayAdapter<String>(
                LIstOfDeviceActivity.this,android.R.layout.simple_list_item_1,data);
     listView = findViewById(R.id.list_view);
                listView.setAdapter(adapter);
        //点击某个Item时
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = data.get(i);
                dialogOfPassword(s);
            }
        });

//        for (BluetoothLeDevice i: deviceList){
//
//        }
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
    public void scanAllSubjects(){
        ViseBle.getInstance().startScan(new ScanCallback(new IScanCallback() {
            @Override
            public void onDeviceFound(BluetoothLeDevice bluetoothLeDevice) {

                if(bledata_compare.contains(bluetoothLeDevice.getAddress())) {
                }   //若列表中已经有了相应设备信息，则不添加进去
                else {
                    bledata_compare.add(bluetoothLeDevice.getAddress());
                    data.add(bluetoothLeDevice.getAddress()+bluetoothLeDevice.getName());
                    adapter.notifyDataSetChanged();
                }
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
    public void dialogOfPassword(final String deviceAddress){
        final String password="123456";
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(LIstOfDeviceActivity.this);
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
                        if (text.equals(password)) {
                            connect_with_address(deviceAddress);

                        }else {
                            Toast.makeText(LIstOfDeviceActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }
    public void connect_with_address(String deviceMac){
        ViseBle.getInstance().connectByMac(deviceMac, new IConnectCallback() {
            @Override
            public void onConnectSuccess(DeviceMirror deviceMirror) {
                Toast.makeText(LIstOfDeviceActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onConnectFailure(BleException exception) {
                Toast.makeText(LIstOfDeviceActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDisconnect(boolean isActive) {
                Toast.makeText(LIstOfDeviceActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
