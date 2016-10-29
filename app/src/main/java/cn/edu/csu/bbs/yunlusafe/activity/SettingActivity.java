package cn.edu.csu.bbs.yunlusafe.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import cn.edu.csu.bbs.yunlusafe.R;
import cn.edu.csu.bbs.yunlusafe.utils.ConstantValue;
import cn.edu.csu.bbs.yunlusafe.utils.SpUtil;
import cn.edu.csu.bbs.yunlusafe.view.SettingItemView;

/**
 * Created by Alan on 2016/9/13.
 */
public class SettingActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUpdate();
    }

    private void initUpdate() {
        final SettingItemView siv_update= (SettingItemView) findViewById(R.id.siv_update);
        boolean open_update=SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE,false);
        siv_update.setCheck(open_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck=siv_update.isCheck();
                siv_update.setCheck(!isCheck);
                SpUtil.putBoolean(getApplicationContext(),ConstantValue.OPEN_UPDATE,!isCheck);
                if (!isCheck){
                    Toast.makeText(getApplicationContext(),"自动更新已关闭",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"自动更新已开启",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
