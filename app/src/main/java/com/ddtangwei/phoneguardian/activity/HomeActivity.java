package com.ddtangwei.phoneguardian.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddtangwei.phoneguardian.R;
import com.ddtangwei.phoneguardian.Utils.ConstantValue;
import com.ddtangwei.phoneguardian.Utils.SpUtils;
import com.ddtangwei.phoneguardian.Utils.ToastUtils;

/**
 * Created by ddtangwei on 2016/10/16.
 */
public class HomeActivity extends Activity{

    private int[] mMipmaps;
    private String[] mTitleStr;
    private GridView gv_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();

        initData();

    }

    private void initData() {

        mTitleStr = new String[]{
                "手机防盗", "通信卫士", "软件管理", "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"
        };

        mMipmaps = new int[]{R.drawable.home_safe, R.drawable.home_callmsgsafe,
                R.drawable.home_apps, R.drawable.home_taskmanager,
                R.drawable.home_netmanager, R.drawable.home_trojan,
                R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings};

        gv_home.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mTitleStr.length;
            }

            @Override
            public Object getItem(int position) {
                return mTitleStr[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                    View view = View.inflate(getApplicationContext(), R.layout.gridview_item, null);

                    TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
                    ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);

                    tv_title.setText(mTitleStr[position]);
                    iv_icon.setBackgroundResource(mMipmaps[position]);

                return view;
            }
        });

        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position){

                    case 0:
                        //开启对话框
                        String psd = SpUtils.getString(getApplicationContext(), ConstantValue.PHONEGUARDIAN_SAFE_PSD, "");

                        if (TextUtils.isEmpty(psd)){

                            showSetPsdDialog();

                        }else {

                            showComfirmPsdDialog();

                        }

                        break;

                    case 8:
                        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);

                        startActivity(intent);
                        break;

                }

            }

            private void showSetPsdDialog() {

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

                final AlertDialog dialog = builder.create();

                final View view = View.inflate(getApplicationContext(),R.layout.dialog_set_psd,null);

                dialog.setView(view);

                dialog.show();

                Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
                Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

                bt_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText et_set_psd = (EditText) view.findViewById(R.id.et_set_psd);
                        EditText et_comfirm_psd = (EditText) view.findViewById(R.id.et_comfirm_psd);

                        String psd = et_set_psd.getText().toString();
                        String confirmPsd = et_comfirm_psd.getText().toString();

                        if (!TextUtils.isEmpty(psd)){

                            if (psd.equals(confirmPsd)){

                                Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                                startActivity(intent);

                                dialog.dismiss();

                                SpUtils.putString(getApplicationContext(),ConstantValue.PHONEGUARDIAN_SAFE_PSD,psd);

                            }else {
                                ToastUtils.Toast(getApplicationContext(),"密码不一致，请再次输入");
                            }

                        }else {

                            ToastUtils.Toast(getApplicationContext(),"请输入密码");

                        }

                    }
                });

                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    dialog.dismiss();

                    }
                });


            }

            private void showComfirmPsdDialog() {

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

                final AlertDialog dialog = builder.create();

                final View view = View.inflate(getApplicationContext(),R.layout.dialog_confirm_psd,null);

                dialog.setView(view);

                dialog.show();

                Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
                Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

                bt_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        EditText et_comfirm_psd = (EditText) view.findViewById(R.id.et_comfirm_psd);

                        String confirmPsd = et_comfirm_psd.getText().toString();

                        if (!TextUtils.isEmpty(confirmPsd)){

                            String psd  = SpUtils.getString(getApplicationContext(), ConstantValue.PHONEGUARDIAN_SAFE_PSD, "");

                            if (psd.equals(confirmPsd)){

                                Intent intent = new Intent(getApplicationContext(), TestActivity.class);
                                startActivity(intent);

                                dialog.dismiss();

                            }else {
                                ToastUtils.Toast(getApplicationContext(),"密码错误，请再次输入");
                            }

                        }else {

                            ToastUtils.Toast(getApplicationContext(),"请输入密码");
                        }
                    }
                });

                bt_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });
            }
        });
    }

    private void initUI() {
        gv_home = (GridView) findViewById(R.id.gv_home);
    }
}
