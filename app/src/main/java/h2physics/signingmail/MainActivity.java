package h2physics.signingmail;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    public static final String LOG_TAG = MainActivity.class.getSimpleName();
    private GoogleSignInOptions mGoogleSignInOption;
    private GoogleApiClient mGoogleApiClient;

    private static final int SIGN_IN_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                Log.e(LOG_TAG, account.getDisplayName() + " - " + account.getEmail());
//                Intent intent = new Intent(this, DetailActivity.class);

//                intent.putExtra("SignIn", account);
//                startActivity(intent);
//                finish();
                new GetAccessToken().execute(account);
            } else {
                Log.e(LOG_TAG, "Login unsuccessfully");
            }
        }
    }

    private void initView(){
        Button mSignIn = (Button) findViewById(R.id.btn_sign_in);
        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        mGoogleSignInOption = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOption)
                .build();
    }

    private void initData(){

    }

    private void signIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, SIGN_IN_CODE);
    }

    private class GetAccessToken extends AsyncTask<GoogleSignInAccount, Void, String> {

        @Override
        protected String doInBackground(GoogleSignInAccount... accounts) {
            String token = null;

            try {
                token = GoogleAuthUtil.getToken(getApplicationContext(), accounts[0].getEmail(), "oauth2:profile email");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (GoogleAuthException e) {
                e.printStackTrace();
            }

            return token;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e(LOG_TAG, "Access token: " + s);
        }
    }


}
