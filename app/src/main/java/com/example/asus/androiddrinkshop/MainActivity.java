package com.example.asus.androiddrinkshop;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.asus.androiddrinkshop.Model.CheckUserResponse;
import com.example.asus.androiddrinkshop.Model.User;
import com.example.asus.androiddrinkshop.Retrofit.IDrinkShopAPI;
import com.example.asus.androiddrinkshop.Utils.Common;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.szagurskii.patternedtextwatcher.PatternedTextWatcher;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private  static final int REQUEST_CODE = 1000;
    Button btn_continue;
    IDrinkShopAPI mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //test admin xxxxxx
      
        mService = Common.getApi();

        btn_continue = (Button)findViewById(R.id.btn_continue);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoginPage(LoginType.PHONE);
            }
        });

    }

    private void startLoginPage(LoginType loginType) {
        Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder builder = new AccountKitConfiguration.AccountKitConfigurationBuilder(loginType,AccountKitActivity.ResponseType.TOKEN);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,builder.build());
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE){
            AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);

            if(result.getError() != null){
                Toast.makeText(this,""+result.getError().getErrorType().getMessage(),Toast.LENGTH_LONG).show();
            }else if(result.wasCancelled()){
                Toast.makeText(this,"Cancel",Toast.LENGTH_LONG).show();
            }else{
                if(result.getAccessToken() != null){
                    final AlertDialog alertDialog = new SpotsDialog(MainActivity.this);
                    alertDialog.show();
                    alertDialog.setMessage("Mohon Tunggu...");

//                    cek user di server
                    AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                        @Override
                        public void onSuccess(final Account account) {
                            mService.checkUserResponseCall(account.getPhoneNumber().toString())
                                    .enqueue(new Callback<CheckUserResponse>() {
                                        @Override
                                        public void onResponse(Call<CheckUserResponse> call, Response<CheckUserResponse> response) {
                                            CheckUserResponse userResponse = response.body();
                                            if(userResponse.isExits()){
                                                alertDialog.dismiss();
                                            }else{
                                                alertDialog.dismiss();

                                                showRegisterDialog(account.getPhoneNumber().toString());

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<CheckUserResponse> call, Throwable t) {

                                        }
                                    });
                        }

                        @Override
                        public void onError(AccountKitError accountKitError) {
                            Log.d("ERROR",accountKitError.getErrorType().getMessage());
                        }
                    });

                }
            }
        }

    }

    private void showRegisterDialog(final String phone) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("REGISTER");

        LayoutInflater inflater = this.getLayoutInflater();
        View registerLayout = inflater.inflate(R.layout.register_layout,null);

        final MaterialEditText edit_name = (MaterialEditText)registerLayout.findViewById(R.id.edit_name);
        final MaterialEditText edit_address = (MaterialEditText)registerLayout.findViewById(R.id.edit_address);
        final MaterialEditText edit_birthDate = (MaterialEditText)registerLayout.findViewById(R.id.edit_birthDate);

        Button btn_register = (Button)registerLayout.findViewById(R.id.btn_register);

        edit_birthDate.addTextChangedListener(new PatternedTextWatcher("####-##-##"));

        builder.setView(registerLayout);
        final AlertDialog dialog = builder.create();

//        event
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

                if(TextUtils.isEmpty(edit_name.getText().toString())){
                    Toast.makeText(MainActivity.this,"Nama tidak boleh kosong",Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(edit_birthDate.getText().toString())){
                    Toast.makeText(MainActivity.this,"Tanggal Lahir tidak boleh kosong",Toast.LENGTH_LONG).show();
                    return;
                }

                if(TextUtils.isEmpty(edit_address.getText().toString())){
                    Toast.makeText(MainActivity.this,"Alamat tidak boleh kosong",Toast.LENGTH_LONG).show();
                    return;
                }

                final AlertDialog watingDialog = new SpotsDialog(MainActivity.this);
                watingDialog.show();
                watingDialog.setMessage("Mohon Tunggu...");

                mService.registerNewUser(phone,
                        edit_name.getText().toString(),
                        edit_birthDate.getText().toString(),
                        edit_address.getText().toString())
                        .enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                watingDialog.dismiss();
                                User user = response.body();
                                if(TextUtils.isEmpty(user.getError_msg())){
                                    Toast.makeText(MainActivity.this,"Register User Successfuly",Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(MainActivity.this,user.getError_msg().toString(),Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                watingDialog.dismiss();
                            }
                        });

            }
        });

        dialog.show();
    }

    private void printKeyHash() {
        try{
            PackageInfo info = getPackageManager().getPackageInfo("com.example.asus.androiddrinkshop", PackageManager.GET_SIGNATURES);

            for(Signature signature:info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KEYHASH", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
