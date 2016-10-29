package cn.edu.csu.bbs.yunlusafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import cn.edu.csu.bbs.yunlusafe.R;
import cn.edu.csu.bbs.yunlusafe.utils.ConstantValue;
import cn.edu.csu.bbs.yunlusafe.utils.SpUtil;

/**
 * Created by Alan on 2016/9/15.
 */
public class SetupOverActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean setup_over=SpUtil.getBoolean(this, ConstantValue.SETUP_OVER,false);

        if (setup_over){
            //密码输入成功且完成了安全号码绑定--->手机防盗功能列表
            setContentView(R.layout.activity_setup_over);
            initUI();
        }else{
            Intent intent=new Intent(this,SetupActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initUI() {
        TextView tv_safe_number=(TextView)findViewById(R.id.tv_safe_number);
        tv_safe_number.setText(SpUtil.getString(getApplicationContext(),ConstantValue.CONTACT_PHONE,"未绑定"));
    }
}
