package com.example.yzt.wm_english.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yzt.wm_english.BasicActivity;
import com.example.yzt.wm_english.HttpUtil;
import com.example.yzt.wm_english.R;
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
                    showToast("用户名请以字母为开头,并限制在6-16字符之间");
                } else if (!Validator.checkEmail(emailText)) {
                    showToast("请输入正确的邮箱地址");
                } else if (!Validator.checkPassword(passwordText)) {
                    showToast("密码请不要包含特殊字符，并限制在6-16字节之间");
                } else if (!passwordText.equals(repeatedText)) {
                    showToast("两次输入的密码不相同");
                } else {
                    RegisterAccount loginAccount = new RegisterAccount(usernameText, emailText, passwordText);
                    Gson gson = new Gson();
                    String json = gson.toJson(loginAccount);
                    HttpUtil.postJson("", json, new okhttp3.Callback() {
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Gson gson = new Gson();
                            Status resJson = gson.fromJson(response.body().string(),Status.class);
                            if (resJson.status.equals("0")) {
                                showToast("注册成功");
                                Intent intent = new Intent(register.this,LoginSuccess.class);
                                startActivity(intent);
                            } else if (resJson.status.equals("1")) {
                                showToast("该用户名已被占用");
                            } else if (resJson.status.equals("2")) {
                                showToast("该邮箱已被注册");
                            }else{
                                showToast("发生未知错误");
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
