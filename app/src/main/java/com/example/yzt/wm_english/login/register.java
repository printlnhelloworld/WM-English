package com.example.yzt.wm_english.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yzt.wm_english.Units.BasicActivity;
import com.example.yzt.wm_english.Units.HttpUtil;
import com.example.yzt.wm_english.R;
import com.example.yzt.wm_english.Units.ToastUtils;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

public class register extends BasicActivity implements View.OnClickListener{
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText repeated;
    private static final String TAG = "register";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        username = (EditText) findViewById(R.id.username);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        repeated = (EditText) findViewById(R.id.repeated);
        repeated.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        Button register_Button = (Button) findViewById(R.id.register_Button);
        register_Button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_Button:
                String usernameText = username.getText().toString();
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();
                String repeatedText = repeated.getText().toString();
                if (!Validator.checkUsername(usernameText)){
                    ToastUtils.showToast(register.this, "用户名请以字母为开头,并限制在6-16字符之间");
                } else if (!Validator.checkEmail(emailText)) {
                    ToastUtils.showToast(register.this, "请输入正确的邮箱地址");
                } else if (!Validator.checkPassword(passwordText)) {
                    ToastUtils.showToast(register.this, "密码请不要包含特殊字符，并限制在6-16字节之间");
                } else if (!passwordText.equals(repeatedText)) {
                    ToastUtils.showToast(register.this, "两次输入的密码不相同");
                } else {
//                    RequestBody requestBody = new FormBody.Builder()
//                            .add("email",emailText ).add("password", passwordText).add("nickname",usernameText)
//                            .build();
                    RegisterAccount loginAccount = new RegisterAccount(usernameText, emailText, passwordText);
                    Gson gson = new Gson();
                    String json = gson.toJson(loginAccount);
                    Log.d(TAG, json);
                    HttpUtil.postJson("http://lincloud.me:8080/app/app_register_email", json, new okhttp3.Callback() {
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                String jsonData = response.body().string();
                                Gson gson = new Gson();
                                Log.d(TAG,jsonData);
                                Status resJson = gson.fromJson(jsonData,Status.class);
                                if (resJson.getStatus() == 0) {
                                    ToastUtils.showToast(register.this, "注册成功");
                                    Intent intent = new Intent(register.this,LoginSuccess.class);
                                    startActivity(intent);
                                } else if (resJson.getStatus() == 1) {
                                    ToastUtils.showToast(register.this, "该用户名已被占用");
                                } else if (resJson.getStatus() == 2) {
                                    ToastUtils.showToast(register.this, "该邮箱已被注册");
                                }else{
                                    ToastUtils.showToast(register.this, "发生未知错误");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d(TAG, "gson解析错误");
                            }

                        }

                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
                break;
            default:
                break;
        }
    }



}
