package com.paulduong.facebooksignin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by PaulDuong on 6/9/2017.
 */

public class LoginActivity extends AppCompatActivity {
    private LoginButton btn_login;
    private CallbackManager callbackManager;
    private static  int REQUEST_CODE_LOGIN= 3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        // tạo một trình quản lý gọi lại nhằm phản hồi đăng nhập
        callbackManager= CallbackManager.Factory.create();

        btn_login = (LoginButton)findViewById(R.id.btn_loginfacebook);

        // thiết lập quyền
        btn_login.setReadPermissions("email");


        btn_login.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                goManHinh_Activity();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }
    private  void goManHinh_Activity(){
        Intent intent_Activity= new Intent(this, MainActivity.class);
        intent_Activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent_Activity,REQUEST_CODE_LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}
