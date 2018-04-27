package com.sorena.kitadaharuka.reco.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sorena.kitadaharuka.reco.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sorena.kitadaharuka.reco.models.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by kitadaharuka on 2018/03/12.
 */

public class LoginActivity extends BaseActivity {
    private final String TAG = "LoginActivity";

    @BindView(R.id.user_name) EditText mEditUserName;
    @BindView(R.id.password) EditText mEditPassword;
    @BindView(R.id.login) Button mLoginButton;
    @BindView(R.id.login_facebook) Button mLoginFacebook;

    private FirebaseDatabase database;
    private DatabaseReference ref;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_facebook_login]
    private CallbackManager mCallbackManager;
    private LoginManager mLoginManager;
    // [END declare_facebook_login]

    public static Intent newIntent(Context packageContext) {
        return new Intent(packageContext, LoginActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START initialize_facebook]
        mLoginManager = LoginManager.getInstance();
        // [END initialize_facebook]

        mCallbackManager = CallbackManager.Factory.create();
        mLoginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });

    }

    // [START on_start_check_user]
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            //Log.d(TAG, "login");
            Intent intent = EditActivity.newIntent(getApplicationContext());
            startActivity(intent);
        }
    }

    // [START on_start_check_user]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }
    // [END on_start_check_user]

    /**
     * handle facebook access token
     * @param token
     */
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInnWithCredential:success");
                            database = FirebaseDatabase.getInstance();
                            ref = database.getReference("users").child(getUid());
                            checkUserData();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        // [START_EXCLUDE silent]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }


    /**
     * sign in Firebase
     * @param email
     * @param password
     */
    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if(!validateForm()) {
            return;
        }
        showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail: success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Log.d(TAG, "uid:" + user.getUid());
                            Intent intent = EditActivity.newIntent(getApplicationContext());
                            startActivity(intent);
                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            //Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                        // [START_EXCLUDE]
                        if(!task.isSuccessful()) {
                        }

                        hideProgressDialog();
                    }
                });
        // [END sign_in_with_email]
    }

    /**
     * check input form
     * @return boolean valid
     */
    private boolean validateForm() {
        boolean valid = true;

        String email = mEditUserName.getText().toString();
        if(TextUtils.isEmpty(email)) {
            mEditUserName.setError("Required");
            valid = false;
        } else {
            mEditUserName.setError(null);
        }

        String password = mEditPassword.getText().toString();
        if(TextUtils.isEmpty(password)) {
            mEditPassword.setError("Required");
            valid = false;
        } else {
            mEditPassword.setError(null);
        }

        return valid;
    }

    @OnClick(R.id.sign_up)
    public void goToRegister() {
        Intent intent = RegisterActivity.newIntent(getApplicationContext());
        startActivity(intent);
    }

    @OnClick(R.id.login)
    public void goToEdit() {
        signIn(mEditUserName.getText().toString(), mEditPassword.getText().toString());
    }

    @OnClick(R.id.login_facebook)
    public void loginWithFacebook() {
        mLoginManager.logInWithReadPermissions(this, Arrays.asList("email","public_profile"));
    }

    private void checkUserData(){
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user == null) {
                    startActivity(WeightActivity.newIntent(getApplicationContext()));
                } else {
                    startActivity(EditActivity.newIntent(getApplicationContext()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}