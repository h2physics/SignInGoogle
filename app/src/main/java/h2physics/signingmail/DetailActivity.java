package h2physics.signingmail;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class DetailActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    public static final String LOG_TAG = DetailActivity.class.getSimpleName();
    private GoogleSignInOptions mGoogleSignInOption;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        initData();
    }

    private void initView(){
        Button mSignOut = (Button) findViewById(R.id.btn_sign_out);
        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
        mGoogleSignInOption = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOption)
                .build();
        Intent intent = getIntent();
        GoogleSignInAccount result = intent.getParcelableExtra("SignIn");
        ((TextView) findViewById(R.id.tv_info)).setText(result.getDisplayName() + "\n" + result.getEmail());
    }

    private void initData(){

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()){
                    startActivity(new Intent(DetailActivity.this, MainActivity.class));
                    finish();
                    Log.e(LOG_TAG, "Sign out successfully");
                } else {
                    Log.e(LOG_TAG, "Sign out unsuccessfully");
                }
            }
        });
    }
}
