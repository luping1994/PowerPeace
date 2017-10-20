package net.suntrans.powerpeace.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pgyersdk.update.PgyUpdateManager;
import com.trello.rxlifecycle.android.ActivityEvent;

import net.suntrans.looney.utils.UiUtils;
import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.powerpeace.App;
import net.suntrans.powerpeace.BuildingManagerMainActivity;
import net.suntrans.powerpeace.Constants;
import net.suntrans.powerpeace.FloorMainActivity;
import net.suntrans.powerpeace.LeaderMainActivity;
import net.suntrans.powerpeace.MainActivity;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.StudentMainActivity;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.LoginEntity;
import net.suntrans.powerpeace.bean.UserInfoEntity;
import net.suntrans.powerpeace.rx.BaseSubscriber;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class Login1Activity extends BasedActivity {
    public static final String TRANSITION_SLIDE_BOTTOM = "SLIDE_BOTTOM";
    public static final String EXTRA_TRANSITION = "EXTRA_TRANSITION";
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;


    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private LoadingDialog dialog;
    private Toolbar toolbar;
    private Button mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        applyTransition();
        setContentView(R.layout.activity_login1);

        PgyUpdateManager.register(this, "net.suntrans.powerpeace.fileProvider");

        String username = App.getSharedPreferences().getString("username", "");
        String password = App.getSharedPreferences().getString("password", "");

        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mLoginButton = (Button) findViewById(R.id.email_sign_in_button);
        mLoginButton.setEnabled(false);

        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(mEmailView.getText())) {
                    if (charSequence.length() > 0) {
                        mLoginButton.setEnabled(true);
                    } else {
                        mLoginButton.setEnabled(false);
                    }
                } else {
                    mLoginButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        mPasswordView.addTextChangedListener(textWatcher);

        mEmailView.setText(username);
        mPasswordView.setText(password);

        findViewById(R.id.wangjimima).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login1Activity.this, FindPasswordActivity.class));

            }
        });

        findViewById(R.id.jihuo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login1Activity.this, JihuoActivity.class));
            }
        });
    }

    private void applyTransition() {
        if (getIntent().getBooleanExtra("notTrans", false)) {

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String transition = getIntent().getStringExtra(EXTRA_TRANSITION);
                switch (transition) {
                    case TRANSITION_SLIDE_BOTTOM:
                        Transition transitionSlideBottom =
                                TransitionInflater.from(this).inflateTransition(R.transition.slide_bottom);
                        getWindow().setEnterTransition(transitionSlideBottom);
                        break;
                }
            }
        }

    }

//    private boolean mayRequestContacts() {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            return true;
//        }
//        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
//            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                    .setAction(android.R.string.ok, new View.OnClickListener() {
//                        @Override
//                        @TargetApi(Build.VERSION_CODES.M)
//                        public void onClick(View v) {
//                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//                        }
//                    });
//        } else {
//            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
//        }
//        return false;
//    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            if (dialog == null) {

                dialog = new LoadingDialog(this);
                dialog.setWaitText(getString(R.string.validating_password));
                dialog.setCancelable(false);
            }
            dialog.show();

            LoginFromServer(email, password);
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    dialog.dismiss();
//                    startActivity(new Intent(Login1Activity.this, MainActivity.class));
//                    finish();
//                }
//            },2000);
        }
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.length() >= 4;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
//        try {
//            PgyUpdateManager.unregister();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void LoginFromServer(final String username, final String password) {
        RetrofitHelper.getLoginApi().login(username, password, "password", "100001", "peS4zinqLC2x5pSc2Li98whTbSaC0d1OwrYsqQpL")
                .compose(this.<LoginEntity>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<LoginEntity>(this) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        super.onError(e);
                        if (dialog != null)
                            dialog.dismiss();
                    }

                    @Override
                    public void onNext(LoginEntity result) {
                        if (result.getAccess_token() != null) {
                            App.getSharedPreferences().edit().putString("token", result.getAccess_token())
                                    .putString("username",username)
                                    .putString("password",password)
                                    .commit();
                            getUserInfo();
                        } else {
                            if (dialog != null)
                                dialog.dismiss();
                            UiUtils.showToast(getString(R.string.username_password_is_error));
                        }
                    }
                });
    }


    private void getUserInfo() {
        if (dialog!=null)
        dialog.setWaitText(getString(R.string.validate_userinfo));
        RetrofitHelper.getApi()
                .getUserInfo()
                .compose(this.<UserInfoEntity>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BaseSubscriber<UserInfoEntity>(this) {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        e.printStackTrace();
                        if (dialog != null)
                            dialog.dismiss();
                        UiUtils.showToast(getString(R.string.getuserinfo_occor_error));

                    }

                    @Override
                    public void onNext(UserInfoEntity info) {
//                        System.out.println(info.toString());
                        if (dialog != null)
                            dialog.dismiss();
                        if (info.code == 1) {

                            App.getSharedPreferences().edit().putString("room_id", info.info.room_id + "")
                                    .putString("studentID", info.info.id)
                                    .putString("username",info.info.username)
                                    .putString("floor_id",info.info.floor_id)
                                    .putInt("role", info.info.role_id)
                                    .commit();
                            switch (info.info.role_id) {

                                case Constants.ROLE_APARTMENT://公寓管理人员
                                    handler.sendEmptyMessage(START_BUILDING_MANAGER_MAIN);
                                    break;
                                case Constants.ROLE_MANAGER://运维人员
//                                    UiUtils.showToast("暂不支持" + info.info.role_name + "登录本系统");
//                                    break;
                                case Constants.ROLE_ADMIN://管理员
                                    handler.sendEmptyMessage(START_MAIN_ADMIN);
                                    break;
                                case Constants.ROLE_OFFICER://办公人员
                                case Constants.ROLE_SCHOOL_LEADER://校领导
                                    handler.sendEmptyMessage(START_MAIN_LEADER);
                                    break;
                                case Constants.ROLE_FLOOR://楼栋管理员
                                    handler.sendEmptyMessage(START_MAIN_FLOOR_MANAGER);
                                    break;
                                case Constants.ROLE_STUDENT://学生
                                    handler.sendEmptyMessage(START_MAIN_STU);

                                    break;
                            }

                        } else {
                            UiUtils.showToast(getString(R.string.getuserinfo_occor_error));

                        }
                    }
                });
    }


    private static final int START_MAIN_ADMIN = 1;
    private static final int START_MAIN_STU = 2;
    private static final int START_MAIN_LEADER = 3;
    private static final int START_MAIN_FLOOR_MANAGER = 4;
    private static final int START_BUILDING_MANAGER_MAIN = 5;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case START_MAIN_ADMIN:
                    startActivity(new Intent(Login1Activity.this, MainActivity.class));
                    overridePendingTransition(R.anim.main_open_enter, R.anim.main_open_exit);

                    finish();
                    break;
                case START_MAIN_STU:
                    startActivity(new Intent(Login1Activity.this, StudentMainActivity.class));
                    overridePendingTransition(R.anim.main_open_enter, R.anim.main_open_exit);

                    finish();
                    break;
                case START_MAIN_LEADER:
                    startActivity(new Intent(Login1Activity.this, LeaderMainActivity.class));
                    overridePendingTransition(R.anim.main_open_enter, R.anim.main_open_exit);

                    finish();
                    break;
                case START_MAIN_FLOOR_MANAGER:
                    startActivity(new Intent(Login1Activity.this, FloorMainActivity.class));
                    overridePendingTransition(R.anim.main_open_enter, R.anim.main_open_exit);

                    finish();
                    break;
                case START_BUILDING_MANAGER_MAIN:
                    startActivity(new Intent(Login1Activity.this, BuildingManagerMainActivity.class));
                    overridePendingTransition(R.anim.main_open_enter, R.anim.main_open_exit);

                    finish();
                    break;
            }
        }
    };
}

