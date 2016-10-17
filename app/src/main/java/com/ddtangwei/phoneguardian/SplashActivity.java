package com.ddtangwei.phoneguardian;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.ddtangwei.phoneguardian.Utils.StreamUtil;
import com.ddtangwei.phoneguardian.Utils.ToastUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class SplashActivity extends Activity {

    protected static final String tag = "SplashActivity";
    protected static final int UPDATE_VERSION = 100;
    protected static final int ENTER_HOME = 101;
    protected static final int IOEXCEPTION = 102;
    protected static final int URLEXCEPTION = 103;
    protected static final int JSONEXCEPTION = 104;

    private String mVersionDes;
    private String mVersionUrl;
    private TextView tv_version_name;
    private int mLocalVersionCode;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case UPDATE_VERSION:
                    showUpdateDialog();
                    break;

                case ENTER_HOME:
                    enterHome();
                    break;

                case IOEXCEPTION:
                    ToastUtils.Toast(getApplicationContext(),"IO读取异常");
                    enterHome();
                    break;

                case URLEXCEPTION:
                    ToastUtils.Toast(getApplicationContext(),"URL读取异常");
                    enterHome();
                    break;

                case JSONEXCEPTION:
                    ToastUtils.Toast(getApplicationContext(),"JSON读取异常");
                    enterHome();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //初始化UI
        initUI();

        //初始化data
        initData();

    }

    private void showUpdateDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("版本更新");
        builder.setMessage(mVersionDes);
        builder.setPositiveButton(R.string.update_dialog_positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                downloadAPK();
            }
        });

        builder.setNegativeButton(R.string.update_dialog_negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void downloadAPK() {

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){

            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/PhoneGuardian.apk";

            System.out.println(path);

            HttpUtils httpUtils = new HttpUtils();
            httpUtils.download(mVersionUrl,path, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    Log.i(tag,"下载成功");
                    File file = responseInfo.result;
                    installAPK(file);
                }

                @Override
                public void onFailure(HttpException e, String s) {

                    Log.i(tag,"下载失败");
                }

                @Override
                public void onStart() {
                    Log.i(tag, "刚刚开始下载");
                    super.onStart();
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {

                    Log.i(tag, "下载中........");
                    Log.i(tag, "total = "+total);
                    Log.i(tag, "current = "+current);

                    super.onLoading(total, current, isUploading);

                }
            });
        }
    }

    private void installAPK(File file) {
        
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.fromFile(file));
        intent.setType("application/vnd.android.package-archive");

        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void enterHome() {

        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
        finish();
    }


    private void initData() {

        //获取本地版本名称
        tv_version_name.setText("版本名称："+getVersionName());

        //获取本地版本号
        mLocalVersionCode = getVersionCode();

        //获取服务器数据，对比
        checkVersion();


    }

    private void checkVersion() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();

                    try {
                        URL url = new URL("http://192.168.31.68:8080/PhoneGuardian/version.json");
                        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                        connection.setConnectTimeout(5000);
                        connection.setReadTimeout(5000);
                        connection.setRequestMethod("GET");

                        if(connection.getResponseCode() == 200){

                            InputStream is = connection.getInputStream();

                            String json = StreamUtil.StreamToString(is);
                            
                            Log.i(tag,json);

                            try {
                                JSONObject jsonObject = new JSONObject(json);

                                String versionName = jsonObject.getString("versionName");
                                mVersionDes = jsonObject.getString("versionDes");
                                String versionCode = jsonObject.getString("versionCode");
                                mVersionUrl = jsonObject.getString("versionUrl");

                                Log.i(tag, versionName);
                                Log.i(tag, mVersionDes);
                                Log.i(tag, versionCode);
                                Log.i(tag, mVersionUrl);

                                if (mLocalVersionCode < Integer.parseInt(versionCode)){

                                    msg.what = UPDATE_VERSION;

                                }else{
                                    msg.what = ENTER_HOME;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                msg.what = JSONEXCEPTION;
                            }
                        }
                    } catch(MalformedURLException e) {
                        e.printStackTrace();
                        msg.what = URLEXCEPTION;
                    } catch (IOException e) {
                        e.printStackTrace();
                        msg.what = IOEXCEPTION;
                    }
                        mHandler.sendMessage(msg);
            }
        }).start();

    }

    private int getVersionCode() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);

            return info.versionCode;
            /*
            PackageManager pm = getPackageManager();
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            return info.versionCode;
            */
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //如果没得到，就return 0
        return 0;
    }

    private String getVersionName() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);

            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //如果没得到versionName，就return null
        return null;
    }


    private void initUI() {

        tv_version_name = (TextView) findViewById(R.id.tv_version_name);
    }
}

