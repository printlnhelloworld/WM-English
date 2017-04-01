package com.example.yzt.wm_english.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yzt.wm_english.R;
import com.example.yzt.wm_english.Units.HttpUtil;
import com.example.yzt.wm_english.Units.ToastUtils;
import com.example.yzt.wm_english.main.MainActivity;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "Login";
    private EditText username;
    private EditText password;
    public static final int UPDATE_TEXT1 = 1;
    public static final int UPDATE_TEXT2 = 2;
    String usernameText;
    String passwordText;
    com.example.yzt.wm_english.login.Status resJson;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        init();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    public void init() {
        Button login_Button = (Button) findViewById(R.id.login_Button);
        login_Button.setOnClickListener(this);
        Button register_Button = (Button) findViewById(R.id.register_Button);
        register_Button.setOnClickListener(this);
        Button forgetPwd_Button = (Button) findViewById(R.id.forgetPwd_Button);
        forgetPwd_Button.setOnClickListener(this);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        usernameText = pref.getString("userName", "");
        username.setText(usernameText);
        passwordText = pref.getString("password", "");
        password.setText(passwordText);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_Button:
                usernameText = username.getText().toString();
                passwordText = password.getText().toString();
                //验证邮箱和密码是否符合格式
                if (!(Validator.checkEmail(usernameText) || Validator.checkPassword(passwordText))) {
                    ToastUtils.showToast(Login.this, "请输入正确的用户名/邮箱和密码");
                } else {
                    new DownloadTask().execute();
                }
                break;
            case R.id.register_Button:
                Intent intent = new Intent(this, register.class);
                startActivity(intent);
                break;

            case R.id.forgetPwd_Button:
                break;
            default:
                break;
        }
    }

    class DownloadTask extends AsyncTask<String, Integer, Integer> {
        ProgressDialog dialog = new ProgressDialog(Login.this);

        @Override
        protected void onPreExecute() {
            dialog.show();
            dialog.setMessage("Loading...");
        }

        @Override
        protected Integer doInBackground(String... params) {
            //账号密码生成json字符串
//                    RequestBody requestBody = new FormBody.Builder()
//                            .add("account",usernameText ).add("password", passwordText)
//                            .build();
            LoginAccount loginAccount = new LoginAccount(usernameText, passwordText);
            Gson gson = new Gson();
            String json = gson.toJson(loginAccount);
            //向后台传送登录的账号和密码并获取返回值
            HttpUtil.postJson("http://lincloud.me:8080/app/app_login", json, new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    //成功获取返回值;
                    try {
                        Gson gson = new Gson();
                        //OkHttp请求回调中response.body().string()只能有效调用一次
                        String jsonData = response.body().string();
                        Log.d(TAG, jsonData);
                        resJson = gson.fromJson(jsonData, com.example.yzt.wm_english.login.Status.class);
                        publishProgress(resJson.getStatus());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "Gson解析错误");
                    }


                }

                @Override
                public void onFailure(Call call, IOException e) {
                    //传送失败
                    publishProgress(2);
                }
            });
            Log.d(TAG, json);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            switch (values[0]) {
                case 0:
                    ToastUtils.showToast(Login.this, "请输入正确的账号和密码");
                    break;
                case 1:
                    SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                    editor.putString("userName", usernameText);
                    editor.putString("password", passwordText);
                    editor.putString("nickName", resJson.nickName);
                    editor.putInt("id", resJson.getid());
                    editor.apply();
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                    finish();
                    break;
                case 2:
                    ToastUtils.showToast(Login.this, "网络连接超时");
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    }
}




