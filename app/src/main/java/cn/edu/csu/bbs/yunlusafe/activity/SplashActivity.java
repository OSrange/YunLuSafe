package cn.edu.csu.bbs.yunlusafe.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.edu.csu.bbs.yunlusafe.R;
import cn.edu.csu.bbs.yunlusafe.utils.ConstantValue;
import cn.edu.csu.bbs.yunlusafe.utils.SpUtil;
import cn.edu.csu.bbs.yunlusafe.utils.StreamUtils;
import cn.edu.csu.bbs.yunlusafe.utils.ToastUtils;

public class SplashActivity extends Activity {

    protected static final String TAG="SplashActivity";

    //状态码
    private static final int UPDATE_VERSION = 100;
    private static final int ENTER_HOME =101 ;
    private static final int URL_ERROR = 102;
    private static final int IO_ERROR =103 ;
    private static final int JSON_ERROR = 104;

    private TextView tv_version_name;
    private int mLocalVersionCode;
    private RelativeLayout ry_root;
    private String mVersionDes;
    private String mDownloadUrl;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_VERSION:
                    //弹出对话框
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    //进入主界面
                    enterHome();
                    break;
                case URL_ERROR:
                    ToastUtils.show(getApplicationContext(),"URL请求失败");
                    enterHome();
                    break;
                case IO_ERROR:
                    ToastUtils.show(getApplicationContext(),"IO错误");
                    enterHome();
                    break;
                case JSON_ERROR:
                    ToastUtils.show(getApplicationContext(),"JSON数据解析错误");
                    enterHome();
                    break;
            }
        }
    };

    private void showUpdateDialog() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.xiangyao);
        builder.setTitle("发现新版本！");
        builder.setMessage(mVersionDes);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downloadApk();
            }
        });
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
        builder.show();
    }

    private void downloadApk() {
        //判断SD卡是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //获取路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "app-release.apk";
            //发送请求
            HttpUtils httpUtils = new HttpUtils();
            httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载成功
                    File file=responseInfo.result;
                    installApk(file);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    //下载失败
                }

                //刚开始下载的方法
                @Override
                public void onStart() {
                    super.onStart();
                }

                //下载中的方法


                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    super.onLoading(total, current, isUploading);
                }
            });
        }
    }

    /**
     * 安装对应APK
     * @param file 安装文件
     */
    protected void installApk(File file) {
        Intent intent=new Intent("android.intent.action.VIEW" );

        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"app-release.apk"))
                ,"application/vnd.android.package-archive");
        startActivity(intent);
    }


    /*
    进入主界面
     */
    protected void enterHome() {
        Intent intent=new Intent(this,HomeActivity.class);
        startActivity(intent);
        //关闭导航界面
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        View decorview=getWindow().getDecorView();
        decorview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);


        //初始化UI
        initUI();
        //初始化数据
        initData();
        //初始化动画
        initAnimation();
    }

    private void initAnimation() {
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
        alphaAnimation.setDuration(3000);
        ry_root.startAnimation(alphaAnimation);
    }

    private void initData() {
        //获取版本名称
        tv_version_name.setText("版本名称:" + getVersionName());
        //本地版本号与服务端版本号对比，如有更新，提醒用户下载
        //获取本地版本号
        mLocalVersionCode=getVersionCode();
        //获取服务端版本号
        if (SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE,false)){
            checkVersion();
        }else{
            mHandler.sendEmptyMessageDelayed(ENTER_HOME,4000);
        }

    }

    /*
    检测版本号
     */
    private void checkVersion() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg=Message.obtain();
                long startTime=System.currentTimeMillis();
                try {
                    URL url=new URL("http://71158.vhost61.cloudvhost.net/workspace/song/update.json");
                    HttpURLConnection connection=(HttpURLConnection)url.openConnection();
                    connection.setConnectTimeout(2000);
                    connection.setReadTimeout(2000);
                    connection.setRequestMethod("GET");
                    if (connection.getResponseCode()==200){
                        InputStream is=connection.getInputStream();
                        String json=StreamUtils.streamToString(is);
                        //解析数据
                        JSONObject jsonObject=new JSONObject(json);
                        String versionName=jsonObject.getString("versionName");
                        String versionCode=jsonObject.getString("versionCode");
                        mVersionDes=jsonObject.getString("versionDes");
                        mDownloadUrl=jsonObject.getString("downloadUrl");
                        
                        if (mLocalVersionCode<Integer.parseInt(versionCode)){
                            //提示用户更新，弹出对话框
                            msg.what=UPDATE_VERSION;
                        }else{
                            //进入主界面
                            msg.what=ENTER_HOME;
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what=URL_ERROR;
                }catch (IOException e){
                    e.printStackTrace();
                    msg.what=IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what=JSON_ERROR;
                }finally {
                    //指定睡眠时长，至少在导航界面停留4秒
                    long endTime=System.currentTimeMillis();
                    long time=4000-endTime+startTime;
                    if (time>0){
                        try {
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //发送消息
                    mHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    /**
     *返回版本号
     * @return
     * 非0代表成功
     */
    private int getVersionCode() {
        //获取包管理者对象
        PackageManager pm=getPackageManager();
        //获取指定包名的基本信息，flags为0代表基本信息
        try {
            PackageInfo packageInfo= pm.getPackageInfo(getPackageName(),0);
            //返回版本号
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本名称：清单文件中
     * @return 应用版本名称 返回null代表异常
     */
    private String getVersionName() {
        //获取包管理者对象
        PackageManager pm=getPackageManager();
        //获取指定包名的基本信息，flags为0代表基本信息
        try {
            PackageInfo packageInfo= pm.getPackageInfo(getPackageName(),0);
            //返回版本名称
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initUI() {
        tv_version_name=(TextView)findViewById(R.id.tv_version_name);
        ry_root = (RelativeLayout) findViewById(R.id.rl_root);
    }
}
