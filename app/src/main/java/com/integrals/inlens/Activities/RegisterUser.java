package com.integrals.inlens.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.vistrav.ask.Ask;

import java.util.HashMap;

import com.integrals.inlens.R;

public class RegisterUser extends AppCompatActivity {
    private EditText mDisplayName;
    private EditText mEmail;
    private EditText mPassword;
    private EditText ReTypePassword;

    private Button mCreateBtn;
    private Button VerifiedButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog progressDialog;

    private RelativeLayout RootForRegisterActivity;

    private Boolean SHOW_PASSWORD = false, SHOW_PASSWORD_CONFIRM = false;
    private ImageButton ShowPasswordBtn,ShowPasswordBtnVisON , ShowPasswordConfirmBtn,ShowPasswordConfirmBtnVisON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ShowPasswordBtn = findViewById(R.id.reg_show_password);
        ShowPasswordBtnVisON = findViewById(R.id.reg_show_password_vis_on);
        ShowPasswordConfirmBtn = findViewById(R.id.reg_confirm_show_password);
        ShowPasswordConfirmBtnVisON = findViewById(R.id.reg_confirm_show_password_vis_on);

        RootForRegisterActivity = findViewById(R.id.root_for_register_activity);
        mAuth = FirebaseAuth.getInstance();
        mDisplayName = (EditText) findViewById(R.id.NameField);
        VerifiedButton = (Button) findViewById(R.id.VerifiedButton);
        mEmail = (EditText) findViewById(R.id.EmailField);
        mPassword = (EditText) findViewById(R.id.Password);
        ReTypePassword = findViewById(R.id.RePassword);
        mCreateBtn = (Button) findViewById(R.id.ProceedButton);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        VerifiedButton.setEnabled(false);

        ShowPasswordConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(ReTypePassword.getText()))
                {
                    if(!SHOW_PASSWORD_CONFIRM)
                    {
                        SHOW_PASSWORD_CONFIRM=true;
                        ReTypePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        ShowPasswordConfirmBtn.setVisibility(View.GONE);
                        ShowPasswordConfirmBtnVisON.setVisibility(View.VISIBLE);
                    }

                }
            }
        });

        ShowPasswordConfirmBtnVisON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(SHOW_PASSWORD_CONFIRM && !TextUtils.isEmpty(ReTypePassword.getText()))
                {
                    SHOW_PASSWORD_CONFIRM = false;
                    ReTypePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ShowPasswordConfirmBtn.setVisibility(View.VISIBLE);
                    ShowPasswordConfirmBtnVisON.setVisibility(View.GONE);
                }
            }
        });

        ShowPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(mPassword.getText()))
                {
                    if(!SHOW_PASSWORD)
                    {
                        SHOW_PASSWORD=true;
                        mPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        ShowPasswordBtn.setVisibility(View.GONE);
                        ShowPasswordBtnVisON.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        ShowPasswordBtnVisON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(SHOW_PASSWORD && !TextUtils.isEmpty(mPassword.getText()))
                {
                    SHOW_PASSWORD = false;
                    mPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ShowPasswordBtn.setVisibility(View.VISIBLE);
                    ShowPasswordBtnVisON.setVisibility(View.GONE);
                }

            }
        });

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String display_name = mDisplayName.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String repassword = ReTypePassword.getText().toString().trim();
                if (!password.equals(repassword)) {
                    DisplaySnackBar("Passwords do not match");

                } else {
                    if (TextUtils.isEmpty(display_name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {


                        DisplaySnackBar("Check the details you entered");
                    } else {
                        if (password.length() <= 7) {
                            DisplaySnackBar("Retype Password it  must contain minimum 8 charachters");
                        } else {
                            progressDialog.setMessage("Registering user. Please wait...");
                            progressDialog.show();
                            register_user(display_name, email, password);
                        }


                    }
                }
            }
        });


        VerifiedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().getCurrentUser().reload();
                checkEmailVerification();
            }
        });

    }


    private void register_user(final String display_name, final String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {


                    FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = current_user.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    String device_token = FirebaseInstanceId.getInstance().getToken();
                    HashMap<String, String> usermap = new HashMap<>();
                    usermap.put("Name", display_name);
                    usermap.put("Email", email);
                    usermap.put("Profile_picture", "default");
                    usermap.put("thumb_image", "default");
                    mDatabase.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            if (task.isSuccessful()) {
                                progressDialog.setMessage("Sending E-mail verification. Please wait");
                                sendEmailVerification();

                            } else {

                                progressDialog.dismiss();
                                DisplaySnackBar(task.getResult().toString());
                            }


                        }
                    });


                } else {

                    progressDialog.dismiss();
                    DisplaySnackBar("Cannot register,Please try again.. ");
                }
            }
        });

    }

    private void sendEmailVerification() {

        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    DisplaySnackBar("Verification mail sent.");
                    VerifiedButton.setEnabled(true);
                }
            });
        }

    }

    private void checkEmailVerification() {
        DisplaySnackBar("Please wait....");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    if (user.isEmailVerified()) {
                        DisplaySnackBar("Done.");
                        startActivity(new Intent(RegisterUser.this, WorkingIntroActivity.class));
                        overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
                        finish();
                    } else {
                        DisplaySnackBar("Email verification send. Please verify and try again.");

                    }

                }else{
                    DisplaySnackBar("Verification failed. Please check your internet connection");
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            startActivity(new Intent(this,IntroActivity.class));
            overridePendingTransition(R.anim.activity_fade_in,R.anim.activity_fade_out);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            startActivity(new Intent(this,IntroActivity.class));
            overridePendingTransition(R.anim.activity_fade_in,R.anim.activity_fade_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void DisplaySnackBar(String message)
    {
        Snackbar.make(RootForRegisterActivity,message,Snackbar.LENGTH_SHORT).show();
    }

}

