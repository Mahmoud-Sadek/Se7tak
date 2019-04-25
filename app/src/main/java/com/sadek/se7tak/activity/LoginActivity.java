package com.sadek.se7tak.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.sadek.se7tak.R;
import com.sadek.se7tak.dialogs.CompleteSocialLoginDialog;
import com.sadek.se7tak.firebase.FireAuth;
import com.sadek.se7tak.firebase.FireDatabase;
import com.sadek.se7tak.model.PatientUser;
import com.sadek.se7tak.utils.Common;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {


    private static final int RC_SIGN_IN = 999;
    @BindView(R.id.username_input)
    EditText usernameEdt;
    @BindView(R.id.password_input)
    EditText passwordEdt;
    LoginButton loginButton;
    CallbackManager mCallbackManager;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    TwitterLoginButton mLoginButton;
    FireDatabase fireDatabase;

    FireAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        Common.keyHash(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mAuth = FirebaseAuth.getInstance();
        auth = new FireAuth(this);
        fireDatabase = new FireDatabase(this);
        loginButton = findViewById(R.id.facebook_login_button);
        onFacebookButtonClick();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        //TWITTER
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(getString(R.string.twitter_consumer_key), getString(R.string.twitter_consumer_secret)))
                .debug(true)
                .build();
        Twitter.initialize(config);
        mLoginButton = findViewById(R.id.twitter_login_button);
        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d("Twitter", "twitterLogin:success" + result);
                handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.w("Twitter", "twitterLogin:failure", exception);
//                updateUI(null);
            }
        });

    }

    @OnClick(R.id.btnSignup)
    public void onButtonSignupClick(View view) {
        Intent signupIntent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(signupIntent);
    }
    @OnClick(R.id.btnLogin)
    void btnLogin(View view) {
        if (!validate()) {
            return;
        }
        auth.signIn(usernameEdt.getText().toString(), passwordEdt.getText().toString());
    }

    @OnClick(R.id.btnFacebook)
    public void btnFacebook(View view) {
        loginButton.performClick();
    }

    @OnClick(R.id.btnTwitter)
    public void btnTwitter(View view) {
        mLoginButton.performClick();
    }


    //google
    @OnClick(R.id.btnGoogle)
    public void btnGoogle(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.btnForgetPass)
    void btn_forget_password() {
        startActivity(new Intent(this, ForgetPasswordActivity.class));

    }
    private Boolean validate() {
        usernameEdt.setError(null);
        passwordEdt.setError(null);
        String email = usernameEdt.getText().toString();
        String pass = passwordEdt.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            usernameEdt.setError(getString(R.string.enter_valid_email));
            return false;
        } else if (pass.isEmpty() || pass.length() < 6) {
            passwordEdt.setError(getString(R.string.enter_valid_password));
            return false;
        }
        return true;

    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("google", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("google", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("google", "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.username_input), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    CompleteSocialLoginDialog completeSocialLoginDialog;
    private void updateUI(FirebaseUser user ) {
        PatientUser patientUser = new PatientUser();
        patientUser.setUserName(user.getDisplayName());
        patientUser.setEmail(user.getEmail());

        completeSocialLoginDialog = new CompleteSocialLoginDialog(this, new CompleteSocialLoginDialog.DialogInterAction() {
            @Override
            public void onDismissed() {
                mAuth.signOut();
                completeSocialLoginDialog.dismiss();
                Toast.makeText(LoginActivity.this, getString(R.string.you_dont_complete_regis), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSubmit(String phone, String gender) {
                patientUser.setPhoneNumber(phone);
                patientUser.setGender(gender);
                patientUser.setId(user.getUid());
                fireDatabase.addUser(patientUser);
            }
        });
        completeSocialLoginDialog.show();

    }

    //facebook
    public void onFacebookButtonClick() {
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();

        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("facebook", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d("facebook", "facebook:onCancel");
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("facebook", "facebook:onError", error);
                // ...
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("google", "Google sign in failed", e);
                // ...
            }
        }

        try {


            // Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);

            mLoginButton.onActivityResult(requestCode, resultCode, data);
        }catch (Exception e){

        }
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("facebook", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("facebook", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            LoginManager.getInstance().logOut();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("facebook", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    private void handleTwitterSession(TwitterSession session) {
        Log.d("Twitter", "handleTwitterSession:" + session);

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Twitter", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Twitter", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    @OnClick(R.id.skip_btn)
    public void onButtonSkipClick(View view) {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
