package com.example.genshin_yuanqinassistant;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class denglu_zhongxin extends AppCompatActivity {
    //从网络获取的uid——json数据
    String json_uidall ;
    //用户的uid本地储存数据
    String json_useruid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_denglu_zhongxin);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = getExternalFilesDir(null).toString()+"/原琴工具箱小程序";
                File file = new File(path,"user_list.json");
                FileInputStream fileInputStream = null;
                try {
                    fileInputStream = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                try {
                    byte[] bytes = new byte[fileInputStream.available()];
                    int len;// 记录每次读取的字节的个数
                    String str = null;
                    while ((len = fileInputStream.read(bytes)) != -1) {
                        str = new String(bytes, 0, len);
                    }
                    fileInputStream.close();
                    json_useruid = str;

                    zhanghaocunzai();

                    Log.d("text", "run: "+json_useruid);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    //判断账号是否存在
    private  void  zhanghaocunzai(){
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json_useruid);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        int a = jsonObject.length();
        if (a>0){
            TextView textView = findViewById(R.id.tishi);
            EditText editText = findViewById(R.id.get_uid);
            CardView cardView = findViewById(R.id.tianjiazhanghao);
            editText.setFocusable(false);
            cardView.setClickable(false);
            String b = null;
            try {
                b = jsonObject.getString("0");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            editText.setText(b);
            textView.setText("切换账号请先删除该账号！！");
        }
        else {
            CardView cardView1 = findViewById(R.id.shanchuzhanghao);
            cardView1.setClickable(false);
        }
    }

    //写文件操作方法
    private void weite_wenjian(String uid){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String userinfo_path = getExternalFilesDir(null).toString()+"/原琴工具箱小程序";
                File file = new File(userinfo_path,uid+".json");
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(json_uidall.getBytes());
                    fileOutputStream.close();
                    Log.d("text", "写操作完成" );
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                File file1 = new File(userinfo_path,"user_list.json");
                try {
                    FileOutputStream fileOutputStream1 = new FileOutputStream(file1);
                    try {
                        JSONObject jsonObject = new JSONObject(json_useruid);
                        String a = String.valueOf(jsonObject.length());
                        jsonObject.put( a,uid );
                        String json_list = jsonObject.toString();
                        try {
                            fileOutputStream1.write(json_list.getBytes());
                            fileOutputStream1.close();
                            Log.d("text", "写操作完成" );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
        this.finish();
    };

    //创建userinfo文件方法
    private boolean chuangjian_userinfo(String uid) {
        boolean a ;
        String dirPath = getExternalFilesDir(null).toString() + "/原琴工具箱小程序";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String dir_name = uid +".json";
        File temdir = new File(dirPath + "/" + dir_name);
        if (!temdir.exists()) {
            a = true;
            try {
                boolean b = temdir.createNewFile();
                Log.d("text", "文件创建成功:" + b + dir_name +"路径："+ temdir.getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            a = false;
        }
        return a;
    }
    //添加账号方法
    public void tianjiazhanghao(View view) {
        EditText get_uid = findViewById(R.id.get_uid);
        CardView cardView = findViewById(R.id.tianjiazhanghao);
        cardView.setClickable(false);
        String uid;
        String url;
        uid = get_uid.getText().toString();

        boolean a = chuangjian_userinfo(uid);
        if(a){
            Toast.makeText(this, "账号添加成功", Toast.LENGTH_SHORT).show();
            url = "https://enka.network/api/uid/"+uid+"/";
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            OkHttpClient client = new OkHttpClient();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = client.newCall(request).execute();
                        json_uidall = response.body().string();
                        weite_wenjian(uid);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }else {
            Toast.makeText(this, "账号已存在，操作终止", Toast.LENGTH_SHORT).show();
        }

    }
    //删除账号方法
    public void shanchuzhanghao(View view) {
        String dirPath = getExternalFilesDir(null).toString()+"/原琴工具箱小程序";
        File file = new File(dirPath);
        File[] files = file.listFiles();
        for (int i = 0 ;i<files.length;i++){
            if(files[i].isFile()){
                files[i].delete();
            }
        }
        File user_list1 = new File(dirPath+"/user_list.json");
        try {
            user_list1.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(user_list1);
            fileOutputStream.write("{}".getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Toast.makeText(this, "账号已删除", Toast.LENGTH_SHORT).show();
        this.finish();
    }
}

