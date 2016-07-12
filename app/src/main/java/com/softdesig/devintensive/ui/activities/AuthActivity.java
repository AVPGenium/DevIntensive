package com.softdesig.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.softdesig.devintensive.R;
import com.softdesig.devintensive.data.managers.DataManager;
import com.softdesig.devintensive.data.network.request.UserLoginReq;
import com.softdesig.devintensive.data.network.responce.UserModelRes;
import com.softdesig.devintensive.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends BaseActivity implements View.OnClickListener{
    private Button mSignIn;
    private TextView mRememberPassword;
    private EditText mLogin, mPassword;
    private CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_coordinator_container);
        mSignIn = (Button)findViewById(R.id.auth_button);
        mLogin = (EditText) findViewById(R.id.et_login_email);
        mPassword = (EditText) findViewById(R.id.et_login_password);
        mRememberPassword = (TextView) findViewById(R.id.forgot_pass);
        mRememberPassword.setOnClickListener(this);
        mSignIn.setOnClickListener(this);
        mDataManager = DataManager.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.auth_button:
                signIn();
                break;
            case R.id.forgot_pass:
                rememberPassword();
                break;
        }
    }

    private void showSnackbar(String message){
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void rememberPassword(){
        Intent rememberIntent= new Intent(Intent.ACTION_VIEW, Uri.parse("http://devintensive.softdesign-apps.ru/forgotpass"));
        startActivity(rememberIntent);
    }

    private void loginSuccess(UserModelRes userModelRes){
        mDataManager.getPreferenceManager().saveAuthTocken(userModelRes.getData().getToken());
        mDataManager.getPreferenceManager().saveUserId(userModelRes.getData().getUser().getId());
        saveUserValues(userModelRes);
        saveUserFields(userModelRes);
        saveUserPhoto(userModelRes);
        saveUserAvatar(userModelRes);
        Intent loginIntent = new Intent(this, MainActivity.class);
        startActivity(loginIntent);
    }

    private void signIn(){
        if(NetworkStatusChecker.isNetworkAvailable(this)) {
            Call<UserModelRes> call = mDataManager.loginUser(
                    new UserLoginReq(mLogin.getText().toString(), mPassword.getText().toString())
            );
            call.enqueue(new Callback<UserModelRes>() {
                @Override
                public void onResponse(Call<UserModelRes> call, Response<UserModelRes> response) {
                    if (response.code() == 200) {
                        loginSuccess(response.body());
                    } else if (response.code() == 404) {
                        showSnackbar("Пользователь не найден или логин и пароль не совпадают!");
                    } else {
                        showSnackbar("Ошибка. Все плохо :( ");
                    }
                }

                @Override
                public void onFailure(Call<UserModelRes> call, Throwable t) {
                    // TODO: 11.07.2016 Обработать ошибки
                }
            });
        } else {
            showSnackbar("Сеть не доступна на данный момент, попробуйте позже!");
        }
    }

    private void saveUserValues(UserModelRes userModelRes){
        int[] userValues = {
            userModelRes.getData().getUser().getProfileValues().getRating(),
            userModelRes.getData().getUser().getProfileValues().getLinesCode(),
            userModelRes.getData().getUser().getProfileValues().getProjects(),
        };
        mDataManager.getPreferenceManager().saveUserProfileValues(userValues);
    }

    private void saveUserFields(UserModelRes userModelRes){
        List<String> userFields = new ArrayList<>();
        userFields.add(userModelRes.getData().getUser().getContacts().getPhone());
        userFields.add(userModelRes.getData().getUser().getContacts().getEmail());
        userFields.add(userModelRes.getData().getUser().getContacts().getVk());
        try{
            userFields.add(userModelRes.getData().getUser().getRepositories().getRepo().get(0).getGit());
        }catch (Exception e){
            userFields.add("");
        }
        userFields.add(userModelRes.getData().getUser().getPublicInfo().getBio());
        mDataManager.getPreferenceManager().saveUserProfileData(userFields);
    }

    private void saveUserPhoto(UserModelRes userModelRes){
        mDataManager.getPreferenceManager().saveUserPhoto(Uri.parse(userModelRes.getData().getUser().getPublicInfo().getPhoto()));
    }

    private void saveUserAvatar(UserModelRes userModelRes){
        mDataManager.getPreferenceManager().saveUserAvatar(Uri.parse(userModelRes.getData().getUser().getPublicInfo().getAvatar()));
    }
}
