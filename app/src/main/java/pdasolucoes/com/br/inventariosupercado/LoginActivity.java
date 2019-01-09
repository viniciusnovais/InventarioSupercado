package pdasolucoes.com.br.inventariosupercado;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import pdasolucoes.com.br.inventariosupercado.Inventario.AutorizaoActivity;
import pdasolucoes.com.br.inventariosupercado.Util.Async.AsyncTaskLogin;
import pdasolucoes.com.br.inventariosupercado.Util.Metodo;


public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsuario, editextSenha;
    SharedPreferences preferences;
    private static final int REQUEST_CODE = 0x11;
    //String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity_login);

        preferences = getSharedPreferences(getString(R.string.preference_login_file), MODE_PRIVATE);

        editTextUsuario = findViewById(R.id.etUser);
        editextSenha = findViewById(R.id.etPassword);
        Button btnEntrar = findViewById(R.id.btnEntrar);

//        if (!TextUtils.isEmpty(preferences.getString(getString(R.string.preference_login), ""))) {
//
//            Bundle bundle = new Bundle();
//            bundle.putString(getString(R.string.preference_login), preferences.getString(getString(R.string.preference_login), ""));
//            bundle.putString(getString(R.string.preference_senha), preferences.getString(getString(R.string.preference_senha), ""));
//
//            AsyncTaskLogin taskLogin = new AsyncTaskLogin(LoginActivity.this, bundle);
//            taskLogin.startTask();
//            taskLogin.setResultLogin(result -> {
//                if (result) {
//                    startActivity(new Intent(LoginActivity.this, AutorizaoActivity.class));
//                    finish();
//                }
//            });
//        }

        btnEntrar.setOnClickListener(v -> {

            if (Metodo.isNetworkConnected(LoginActivity.this)) {
                if (Metodo.verificaCamposNulos(new EditText[]{editTextUsuario, editextSenha})) {
                    if (editTextUsuario.getText().toString().equals(getString(R.string.usuario_senha_padrao))
                            && editextSenha.getText().toString().equals(getString(R.string.usuario_senha_padrao))) {

                        Metodo.limparCampos(new EditText[]{editTextUsuario, editextSenha});
                        Intent i = new Intent(LoginActivity.this, ConfiguracaoActivity.class);
                        startActivity(i);
                        finish();

                    } else {
                        Bundle bundle = new Bundle();

                        bundle.putString(getString(R.string.preference_login), editTextUsuario.getText().toString());
                        bundle.putString(getString(R.string.preference_senha), editextSenha.getText().toString());

                        AsyncTaskLogin taskLogin = new AsyncTaskLogin(LoginActivity.this, bundle);
                        taskLogin.startTask();
                        taskLogin.setResultLogin(result -> {
                            if (result) {
                                startActivity(new Intent(LoginActivity.this, AutorizaoActivity.class));
                                finish();
                            }
                        });
                    }
                } else {
                    Metodo.toastCamposObrigatorios(LoginActivity.this);
                }
            } else {
                Metodo.popupMensgam(LoginActivity.this, getString(R.string.falha_conexao));
            }
        });

        //ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//
//        if (requestCode == REQUEST_CODE) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // save file
//            } else {
//                Toast.makeText(getApplicationContext(), "PERMISSION_DENIED", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}
