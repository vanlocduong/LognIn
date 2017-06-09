https://github.com/googlesamples/google-services/blob/master/android/signin/app/src/main/java/com/google/samples/quickstart/signin/SignInActivity.java#L59-L64

-add thư viện
compile 'com.google.firebase:firebase-auth:10.2.6'
compile 'com.google.android.gms:play-services-auth:10.2.6'

https://developers.google.com/mobile/add?platform=android&cntapi=signin&cnturl=https:%2F%2Fdevelopers.google.com%2Fidentity%2Fsign-in%2Fandroid%2Fsign-in%3Fconfigured%3Dtrue&cntlbl=Continue%20Adding%20Sign-In
-lấy mã SHA-1


    SHA-1 certificate fingerprint:
    D0:7D:0A:AF:80:BF:FE:8C:FB:0C:D8:85:C6:67:9D:23:FC:C8:EA:62
-laays service_Json
-add classpath 'com.google.gms:google-services:3.0.0' yourproject
-add apply plugin: 'com.google.gms.google-services' yourapp, compile 'com.google.android.gms:play-services-auth:9.8.0'
-Credentials page https://console.developers.google.com/apis/credentials?project=testsignin-169723

client id :  850135650773-qm9l4qr30n0e2n9a7oq2gagldkupsdpk.apps.googleusercontent.com
client :pass  TGiJ5p3yRI_bbOxO9xYe92F6


-GoogleSignAPIOption
-GoogleSignInAPI vào hàm oncreate
GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();

			// nhan vao ggoption, va api
    GoogleApiClient googleAent= new GoogleApiClient.Builder(this)
            .enableAutoManage(this,this)
            .addApi(Auth.GOOGLpiCliE_SIGN_IN_API,gso)
            .build();

- Taoj Button Signin <com.google.android.gms.common.SignInButton
 android:id="@+id/sign_in_button"
 android:layout_width="wrap_content"
 android:layout_height="wrap_content" />

 - gán button
 -tạo method signIn();
 
    private void signOut()
	private void revokeAccess()


-GoogleSignAPIOption
-GoogleSignInAPI