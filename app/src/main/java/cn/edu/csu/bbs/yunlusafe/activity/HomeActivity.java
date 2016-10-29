package cn.edu.csu.bbs.yunlusafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import cn.edu.csu.bbs.yunlusafe.R;
import cn.edu.csu.bbs.yunlusafe.utils.ConstantValue;
import cn.edu.csu.bbs.yunlusafe.utils.Md5Util;
import cn.edu.csu.bbs.yunlusafe.utils.SpUtil;
import cn.edu.csu.bbs.yunlusafe.utils.ToastUtils;

/**
 * Created by Alan on 2016/8/12.
 */
public class HomeActivity extends Activity{

    private GridView gv_home;
    private String[] mTitleStr;
    private int[] mDrawableIds;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_home);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.blue);

        }

        initUI();
        //初始化数据
        initData();
    }

    private void initData() {
        mTitleStr=new String[] {
                "手机防盗","通信卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理","高级工具","设置中心"
        };
        mDrawableIds=new int[] {
                R.drawable.home_safe,R.drawable.home_callmsgsafe,R.drawable.home_apps,
                R.drawable.home_taskmanager,R.drawable.home_netmanager,R.drawable.home_trojan,
                R.drawable.home_sysoptimize,R.drawable.home_tools,R.drawable.home_settings
        };

        gv_home.setAdapter(new MyAdapter());
        gv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showDialog();
                        break;

                    case 8:
                        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void showDialog() {
        String psd=SpUtil.getString(this, ConstantValue.MOBILE_SAFE_PSD,"");
        if (TextUtils.isEmpty(psd)){
            showSetPsdDialog();
        }else{
            showConfirmPsdDialog(psd);
        }
    }

    private void showConfirmPsdDialog(final String psd) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final AlertDialog dialog=builder.create();
        final View view=View.inflate(this,R.layout.dialog_confirm_psd,null);
        dialog.setView(view);
        dialog.show();

        Button bt_submmit=(Button)view.findViewById(R.id.bt_submmit);
        Button bt_cancel=(Button)view.findViewById(R.id.bt_cancel);

        bt_submmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText et_confirm_psd=(EditText)view.findViewById(R.id.et_confirm_psd);

                String confirmPsd=et_confirm_psd.getText().toString();
                confirmPsd=Md5Util.encoder(confirmPsd);
                if (!TextUtils.isEmpty(confirmPsd)){
                    if (psd.equals(confirmPsd)){
                        Intent intent=new Intent(getApplicationContext(),SetupOverActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }else{
                        ToastUtils.show(getApplicationContext(),"密码错误");
                    }
                }else{
                    ToastUtils.show(getApplicationContext(),"请输入密码");
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

    private void showSetPsdDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        final AlertDialog dialog=builder.create();
        final View view=View.inflate(this,R.layout.dialog_set_psd,null);
        dialog.setView(view);
        dialog.show();

        Button bt_submmit=(Button)view.findViewById(R.id.bt_submmit);
        Button bt_cancel=(Button)view.findViewById(R.id.bt_cancel);

        bt_submmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et_set_psd=(EditText)view.findViewById(R.id.et_set_psd);
                EditText et_confirm_psd=(EditText)view.findViewById(R.id.et_confirm_psd);

                String psd=et_set_psd.getText().toString();
                String confirmPsd=et_confirm_psd.getText().toString();

                if (!TextUtils.isEmpty(psd)&&!TextUtils.isEmpty(confirmPsd)){
                    if (psd.equals(confirmPsd)){
                        Intent intent=new Intent(getApplicationContext(),SetupOverActivity.class);
                        startActivity(intent);
                        dialog.dismiss();

                        SpUtil.putString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD, Md5Util.encoder(psd));
                    }else{
                        ToastUtils.show(getApplicationContext(),"确认密码错误");
                    }
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

    private void initUI() {
        gv_home = (GridView) findViewById(R.id.gv_home);
    }

    private class MyAdapter extends BaseAdapter {
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
            View view=View.inflate(getApplicationContext(), R.layout.gridview_item,null);
            ImageView iv_icon=(ImageView)view.findViewById(R.id.iv_icon);
            TextView tv_title=(TextView)view.findViewById(R.id.tv_title);

            tv_title.setText(mTitleStr[position]);
            iv_icon.setBackgroundResource(mDrawableIds[position]);

            return view;
        }
    }

}
