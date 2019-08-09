package com.example.owner.mystarlive;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 로그인 화면
 * 회원의 아이디와 패스워드를 받아 서버로 전송한다.
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    UserAPI userAPI;
    Retrofit retrofit;
    Gson gson;
    Boolean loginChecked; //자동로그인 체크확인 변수
    SharedPreferences.Editor editor;

    @BindView(R.id.input_id) EditText _idText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_login) Button _loginButton;
    @BindView(R.id.link_signup) TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        // 자동로그인 세팅값 불러오기, 만약 값이 없으면 기본값 false를 가져옴
        SharedPreferences save = getSharedPreferences("Save", Activity.MODE_PRIVATE);



        gson = new GsonBuilder()
                .setLenient()
                .create();


        retrofit = new Retrofit.Builder()
                .baseUrl(UserAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        userAPI = retrofit.create(UserAPI.class);

        //로그인버튼 누를 때 이벤트발생
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("로그인버튼 클릭","로그인버튼 클릭");
                login();
            }
        });
        //회원가입 누를 때 이벤트발생
        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    /*로그인버튼 누르고 진행되는 함수
    id와 password를 받아서 서버로 전송해주는 메소드이다.
     */
    public void login() {


        if (!validate()) {
            Log.d(TAG, "Login()메소드문제");
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String id = _idText.getText().toString();
        final String password = _passwordText.getText().toString();

        Log.e("id : ", id);
        Log.e("password : ", password);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        userAPI.login(id, password)
                                .enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> response) {

                                        String ii = response.toString();

                                        Log.e("서버메세지 : ", ii);

                                        if (response.isSuccessful()) {
                                            User body = response.body();
                                            //서버에서 값을 잘받아왔는지 확인용
                                            if (body != null) {
                                                Log.d("data.getUserId()", body.getNo() + "");
                                                Log.d("data.getId()", body.getUserid());
                                                Log.d("data.getTitle()", body.getPassword());
                                                Log.d("data.getBody()", body.getLikestar());
                                                Log.d("data.getBody()", body.getUserage()+ "");
                                                Log.d("data.getBody()", body.getPicture());
                                                Log.d("data.getBody()", body.getType()+ "");
                                                Log.e("getData end", "======================================");


                                                //자동로그인 체크시 SharedPreference로 아이디,패스워드,체크박스값을 저장한다.
                                                SharedPreferences save = getSharedPreferences("Save", Activity.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = save.edit();

                                                int no = body.getNo();
                                                String userid = body.getUserid();
                                                String userpass = body.getPassword();
                                                String likestar = body.getLikestar();
                                                int age = body.getNo();
                                                String picture = body.getPicture();
                                                int type = body.getNo();

                                                loginChecked = true;

                                                editor.putInt("no",no);
                                                editor.putString("userid",userid);
                                                editor.putString("userpass",userpass);
                                                editor.putString("likestar",likestar);
                                                editor.putInt("age",age);
                                                editor.putString("picture",picture);
                                                editor.putInt("type",type);
                                                editor.putBoolean("Check", loginChecked);


                                                editor.commit();

                                            }
                                            finish();

                                        } else {
                                            //실패
                                            Toast.makeText(LoginActivity.this, "실패", Toast.LENGTH_SHORT).show();
                                            _loginButton.setEnabled(true);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        // 문제발생(네트워크나 변수설정.)
                                        Log.e("Err", t.getMessage());
                                        Toast.makeText(LoginActivity.this, "문제발생", Toast.LENGTH_SHORT).show();
                                        _loginButton.setEnabled(true);
                                    }
                                });


                        progressDialog.dismiss();
                        // onLoginFailed();

                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String mid = _idText.getText().toString();
        String mpassword = _passwordText.getText().toString();

        if (mid.isEmpty()) {
            _idText.setError("id를 작성해주세요");
            valid = false;
        } else {
            _idText.setError(null);
        }

        if (mpassword.isEmpty()) {
            _passwordText.setError("패스워드 작성해주세요.");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
