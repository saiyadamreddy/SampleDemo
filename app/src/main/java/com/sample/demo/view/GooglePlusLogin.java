package com.sample.demo.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.sample.demo.R;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.Observer;
import rx.schedulers.Schedulers;

public class GooglePlusLogin extends Activity implements OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;
    // Google client to communicate with Google
    private GoogleApiClient mGoogleApiClient;

    private boolean mIntentInProgress;
    private boolean signedInUser;
    private ConnectionResult mConnectionResult;
    private SignInButton signinButton;
    private ProgressDialog progressDialog;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        signinButton = (SignInButton) findViewById(R.id.sign_in_button);
        signinButton.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        connectToGooglePlus(progressDialog);
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this, 0).show();
            return;
        }
        if (!mIntentInProgress) {
            mConnectionResult = result;
            if (signedInUser) {
                resolveSignInError();
            }
        }
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        switch (requestCode) {
            case RC_SIGN_IN:
                if (responseCode == RESULT_OK) {
                    signedInUser = false;
                }
                mIntentInProgress = false;
                if (!mGoogleApiClient.isConnecting()) {
                    if (progressDialog != null && !progressDialog.isShowing()) {
                        progressDialog.show();
                    }
                    mGoogleApiClient.connect();
                }
                break;
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        signedInUser = false;
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Intent intent = new Intent(GooglePlusLogin.this, BookListView.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        mGoogleApiClient.connect();
        Intent intent = new Intent(GooglePlusLogin.this, BookListView.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                googlePlusLogin();
                break;
        }
    }


    private void googlePlusLogin() {
        if (!mGoogleApiClient.isConnecting()) {
            signedInUser = true;
            resolveSignInError();
        }
    }

    private void googlePlusLogout() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }

    public void connectToGooglePlus(final ProgressDialog progressDialog) {
        final Observable<Void> bookList = Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() {
                mGoogleApiClient = new GoogleApiClient.Builder(GooglePlusLogin.this).addConnectionCallbacks(GooglePlusLogin.this).addOnConnectionFailedListener(GooglePlusLogin.this).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();
                mGoogleApiClient.connect();
                return null;
            }
        });

        Observable<Void> observable;
        observable = bookList.subscribeOn(Schedulers.io());
        observable.subscribe(new Observer<Void>() {
            @Override
            public void onCompleted() {
                Log.d("connection", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("connection", "onError");
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onNext(@NonNull Void data) {
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }


}