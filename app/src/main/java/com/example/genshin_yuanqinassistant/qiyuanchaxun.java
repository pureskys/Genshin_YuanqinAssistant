package com.example.genshin_yuanqinassistant;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class qiyuanchaxun extends AppCompatActivity {
    String name;//定义祈愿用户文件夹
    String gacha_type = "200";//卡池代码
    String page = "1";//页面代码（基本没用）
    String end_id = "0";//祈愿json数据最后物品的id（很重要）
    List<JSONObject> list_changzhu = new ArrayList<>();//常驻池list
    List<JSONObject> list_upjuese = new ArrayList<>();//up角色list
    List<JSONObject> list_wuqi = new ArrayList<>();//up武器list
    String path_yuanqingongjuxiang;//获取本地用户登录数据文件地址
    String path_qiyuanshuju; //祈愿数据文件夹";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qiyuanchaxun);
        path_yuanqingongjuxiang = getExternalFilesDir(null).toString() + "/原琴工具箱小程序";//获取本地用户登录数据文件地址
        path_qiyuanshuju = getExternalFilesDir(null).toString() + "/祈愿数据";
        wenjianjiancha();//运行文件检查方法
    }

    //文件检查器
    private void wenjianjiancha() {
        File qiyuan_wenjianjia = new File(path_qiyuanshuju);//祈愿数据文件夹
        File userinfo = new File(path_yuanqingongjuxiang, "user_list.json");//获取原琴工具箱地址下的用户数据表
        int changdu_yonghu;//记录用户表长度
        JSONObject yonghushuju;
        try {
            FileInputStream inputStream = new FileInputStream(userinfo);
            byte[] bytes = new byte[inputStream.available()];
            int len;
            String str = null;
            while ((len = inputStream.read(bytes)) != -1) {
                str = new String(bytes, 0, len);
            }
            inputStream.close();
            yonghushuju = new JSONObject(str);
            changdu_yonghu = yonghushuju.length();
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
        //判断是否创建祈愿文件夹
        if (!qiyuan_wenjianjia.exists() && changdu_yonghu > 0) {
            qiyuan_wenjianjia.mkdirs();
        }
        if (qiyuan_wenjianjia.exists() && changdu_yonghu > 0) {
            try {
                name = "/" + yonghushuju.getString("0");
                File yonghuqiyuanshujuwenjianjia = new File(path_qiyuanshuju + name);
                yonghuqiyuanshujuwenjianjia.mkdirs();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        //创建祈愿数据数据缓存
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = getExternalFilesDir(null).toString() + "/祈愿数据" + name + "/常驻祈愿.json";
                File file = new File(path);
                String path1 = getExternalFilesDir(null).toString()+ "/祈愿数据" + name + "/up角色.json";
                File file1 = new File(path1);
                String path2 = getExternalFilesDir(null).toString()+ "/祈愿数据" + name + "/up武器.json";
                File file2 = new File(path2);
                //常驻祈愿文件检查
                if (file.exists()) {
                    try {
                        FileInputStream inputStream = new FileInputStream(file);
                        byte[] bytes = new byte[inputStream.available()];
                        int len;
                        String changzhushuju = null;
                        if (!(inputStream.available() == 0)) {
                            while ((len = inputStream.read(bytes)) != -1) {
                                changzhushuju = new String(bytes, 0, len);
                            }
                            JSONArray jsonObjects = new JSONArray(changzhushuju);
                            List<JSONObject> jsonObjectList = new ArrayList<>();
                            for (int i = 0; i < jsonObjects.length(); i++) {
                                jsonObjectList.add(jsonObjects.getJSONObject(i));
                            }
                            list_changzhu.clear();
                            list_changzhu.addAll(jsonObjectList);
                        }
                        inputStream.close();
                        //Log.d("text", "run: " + list_changzhu);
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                //up角色祈愿文件检查
                if (file1.exists()) {
                    try {
                        FileInputStream inputStream = new FileInputStream(file1);
                        byte[] bytes = new byte[inputStream.available()];
                        int len;
                        String upjuese = null;
                        if (!(inputStream.available() == 0)) {
                            while ((len = inputStream.read(bytes)) != -1) {
                                upjuese = new String(bytes, 0, len);
                            }
                            JSONArray jsonObjects = new JSONArray(upjuese);
                            List<JSONObject> jsonObjectList = new ArrayList<>();
                            for (int i = 0; i < jsonObjects.length(); i++) {
                                jsonObjectList.add(jsonObjects.getJSONObject(i));
                            }
                            list_upjuese.clear();
                            list_upjuese.addAll(jsonObjectList);
                        }
                        inputStream.close();
                        //Log.d("text", "run: " + list_changzhu);
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                //up武器祈愿文件检查
                if (file2.exists()) {
                    try {
                        FileInputStream inputStream = new FileInputStream(file2);
                        byte[] bytes = new byte[inputStream.available()];
                        int len;
                        String upwuqi = null;
                        if (!(inputStream.available() == 0)) {
                            while ((len = inputStream.read(bytes)) != -1) {
                                upwuqi = new String(bytes, 0, len);
                            }
                            JSONArray jsonObjects = new JSONArray(upwuqi);
                            List<JSONObject> jsonObjectList = new ArrayList<>();
                            for (int i = 0; i < jsonObjects.length(); i++) {
                                jsonObjectList.add(jsonObjects.getJSONObject(i));
                            }
                            list_wuqi.clear();
                            list_wuqi.addAll(jsonObjectList);
                        }
                        inputStream.close();
                        //Log.d("text", "run: " + list_changzhu);
                    } catch (IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }


            }
        }).start();


    }

    //祈愿查询方法
    private void qiyuanchaxun() {
        List<JSONObject> changzhu_list = new ArrayList<>();//常驻池暂存list
        Set<Qiyuanjson> qiyuanjsons = new LinkedHashSet<>();//常驻的去重用的自定义类
        List<JSONObject> upjuese_list = new ArrayList<>();//up角色暂存list
        Set<Qiyuanjson> upjuese_quchong = new LinkedHashSet<>();//up角色池去重自定义类
        List<JSONObject> upwuqi_list = new ArrayList<>();//up武器暂存list
        Set<Qiyuanjson> upwuqi_quchong = new LinkedHashSet<>();//up武器池去重自定义类
        EditText qiyuan_url = findViewById(R.id.qiyuan_url);




        String url_qiyuan = qiyuan_url.getText().toString();//获取祈愿链接





        String url_lite = url_qiyuan.replace("gacha_type=200&page=1&end_id=0", "");//获取参数分离后的yrl
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String url_whole = url_lite + "gacha_type=" + gacha_type + "&page=" + page + "&end_id=" + end_id;
                    Request request = new Request.Builder()
                            .url(url_whole)
                            .build();
                    OkHttpClient client = new OkHttpClient();

                    //常驻祈愿操作
                    if (gacha_type == "200") {
                        String path = getExternalFilesDir(null).toString() + "/祈愿数据" + name + "/常驻祈愿.json";
                        File file = new File(path);
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        try {
                            Response response = client.newCall(request).execute();
                            String responseBody = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseBody);
                            JSONObject data = (JSONObject) jsonObject.getJSONObject("data");
                            JSONArray list = data.getJSONArray("list");
                            int a = list.length();//获取list列表长度，作为遍历次数关键变量
                            for (int i = 0; i < a; i++) {
                                changzhu_list.add(list.getJSONObject(i));
                            }
                            list_changzhu.addAll(changzhu_list);
                            //拿到数据库和网络合并的数据，并去除重复元素保持顺序不变
                            JSONArray jsonArray = new JSONArray(list_changzhu);
                            int b = jsonArray.length();
                            for (int i = 0; i < b; i++) {
                                String uid;
                                String gacha_type;
                                String item_id;
                                String count;
                                String time;
                                String name;
                                String lang;
                                String item_type;
                                String rank_type;
                                String id;
                                uid = jsonArray.getJSONObject(i).getString("uid");
                                gacha_type = jsonArray.getJSONObject(i).getString("gacha_type");
                                item_id = jsonArray.getJSONObject(i).getString("item_id");
                                count = jsonArray.getJSONObject(i).getString("count");
                                time = jsonArray.getJSONObject(i).getString("time");
                                name = jsonArray.getJSONObject(i).getString("name");
                                lang = jsonArray.getJSONObject(i).getString("lang");
                                item_type = jsonArray.getJSONObject(i).getString("item_type");
                                rank_type = jsonArray.getJSONObject(i).getString("rank_type");
                                id = jsonArray.getJSONObject(i).getString("id");
                                qiyuanjsons.add(new Qiyuanjson(uid, gacha_type, item_id, count, time, name, lang, item_type, rank_type, id));
                            }
                            int c = list.length();//网络数据长度
                            if (c>0){
                            int changzhuchangdu = c-1;//获取常驻祈愿数据的最后一个数组下标
                            JSONObject changzhudezuihouyigeshuzu = list.getJSONObject(changzhuchangdu);
                            String id_changzhu = changzhudezuihouyigeshuzu.getString("id");
                            end_id = id_changzhu;
                            } else if(c==0){
                                //将常驻祈愿数据存入本地数据表开始
                                String d = qiyuanjsons.toString();//将list转化为string方便存到数据表
                                FileOutputStream outputStream = new FileOutputStream(file);
                                outputStream.write(d.getBytes());
                                outputStream.close();
                                wenjianjiancha();
                                gacha_type = "301";
                                end_id = "0";
                                continue;
                                //将常驻祈愿数据存入本地数据表结束
                            }

                        } catch (IOException | JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    //角色up祈愿操作
                    if (gacha_type == "301") {
                        String path = getExternalFilesDir(null).toString() + "/祈愿数据" + name + "/up角色.json";
                        File file = new File(path);
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        try {
                            Response response = client.newCall(request).execute();
                            String responseBody = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseBody);
                            JSONObject data = (JSONObject) jsonObject.getJSONObject("data");
                            JSONArray list = data.getJSONArray("list");
                            int a = list.length();//获取list列表长度，作为遍历次数关键变量
                            for (int i = 0; i < a; i++) {
                                upjuese_list.add(list.getJSONObject(i));
                            }
                            list_upjuese.addAll(upjuese_list);
                            //拿到数据库和网络合并的数据，并去除重复元素保持顺序不变
                            JSONArray jsonArray = new JSONArray(list_upjuese);
                            int b = jsonArray.length();
                            for (int i = 0; i < b; i++) {
                                String uid;
                                String gacha_type;
                                String item_id;
                                String count;
                                String time;
                                String name;
                                String lang;
                                String item_type;
                                String rank_type;
                                String id;
                                uid = jsonArray.getJSONObject(i).getString("uid");
                                gacha_type = jsonArray.getJSONObject(i).getString("gacha_type");
                                item_id = jsonArray.getJSONObject(i).getString("item_id");
                                count = jsonArray.getJSONObject(i).getString("count");
                                time = jsonArray.getJSONObject(i).getString("time");
                                name = jsonArray.getJSONObject(i).getString("name");
                                lang = jsonArray.getJSONObject(i).getString("lang");
                                item_type = jsonArray.getJSONObject(i).getString("item_type");
                                rank_type = jsonArray.getJSONObject(i).getString("rank_type");
                                id = jsonArray.getJSONObject(i).getString("id");
                                upjuese_quchong.add(new Qiyuanjson(uid, gacha_type, item_id, count, time, name, lang, item_type, rank_type, id));
                            }
                            int c = list.length();//网络数据长度
                            if (c>0){
                                int upjuese = c-1;//获取常驻祈愿数据的最后一个数组下标
                                JSONObject changzhudezuihouyigeshuzu = list.getJSONObject(upjuese);
                                String id_upjuese = changzhudezuihouyigeshuzu.getString("id");
                                end_id = id_upjuese;
                                continue;
                            } else if(c==0){
                                //将up角色祈愿数据存入本地数据表开始
                                String d = upjuese_quchong.toString();//将list转化为string方便存到数据表
                                FileOutputStream outputStream = new FileOutputStream(file);
                                outputStream.write(d.getBytes());
                                outputStream.close();
                                wenjianjiancha();
                                gacha_type = "302";
                                end_id= "0";
                                break;
                                //将常驻祈愿数据存入本地数据表结束
                            }
                        } catch (IOException | JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    //武器up祈愿操作
                    if (gacha_type == "302") {
                        String path = getExternalFilesDir(null).toString() + "/祈愿数据" + name + "/up武器.json";
                        File file = new File(path);
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        try {
                            Response response = client.newCall(request).execute();
                            String responseBody = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseBody);
                            JSONObject data = (JSONObject) jsonObject.getJSONObject("data");
                            JSONArray list = data.getJSONArray("list");
                            int a = list.length();//获取list列表长度，作为遍历次数关键变量
                            for (int i = 0; i < a; i++) {
                                upwuqi_list.add(list.getJSONObject(i));
                            }
                            list_wuqi.addAll(upwuqi_list);
                            //拿到数据库和网络合并的数据，并去除重复元素保持顺序不变
                            JSONArray jsonArray = new JSONArray(list_wuqi);
                            int b = jsonArray.length();
                            for (int i = 0; i < b; i++) {
                                String uid;
                                String gacha_type;
                                String item_id;
                                String count;
                                String time;
                                String name;
                                String lang;
                                String item_type;
                                String rank_type;
                                String id;
                                uid = jsonArray.getJSONObject(i).getString("uid");
                                gacha_type = jsonArray.getJSONObject(i).getString("gacha_type");
                                item_id = jsonArray.getJSONObject(i).getString("item_id");
                                count = jsonArray.getJSONObject(i).getString("count");
                                time = jsonArray.getJSONObject(i).getString("time");
                                name = jsonArray.getJSONObject(i).getString("name");
                                lang = jsonArray.getJSONObject(i).getString("lang");
                                item_type = jsonArray.getJSONObject(i).getString("item_type");
                                rank_type = jsonArray.getJSONObject(i).getString("rank_type");
                                id = jsonArray.getJSONObject(i).getString("id");
                                upwuqi_quchong.add(new Qiyuanjson(uid, gacha_type, item_id, count, time, name, lang, item_type, rank_type, id));
                            }
                            int c = list.length();//网络数据长度
                            if (c>0){
                                int upwuqi = c-1;//获取常驻祈愿数据的最后一个数组下标
                                JSONObject changzhudezuihouyigeshuzu = list.getJSONObject(upwuqi);
                                String id_upwuqi = changzhudezuihouyigeshuzu.getString("id");
                                end_id = id_upwuqi;
                                Log.d("text", "up武器id: "+end_id);
                                continue;
                            } else if(c==0){
                                //将up武器祈愿数据存入本地数据表开始
                                String d = upwuqi_quchong.toString();//将list转化为string方便存到数据表
                                FileOutputStream outputStream = new FileOutputStream(file);
                                outputStream.write(d.getBytes());
                                outputStream.close();
                                wenjianjiancha();
                                gacha_type = "302";
                                end_id= "0";
                                Log.d("text", "up武器list: "+upwuqi_quchong);
                                break;
                                //将up武器祈愿数据存入本地数据表结束
                            }
                        } catch (IOException | JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }).start();
//循环结束

    }

    //点击查询按钮点击事件
    public void dianjichaxun(View view) {
        qiyuanchaxun();
    }
}