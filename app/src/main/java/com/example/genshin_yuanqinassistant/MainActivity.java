package com.example.genshin_yuanqinassistant;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    //id操作参照表
    private JSONObject id_all ;
    //用户的uid本地储存数据
    private String json_useruid;
    //用户从网络获取的json数据
    private JSONObject json_wangluo;
    //本地数据库环境地址path
    private String path;
    //本地创建的用户json数据文件名称
    private  String user_list = "user_list.json";
    //用户uid
    private  String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //页面加载获取本地所有id参照表信息
        try {
            ReadId_xinxi();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //页面加载创建user_list文件
        try {
            chuangjian_userlist();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //获取本地数据文件地址
        path = getExternalFilesDir(null).toString()+"/原琴工具箱小程序";
        //运行获取本地创建的uid——json数据
        bendiuidshuju();
    }

    //返回时执行的代码
    @Override
    protected void onResume() {
        super.onResume();
        //运行获取本地创建的uid——json数据
        bendiuidshuju();
    }

    //获取用户本地创建的uid——json数据并创建uid实例
    private void bendiuidshuju(){
        File bendiyonghuuidxinxi = new File(path,user_list);
        try {
            FileInputStream fileInputStream = new FileInputStream(bendiyonghuuidxinxi);
            byte[] bytes = new byte[fileInputStream.available()];
            int len;// 记录每次读取的字节的个数
            String uid_json = null;
            while ((len = fileInputStream.read(bytes)) != -1) {
                uid_json = new String(bytes,0,len);
            }
            fileInputStream.close();
            JSONObject jsonObject = new JSONObject(uid_json);
            int changdu = jsonObject.length();
            if(changdu>0){
                TextView textView = findViewById(R.id.denluanniu);
                textView.setText("切换账号");
                uid = jsonObject.getString("0");
                shezhikongjianzhuangtai();
            }else {
                TextView textView = findViewById(R.id.denluanniu);
                textView.setText("添加账号");
                ImageView imageView = findViewById(R.id.touxiang);
                imageView.setImageResource(R.drawable.ganyu);
                TextView nicheng_kongjian = findViewById(R.id.nichen);
                nicheng_kongjian.setText("？？？？");
                TextView uid_kongjian=findViewById(R.id.uid);
                uid_kongjian.setText("？？？？？");
            }
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

    }
//获取从网络保存的uid——json数据并实例化uid，用户头像，昵称等
    private void shezhikongjianzhuangtai() {
        ImageView touxiang_kongjian = findViewById(R.id.touxiang);
        TextView nicheng_kongjian=findViewById(R.id.nichen);
        TextView uid_kongjian =findViewById(R.id.uid);
        //获取网络保存的用户json文件并解析
        File file = new File(path,uid+".json");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes =new byte[fileInputStream.available()];
            int len;
            String str = null;
            while ((len = fileInputStream.read(bytes))!=-1){
                str = new String(bytes,0,len);
            }
            fileInputStream.close();
            JSONObject jsonObject = new JSONObject(str);
            json_wangluo = jsonObject;
            int changdu1 = json_wangluo.length();
            if(changdu1>1){
                uid_kongjian.setText(uid);
                JSONObject playerInfo = json_wangluo.getJSONObject("playerInfo");
                String nicheng = playerInfo.getString("nickname");
                nicheng_kongjian.setText(nicheng);
                //获取头像id
                JSONObject profilePicture = playerInfo.getJSONObject("profilePicture");
                String avatarId =profilePicture.getString("avatarId");
                JSONObject id = id_all.getJSONObject(avatarId);
                String SideIconName = id.getString("SideIconName");
                int a = SideIconName.length();
                String SideIconName1 = SideIconName.substring(19);
                String url_touxiang = "https://enka.network/ui/UI_AvatarIcon_"+SideIconName1+".png";
                Log.d("text", "头像url: "+url_touxiang);
                File file1 = new File(path,SideIconName1+".png");
                if (file1.exists()){
                    //这里设置头像
                    touxiang_kongjian.setImageURI(Uri.fromFile(file1));
                }else {
                    file1.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file1);
                    Request request = new Request.Builder()
                            .url(url_touxiang)
                            .build();
                    OkHttpClient client = new OkHttpClient();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //保存图片
                                ResponseBody response = client.newCall(request).execute().body();
                                InputStream inputStream = response.byteStream();
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);
                                fileOutputStream.flush();
                                fileOutputStream.close();
                                Log.d("text", "图片保存成功: ");
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }).start();
                    ImageView imageView = findViewById(R.id.touxiang);
                    imageView.setImageResource(R.drawable.ganyu);
                }
            }
            else {
                File file1 = new File(path);
                File[] files = file1.listFiles();
                for (int i = 0 ;i<files.length;i++){
                    if(files[i].isFile()){
                        files[i].delete();
                    }
                }
                File user_list1 = new File(path+"/user_list.json");
                try {
                    user_list1.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(user_list1);
                    fileOutputStream.write("{}".getBytes());
                    fileOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }



        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    //    创建user_list文件方法
    private void chuangjian_userlist() throws IOException {
        String dirPath = getExternalFilesDir(null).toString() + "/原琴工具箱小程序";
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String dir_name = "user_list.json";
        File temdir = new File(dirPath + "/" + dir_name);
        if (!temdir.exists()) {
            try {
                temdir.createNewFile();
                String userinfo_path = getExternalFilesDir(null).toString()+"/原琴工具箱小程序";
                File file = new File(userinfo_path,"user_list.json");
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write("{}".getBytes());
                fileOutputStream.close();
                Log.d("text", "文件创建成功:" + dir_name +"路径："+ temdir.getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            File file = new File(dirPath,"user_list.json");
            FileInputStream inputStream = new FileInputStream(file);
            int a =  inputStream.available();
            inputStream.close();
            if (a == 0){
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write("{}".getBytes());
                fileOutputStream.close();
            }

        }
    }


    //读取对照id数据文件
    private void ReadId_xinxi() throws IOException {
        InputStream inputStream = getResources().openRawResource(R.raw.id_xinxi);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String jsonStr = "", line;
        while ((line = bufferedReader.readLine()) != null) {
            jsonStr += line;
        }
        try {
            JSONObject jsonObject =new JSONObject(jsonStr);
            id_all = jsonObject;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    //跳转祈愿页面
    public void tiaozhuan_qiyuan(View view) {
        Intent intent = new Intent();
        intent.setClass(this, qiyuanchaxun.class);
        startActivity(intent);
    }

    //跳转到账号管理页面方法
    public void show_denglu(View view) {
        Intent intent = new Intent();
        intent.setClass(this, denglu_zhongxin.class);
        startActivity(intent);
        //自定义弹窗
//        View view1 = LayoutInflater.from(MainActivity.this).inflate(R.layout.denglutanchuang,null,false);
//        final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setView(view1).create();
//        dialog.show();
    }

    public void xinyiliutu(View view) {
    }
}