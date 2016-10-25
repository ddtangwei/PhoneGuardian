package com.ddtangwei.phoneguardian.activity;

import android.app.Activity;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.view.View;

import com.ddtangwei.phoneguardian.R;
import com.ddtangwei.phoneguardian.Utils.ConstantValue;
import com.ddtangwei.phoneguardian.Utils.SpUtils;
import com.ddtangwei.phoneguardian.view.SettingItemView;

/**
 * Created by ddtangwei on 2016/10/23.
 */

public class SettingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUpdate();

    }

    private void initUpdate() {

        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);

        //获取已有的开关状态作显示
        boolean open_update = SpUtils.getBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, false);

        //是否选中，根据上一次存储的结果去决定
        siv_update.setCheck(open_update);

        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean check = siv_update.isCheck();
                siv_update.setCheck(!check);
                //将取反后的状态存储到相应的sp中
                SpUtils.putBoolean(getApplicationContext(),ConstantValue.OPEN_UPDATE,!check);
            }
        });
    }
}
