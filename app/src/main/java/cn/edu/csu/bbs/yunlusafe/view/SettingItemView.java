package cn.edu.csu.bbs.yunlusafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.edu.csu.bbs.yunlusafe.R;

/**
 * Created by Alan on 2016/9/13.
 */
public class SettingItemView extends RelativeLayout {
    private final CheckBox cb_box;
    private final TextView tv_des;
    private final TextView tv_title;
    private final String NAMESPACE="http://schemas.android.com/apk/res-auto";
    private String mDestitle;
    private String mDesoff;
    private String mDeson;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.setting_item_view, this);

        tv_title =(TextView)findViewById(R.id.tv_title);
        tv_des=(TextView)findViewById(R.id.tv_des);
        cb_box= (CheckBox) findViewById(R.id.cb_box);

        initAttrs(attrs);

        tv_title.setText(mDestitle);
        tv_des.setText(mDesoff);
    }

    private void initAttrs(AttributeSet attrs) {
        mDestitle=attrs.getAttributeValue(NAMESPACE,"destitle");
        mDesoff=attrs.getAttributeValue(NAMESPACE,"desoff");
        mDeson=attrs.getAttributeValue(NAMESPACE,"deson");
    }

    public boolean isCheck(){
        return cb_box.isChecked();
    }

    public void setCheck(boolean isCheck){
        cb_box.setChecked(isCheck);
        if (isCheck){
            tv_des.setText(mDeson);
        }else {
            tv_des.setText(mDesoff);
        }
    }
}
