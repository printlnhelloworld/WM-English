package com.example.yzt.wm_english.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yzt.wm_english.BasicActivity;
import com.example.yzt.wm_english.HttpUtil;
import com.example.yzt.wm_english.R;
import com.example.yzt.wm_english.main.MainActivity;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class Login extends BasicActivity implements View.OnClickListener {
    private static final String TAG = "Login";
    private EditText username;
    private EditText password;
    public static final int UPDATE_TEXT1 = 1;
    public static final int UPDATE_TEXT2 = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT1:
                    showToast("网络连接超时");
                    break;
                case UPDATE_TEXT2:
                    showToast("请输入正确的账号和密码");
                    break;
                default:
                    break;
            }
        }
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        Button login_Button = (Button) findViewById(R.id.login_Button);
        login_Button.setOnClickListener(this);
        Button register_Button = (Button) findViewById(R.id.register_Button);
        register_Button.setOnClickListener(this);
        Button forgetPwd_Button = (Button) findViewById(R.id.forgetPwd_Button);
        forgetPwd_Button.setOnClickListener(this);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_Button:
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();
                //验证邮箱和密码是否符合格式
                if (!(Validator.checkEmail(usernameText) || Validator.checkPassword(passwordText))) {
                    showToast("请输入正确的用户名/邮箱和密码");
                } else {
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
                                Status resJson = gson.fromJson(jsonData, Status.class);
                                if (resJson.getStatus() == 0) {
                                    Message message = new Message();
                                    message.what = UPDATE_TEXT2;
                                    handler.sendMessage(message);
                                } else if (resJson.getStatus() == 1 ) {
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d(TAG, "Gson解析错误");
                            }


                        }

                        @Override
                        public void onFailure(Call call, IOException e) {
                            //传送失败
                            Message message = new Message();
                            message.what = UPDATE_TEXT1;
                            handler.sendMessage(message);
                            e.printStackTrace();
                        }
                    });
                    Log.d(TAG, json);
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





    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Login Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
