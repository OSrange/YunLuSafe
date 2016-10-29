package cn.edu.csu.bbs.yunlusafe.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import cn.edu.csu.bbs.yunlusafe.R;
import cn.edu.csu.bbs.yunlusafe.utils.ConstantValue;
import cn.edu.csu.bbs.yunlusafe.utils.SpUtil;
import cn.edu.csu.bbs.yunlusafe.utils.ToastUtils;
import cn.edu.csu.bbs.yunlusafe.view.SettingItemView;

/**
 * Created by Alan on 2016/9/17.
 */
public class SetupActivity extends BaseActivity{

    private EditText et_safe_number;
    private SettingItemView siv_sim;
    private Button btn_select;
    private Button btn_setup_over;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        initUI();

    }

    private void initUI() {
        siv_sim=(SettingItemView)findViewById(R.id.siv_sim);
        et_safe_number=(EditText)findViewById(R.id.et_safe_number);
        btn_select=(Button)findViewById(R.id.btn_select);
        btn_setup_over=(Button)findViewById(R.id.btn_setup_over);

        String sim_number=SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
        if (TextUtils.isEmpty(sim_number)){
            siv_sim.setCheck(false);
        }else{
            siv_sim.setCheck(true);
        }

        siv_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck=siv_sim.isCheck();
                siv_sim.setCheck(!isCheck);
                if (isCheck){
                    SpUtil.remove(getApplicationContext(),ConstantValue.SIM_NUMBER);
                }else {
                    TelephonyManager manager= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String simSerialNumber=manager.getSimSerialNumber();
                    SpUtil.putString(getApplicationContext(),ConstantValue.SIM_NUMBER,simSerialNumber);
                    ToastUtils.show(getApplicationContext(),"已成功绑定此SIM卡");
                }
            }
        });

        btn_setup_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String simNumber=SpUtil.getString(getApplicationContext(), ConstantValue.SIM_NUMBER, "");
                String phoneNumber=et_safe_number.getText().toString();
                SpUtil.putString(getApplicationContext(),ConstantValue.CONTACT_PHONE,phoneNumber);
                if (!TextUtils.isEmpty(simNumber)){
                    if (!TextUtils.isEmpty(phoneNumber)){
                        SpUtil.putBoolean(getApplicationContext(),ConstantValue.SETUP_OVER,true);
                        Intent intent=new Intent(getApplicationContext(),SetupOverActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        ToastUtils.show(getApplicationContext(),"请输入安全号码");
                    }
                }else {
                    ToastUtils.show(getApplicationContext(),"请绑定SIM卡");
                }
            }
        });

        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),ContactListActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null){
            String phoneNumber=data.getStringExtra("phoneNumber");
            //去空格去横杠
            phoneNumber=phoneNumber.replace("-","").replace(" ","").trim();
            et_safe_number.setText(phoneNumber);

        }
    }
}
