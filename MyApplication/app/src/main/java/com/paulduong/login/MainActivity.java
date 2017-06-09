package com.paulduong.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    private final int REQUEST_CODE_SIGN_IN = 3;
    private static final String TAG = "SignInActivity";
    private ProgressDialog mProgressDialog;

    private  GoogleApiClient googleApiClient;
    SignInButton btn_sign_in;
    Button btn_Revoke, btn_Sign_Out;
    TextView txt_Hienthi;
    ImageView image_View_person_Photo;
    Uri personPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_sign_in = (SignInButton) findViewById(R.id.btn_Sign_in);
        btn_Sign_Out = (Button) findViewById(R.id.btn_sign_out);
        btn_Revoke = (Button) findViewById(R.id.btn_access);
        txt_Hienthi=(TextView)findViewById(R.id.txthienthi);
        image_View_person_Photo=(ImageView)findViewById(R.id.img_View_Person_Photo) ;

        // khai báo gg
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        btn_sign_in.setOnClickListener(this);
        btn_Sign_Out.setOnClickListener(this);
        btn_Revoke.setOnClickListener(this);

        btn_sign_in.setSize(SignInButton.SIZE_STANDARD);
    }








    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            ketquatrave(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    ketquatrave(googleSignInResult);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProgressDialog();
    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }


    // start Kết quả trả về hàm StartActivityforResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_SIGN_IN)) {
            // kết quả thông tin trả về
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            // lấy thông tin tài khoản
            GoogleSignInAccount acct = googleSignInResult.getSignInAccount();
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            personPhoto = acct.getPhotoUrl();

            ketquatrave(googleSignInResult);
        }

    }
    // end Kết quả trả về hàm StartActivityforResult


    // start signI
    private void signIn() {
        Intent intentSignIn = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intentSignIn, REQUEST_CODE_SIGN_IN);
    }
    // end signIn
    //start kết quả trả về và cập nhật giao diện
    private void ketquatrave(GoogleSignInResult result) {
        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();
            txt_Hienthi.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            image_View_person_Photo.setImageURI(personPhoto);


            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    // end

    // [start signOut]
    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    // [end signOut]

    // [start revokeAccess]
    private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
    //end revokeAccess

    // start updateUI
    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.btn_Sign_in).setVisibility(View.GONE);
            findViewById(R.id.btn_access).setVisibility(View.VISIBLE);

        } else {
            txt_Hienthi.setText(R.string.sign_out);

            findViewById(R.id.btn_Sign_in).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_access).setVisibility(View.GONE);
        }
    }
    // end updateUI

    //start trả về nếu kết nối thất bại
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"Kết nối thất bại"+ connectionResult);
    }
    //end

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btn_Sign_in:
                signIn();
                break;
            case R.id.btn_sign_out:
                signOut();
                break;
            case R.id.btn_access:
               revokeAccess();
                break;

        }

    }


}
