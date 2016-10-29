package cn.edu.csu.bbs.yunlusafe.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.csu.bbs.yunlusafe.R;

/**
 * Created by Alan on 2016/9/19.
 */
public class ContactListActivity extends BaseActivity{
    private static final String TAG ="ContactListActivity" ;
    private ListView lv_contact;
    private List<HashMap<String,String>> contactList=new ArrayList<HashMap<String, String>>();
    private MyAdapter mAdapter;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mAdapter=new MyAdapter();
            lv_contact.setAdapter(mAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        initUI();
        initData();
    }

    private void initData() {
        new Thread(){
            @Override
            public void run() {
                ContentResolver contentResolver=getContentResolver();
                Cursor cursor=contentResolver.query(Uri.parse("content://icc/adn"),
                        new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER},
                        null, null, null);
                contactList.clear();

                while (cursor.moveToNext()){
                    String phoneNumber=cursor.getString(1);
                    if (!TextUtils.isEmpty(phoneNumber)){
                        HashMap<String,String> hashMap=new HashMap<String, String>();
                        String contactName=cursor.getString(0);
                        hashMap.put("contactName",contactName);
                        hashMap.put("phoneNumber",phoneNumber);
                        contactList.add(hashMap);
                    }

                }
                cursor.close();
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initUI() {
        lv_contact=(ListView)findViewById(R.id.lv_contact);
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter!=null){
                    HashMap<String,String> hashMap=mAdapter.getItem(position);
                    String phoneNumber=hashMap.get("phoneNumber");
                    Intent intent=new Intent();
                    intent.putExtra("phoneNumber",phoneNumber);
                    setResult(0,intent);
                    finish();
                }
            }
        });
    }

    private class MyAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public HashMap<String,String> getItem(int position) {
            return contactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=View.inflate(getApplicationContext(),R.layout.listview_contact_item,null);

            TextView tv_name=(TextView)view.findViewById(R.id.tv_name);
            TextView tv_number=(TextView)view.findViewById(R.id.tv_number);

            tv_name.setText(contactList.get(position).get("contactName"));
            tv_number.setText(contactList.get(position).get("phoneNumber"));

            return view;
        }
    }
}
