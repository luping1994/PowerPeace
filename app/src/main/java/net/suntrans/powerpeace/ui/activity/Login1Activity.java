package net.suntrans.powerpeace.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;

import android.os.Build;
import android.os.Bundle;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.suntrans.looney.utils.LogUtil;
import net.suntrans.looney.utils.UiUtils;
import net.suntrans.looney.widgets.LoadingDialog;
import net.suntrans.powerpeace.App;
import net.suntrans.powerpeace.Constants;
import net.suntrans.powerpeace.MainActivity;
import net.suntrans.powerpeace.R;
import net.suntrans.powerpeace.StudentMainActivity;
import net.suntrans.powerpeace.api.RetrofitHelper;
import net.suntrans.powerpeace.bean.LoginEntity;
import net.suntrans.powerpeace.bean.UserInfoEntity;
import net.suntrans.powerpeace.bean.ZongheEntity;
import net.suntrans.powerpeace.rx.BaseSubscriber;

import rx.Subscriber;
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
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("登录");
//        toolbar.setNavigationIcon(R.drawable.ic_clear);
//
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();


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
                startActivity(new Intent(Login1Activity.this,FindPasswordActivity.class));

            }
        });

        findViewById(R.id.jihuo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login1Activity.this,JihuoActivity.class));
            }
        });


    }

    private void applyTransition() {
        if (getIntent().getBooleanExtra("notTrans",false)){

        }else {
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

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

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
                dialog.setWaitText("正在验证您的账号和密码...");
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

    Handler handler = new Handler();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    private void LoginFromServer(String username, final String password) {
        RetrofitHelper.getApi().login(username, password)
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

                        if (result.code == 1) {
                            App.getSharedPreferences().edit().putString("token", result.token)
                                    .putInt("role", result.info.role_id)
                                    .putString("username", result.info.username)
                                    .putString("password", password)
                                    .putString("user_id",result.info.id)
                                    .commit();
//                            System.out.println(result.info.role_id);
                            if (result.info.role_id == Constants.ADMIN) {
                                if (dialog != null)
                                    dialog.dismiss();
                                startActivity(new Intent(Login1Activity.this, MainActivity.class));
                                finish();
                            } else {
                                getUserInfo(result.info.username,result.info.role_id+"");
                            }
                        } else {
                            UiUtils.showToast("账号或密码错误!");
                        }
                    }
                });
    }


    private void getUserInfo(String userName,String role) {
        LogUtil.i("username="+userName+",role="+role);
        RetrofitHelper.getApi()
                .getUserInfo(userName,role)
                .compose(this.<UserInfoEntity>bindToLifecycle())
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
                    }

                    @Override
                    public void onNext(UserInfoEntity info) {
//                        System.out.println(info.toString());
                        if (dialog != null)
                            dialog.dismiss();
                        if (info.code == 1) {
                            UiUtils.showToast("登录成功!");
                            App.getSharedPreferences().edit().putString("room_id", info.info.get(0).room_id + "")
                                    .putString("studentID",info.info.get(0).studentID)
                                    .commit();
                            startActivity(new Intent(Login1Activity.this, StudentMainActivity.class));
                            finish();
                        } else {
                            UiUtils.showToast("登录失败,请检查您的用户名");
                        }
                    }
                });
    }

}

