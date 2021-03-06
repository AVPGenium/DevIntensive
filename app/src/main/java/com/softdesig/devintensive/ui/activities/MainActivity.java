package com.softdesig.devintensive.ui.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.softdesig.devintensive.R;
import com.softdesig.devintensive.data.managers.DataManager;
import com.softdesig.devintensive.utils.ConstantManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private static final String TAG = ConstantManager.TAG_PREFIX+"Main Activity";

    private ImageView mCallImg;
    private CoordinatorLayout mCoordinatorLayout;
    private Toolbar mToolbar;
    private DrawerLayout mNavigationDrawer;
    private FloatingActionButton mFloatingActionButton;
    private EditText mUserPhone, mUserMail, mVk, mGit, mAbout;
    private RelativeLayout mProfilePlaceholder;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private AppBarLayout.LayoutParams mappBarParams = null;
    private AppBarLayout mAppBarLayout;
    private ImageView mProfileImage;
    private TextView mUserValueRating, mUserValueCodeLines, mUserValueProject;
    private List<TextView> mUserValuesViews;
    private ImageView mProfileAvatar;
    private NavigationView mNavigationView;

    private File mPhotoFile = null;
    private Uri mSelectedImage = null;
    private DataManager mDataManager;
    private int mCurrentEditMode=0;
    private List<EditText> mUserInfoViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        List<EditText> mEditTexts;

        mDataManager = DataManager.getInstance();
        mCallImg = (ImageView)findViewById(R.id.call_img);
        mCoordinatorLayout = (CoordinatorLayout)findViewById(R.id.main_coordinator_container);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mNavigationDrawer = (DrawerLayout)findViewById(R.id.navigation_drawer);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mAppBarLayout = (AppBarLayout)findViewById(R.id.appbar_layout);
        mUserPhone = (EditText) findViewById(R.id.phone_et);
        mUserMail = (EditText) findViewById(R.id.email_et);
        mVk = (EditText) findViewById(R.id.vk_et);
        mGit = (EditText) findViewById(R.id.github_et);
        mAbout = (EditText) findViewById(R.id.about_et);
        mProfilePlaceholder = (RelativeLayout)findViewById(R.id.profile_placeholder);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        mProfileImage = (ImageView) findViewById(R.id.user_photo_img);
        mUserValueRating = (TextView) findViewById(R.id.user_info_rait_txt);
        mUserValueCodeLines = (TextView) findViewById(R.id.user_info_code_lines_txt);
        mUserValueProject = (TextView) findViewById(R.id.user_info_project_txt);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mProfileAvatar = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.user_avatar);

        mUserInfoViews = new ArrayList<>();
        mUserInfoViews.add(mUserPhone);
        mUserInfoViews.add(mUserMail);
        mUserInfoViews.add(mVk);
        mUserInfoViews.add(mGit);
        mUserInfoViews.add(mAbout);

        mUserValuesViews = new ArrayList<>();
        mUserValuesViews.add(mUserValueRating);
        mUserValuesViews.add(mUserValueCodeLines);
        mUserValuesViews.add(mUserValueProject);

        mCallImg.setOnClickListener(this);
        mFloatingActionButton.setOnClickListener(this);
        mProfilePlaceholder.setOnClickListener(this);

        setupToolbar();
        setupDrawer();
        initUserFields();
        initUserInfoValue();

        Picasso.with(this)
                .load(mDataManager.getPreferenceManager().loadUserPhoto())
                .placeholder(R.drawable.user_photo) // TODO: 05.07.2016 сделать плейсхолдер и transform + crop
                .into(mProfileImage);

        List<String> test = mDataManager.getPreferenceManager().loadUserProfileData();

        if(savedInstanceState == null){
            showToast("активити запускается впервые");
        } else{
            mCurrentEditMode = savedInstanceState.getInt(ConstantManager.EDIT_MODE_KEY, 0);
            changeEditMode(mCurrentEditMode);
            showToast("активити уже запускалось");
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG, "onPause");
        saveUserFields();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home){
            mNavigationDrawer.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                if(mCurrentEditMode == 0){
                    changeEditMode(1);
                    mCurrentEditMode = 1;
                } else{
                    changeEditMode(0);
                    mCurrentEditMode = 0;
                }
                break;
            case R.id.profile_placeholder:
                //TODO: 2.07.2016 Сделать выбор, откуда загружать фото
                showDialog(ConstantManager.LOAD_PROFILE_PHOTO);
                break;
            case R.id.call_img:
                String number = mUserPhone.getText().toString();
                if (!number.equals("") || !number.equals("null")) {
                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.fromParts("tel", number, null));

                    //проверка разрешения на звонок
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{
                                android.Manifest.permission.CALL_PHONE
                        }, ConstantManager.PERMISSION_CALL_REQUEST_CODE);

                        Snackbar.make(mCoordinatorLayout, R.string.snackbar_message_need_take_permissions, Snackbar.LENGTH_LONG)
                                .setAction("Разрешить", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        openApplicationSettings();
                                    }
                                }).show();
                    } else {
                        try {
                            startActivity(callIntent);
                        } catch (ActivityNotFoundException e) {
                            showSnackBar(getString(R.string.main_activity_error_not_found_call_app));
                        }
                    }
                }
                break;

            case R.id.mail_img:
                String email = mUserMail.getText().toString();
                if (!email.equals("") || !email.equals("null")) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
                    try {
                        startActivity(Intent.createChooser(emailIntent, "Отправка письма..."));
                    } catch (ActivityNotFoundException e) {
                        //showSnackBar(getString(R.string.main_activity_error_not_found_email_app));
                    }
                }

                break;

            case R.id.vk_img:
                String vkUrl = mVk.getText().toString();
                if (!vkUrl.equals("") || !vkUrl.equals("null")) {
                    Intent vkIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/" + vkUrl));

                    try {
                        startActivity(vkIntent);
                    } catch (ActivityNotFoundException e) {
                        showSnackBar(getString(R.string.main_activity_error_not_found_vk_app));
                    }
                }
                break;

            case R.id.git_img:
                String gitUrl = mGit.getText().toString();
                if (!gitUrl.equals("") || !gitUrl.equals("null")) {
                    Intent gitIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/" + gitUrl));

                    try {
                        startActivity(gitIntent);
                    } catch (ActivityNotFoundException e) {
                        showSnackBar(getString(R.string.main_activity_error_not_found_git_app));
                    }
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putInt(ConstantManager.EDIT_MODE_KEY, mCurrentEditMode);
    }

    private void runWithDelay(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        }, 5000);
    }

    @Override
    public void onBackPressed() {
        if (mNavigationDrawer.isDrawerOpen(GravityCompat.START)) {
            mNavigationDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Показывает Snackbar
     * @param message
     */
    private void showSnackBar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Настраивает Toolbar
     */
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        mappBarParams = (AppBarLayout.LayoutParams)mCollapsingToolbarLayout.getLayoutParams();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Настраивает DrawerLayout
     */
    private void setupDrawer(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                showSnackBar(item.getTitle().toString());
                item.setChecked(true);
                mNavigationDrawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(3)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(navigationView.getContext())
                .load(mDataManager.getPreferenceManager().loadUserAvatar())
                .fit()
                .transform(transformation)
//               .placeholder(R.drawable.user_photo) // TODO: 05.07.2016 сделать плейсхолдер и transform + crop
                .into(mProfileAvatar);
    }

    private void changeEditMode(int mode){
        if(mode == 1){
            mFloatingActionButton.setImageResource(R.drawable.ic_done_black_24dp);
            for(EditText userValue : mUserInfoViews){
                userValue.setEnabled(true);
                userValue.setFocusable(true);
                userValue.setFocusableInTouchMode(true);
                showProfilePlaceholder();
                lockToolbar();
                mCollapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
            }
        } else if(mode == 0){
            mFloatingActionButton.setImageResource(R.drawable.ic_create_black_24dp);
            for(EditText userValue : mUserInfoViews){
                userValue.setEnabled(false);
                userValue.setFocusable(false);
                userValue.setFocusableInTouchMode(false);
                hideProfilePlaceholder();
                unlockToolbar();
                mCollapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.white));
                saveUserFields();
            }
        }
    }

    private void initUserFields(){
        List<String> userData = mDataManager.getPreferenceManager().loadUserProfileData();
        for(int i = 0; i < userData.size(); i++){
            mUserInfoViews.get(i).setText(userData.get(i));
        }
    }

    private void saveUserFields(){
        List<String> userData = new ArrayList<>();
        for (EditText userFieldView : mUserInfoViews) {
            userData.add(userFieldView.getText().toString());
        }
        mDataManager.getPreferenceManager().saveUserProfileData(userData);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ConstantManager.REQUEST_GALLERY_PICTURE:
                if(resultCode == RESULT_OK && data != null){
                    mSelectedImage = data.getData();
                    insertProfileImage(mSelectedImage);
                }
                break;
            case ConstantManager.REQUEST_CAMERA_PICTURE:
                if(resultCode == RESULT_OK && mPhotoFile != null){
                    mSelectedImage = Uri.fromFile(mPhotoFile);
                    insertProfileImage(mSelectedImage);
                }
                break;
        }
    }

    /**
     * Обновляет изображение профиля
     * @param selectedImage ссылка на изображение
     */
    private void insertProfileImage(Uri selectedImage) {
        Picasso.with(this)
                .load(selectedImage)
                .into(mProfileImage);
        mDataManager.getPreferenceManager().saveUserPhoto(selectedImage);
    }

    private void initUserInfoValue(){
        List<String> userData = mDataManager.getPreferenceManager().loadUserProfileValues();
        for (int i = 0; i < userData.size(); i++){
            mUserValuesViews.get(i).setText(userData.get(i));
        }
    }

    private void loadPhotoFromGallery(){
        Intent takeGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        takeGalleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(takeGalleryIntent, getString(R.string.user_profile_chose_message)), ConstantManager.REQUEST_GALLERY_PICTURE);
    }

    private void loadPhotoFromCamera(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent takeCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                mPhotoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
                // TODO: 02.07.2016  обработать ошибку
            }
            if (mPhotoFile != null) {
                // TODO: 02.07.2016 передать фото в интент
                takeCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
                startActivityForResult(takeCaptureIntent, ConstantManager.REQUEST_CAMERA_PICTURE);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, ConstantManager.CAMERA_REQUEST_PERMISSION_CODE);
            Snackbar.make(mCoordinatorLayout, "Для корректной работы необходимо дать требуемые разрешения!", Snackbar.LENGTH_LONG)
                    .setAction("Разрешить", new View.OnClickListener(){
                       @Override
                        public void onClick(View v){
                           openApplicationSettings();
                       }
                    }).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == ConstantManager.CAMERA_REQUEST_PERMISSION_CODE && grantResults.length == 2){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // TODO: 04.07.2016 тут обрабатывается разрешение (разрешение получено)
                // например, вывести сообщение или обработать какой-то логикой если нужно
            }
            if(grantResults[1] == PackageManager.PERMISSION_GRANTED){
                // TODO: 04.07.2016 тут обрабатывается разрешение (разрешение получено)
                // например, вывести сообщение или обработать какой-то логикой если нужно
            }
        }
    }

    private void showProfilePlaceholder(){
        mProfilePlaceholder.setVisibility(View.VISIBLE);
    }

    private void hideProfilePlaceholder(){
        mProfilePlaceholder.setVisibility(View.GONE);
    }

    private void lockToolbar(){
        mAppBarLayout.setExpanded(true, true);
        mappBarParams.setScrollFlags(0);
        mCollapsingToolbarLayout.setLayoutParams(mappBarParams);
    }

    private void unlockToolbar(){
        mappBarParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL|AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        mCollapsingToolbarLayout.setLayoutParams(mappBarParams);
    }

    /**
     * Возвращает диалог смены фотографии
     * @param id
     * @return
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id){
            case ConstantManager.LOAD_PROFILE_PHOTO:
                String[] selectItems = {
                        getString(R.string.user_profile_dialog_gallery),
                        getString(R.string.user_profile_dialog_camera),
                        getString(R.string.user_profile_dialog_cancel)};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.user_profile_dialog_title);
                builder.setItems(selectItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int choiceItem) {
                        switch (choiceItem){
                            case 0:
                                // TODO: 2.07.2016 загрузить из галлереи
                                loadPhotoFromGallery();
                                break;
                            case 1:
                                loadPhotoFromCamera();
                                // TODO: 2.07.2016 загрузить из камеры
                                break;
                            case 2:
                                // TODO: 2.07.2016 отменить
                                dialog.cancel();
                                break;
                        }
                    }
                });
                return builder.create();
            default:
                return null;
        }
    }

    private File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageSir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageSir);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, image.getAbsolutePath());
        this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        return image;
    }

    public void openApplicationSettings(){
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, ConstantManager.PERMISSION_REQUEST_SETTINGS_CODE);
    }
}
