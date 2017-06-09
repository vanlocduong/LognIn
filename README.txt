-add sdk facebook r?i d?ng b?
//app
	repositories {
  mavenCentral() 
}
//  grade chung
dependencies { 
  compile 'com.facebook.android:facebook-android-sdk:4.+'
}
-l?y keyHash copy do?n code n�y v�o m?t chuong tr�nh b?t k?
keytool -exportcert -alias androiddebugkey -keystore %HOMEPATH%\.android\debug.keystore | openssl sha1 -binary | openssl base64
trong cmd
	-thu du?c keyHash trong log
	  try {
        PackageInfo info = getPackageManager().getPackageInfo(
		// doi lai pakage cua minh
                "com.facebook.samples.hellofacebook", 
                PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
    } catch (NameNotFoundException e) {
        
    } catch (NoSuchAlgorithmException e) {
        
    }
QhdBJfxRC2+a344+PxtUVMNZ5iY=
// trong log se nhan duoc keyhash
-Th�m v�o String chu?i
	// app id cua chung ta
    <string name="facebook_app_id">231461257259051</string>
	// add meta-data trong application 
        <meta-data android:name="com.facebook.sdk.ApplicationId" android:value="@string/facebook_app_id"/>
-th�m b? l?c
<string name="fb_login_protocol_scheme">fb231461257259051</string>
 <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <data android:scheme="@string/fb_login_protocol_scheme" />
    </intent-filter>

	
	
code:
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

code:main
package com.paulduong.facebooksignin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ImageView img_photo;
    private TextView txt_name;
    private TextView txt_email;
    private TextView txt_id;
    private TextView txt_First_Name;

    private ProfileTracker profileTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img_photo = (ImageView) findViewById(R.id.photoImageView);
        txt_name = (TextView) findViewById(R.id.nameTextView);
        txt_email = (TextView) findViewById(R.id.emailTextView);
        txt_id = (TextView) findViewById(R.id.idTextView);
        txt_First_Name = (TextView) findViewById(R.id.first_NameTextView);

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    HienThi_Thongtin(currentProfile);
                }
            }
        };

        if (AccessToken.getCurrentAccessToken() == null) {
            go_Dang_Nhap();
        } else {
            requestEmail(AccessToken.getCurrentAccessToken());

            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
                HienThi_Thongtin(profile);
            } else {
                Profile.fetchProfileForCurrentAccessToken();
            }
        }
    }



    private void requestEmail(AccessToken currentAccessToken) {
        // tạo một yêu cầu lấy hồ sơ cá nhân của người dùng
        GraphRequest request = GraphRequest.newMeRequest(currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (response.getError() != null) {
                    Toast.makeText(getApplicationContext(), response.getError().getErrorMessage(), Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                    // truyền email cho phuong thuc setEmail khởi tạo ở dưới
                    String email = object.getString("email");
                    setEmail(email);
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, first_name, last_name, email, gender, birthday, location");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void setEmail(String email) {
        txt_email.setText(email);
    }




    // start hien thi thong tin
    private  void HienThi_Thongtin(Profile profile){
        String id = profile.getId();
        String name =profile.getName();
        String firstName = profile.getFirstName();

        String url_Anh=profile.getProfilePictureUri(100,100).toString();



        txt_id.setText(id);
        txt_name.setText(name);
        txt_First_Name.setText(firstName);

        // dùng thư  viện Glide để load ảnh về
        Glide.with(getApplicationContext())
                .load(url_Anh)
                .into(img_photo);
    }

    // end hien thi thong tin


    // start dangnhap den LoginActivity
    private  void go_Dang_Nhap(){
        Intent intent_DangNhap= new Intent(this,LoginActivity.class);
        intent_DangNhap.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent_DangNhap);
    }
    //end dangnhap

    // start logout, gan su kien click trong file xml
    public void logout(View view) {
        LoginManager.getInstance().logOut();
        go_Dang_Nhap();
    }
    //end logout

    // nhận thông báo thay đổi của profile nên được có
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // dùng việc tracking profile
        profileTracker.stopTracking();
    }
}
	
	
	
	

//ham FacebookSdk ph?i n?m tru?c h�m view
--- Mu?n dang nh?p facebook ph?i c� 3 c�i
	Kh?i t?o facebook,t?o m?t callback l?ng nge,g?i phuong ph?c loginManager
	FacebookSdk.sdkInitialize(getContext().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() 
-trong buttonclick 
	  LoginManager.getInstance().logInWithReadPermissions(FragmentDangNhap.this, Arrays.asList("public_profile","user_friends","email"));

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        Log.d("dkm","Co chayj");
        callbackManager.onActivityResult(requestCode,resultCode,data);

    }
https://www.androidtutorialpoint.com/material-design/android-facebook-login-tutorial/
