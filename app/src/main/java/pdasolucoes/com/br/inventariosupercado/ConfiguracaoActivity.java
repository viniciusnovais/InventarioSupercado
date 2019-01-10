package pdasolucoes.com.br.inventariosupercado;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import pdasolucoes.com.br.inventariosupercado.Util.Metodo;


/**
 * Created by PDA on 25/09/2017.
 */

public class ConfiguracaoActivity extends AppCompatActivity {

    private EditText editServidor, editDiretorio, editFilial;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity_configuracao);

        editServidor = findViewById(R.id.etServer);
        editDiretorio = findViewById(R.id.etDiretorio);
        editFilial = findViewById(R.id.etFilial);
        Button btCancelar = findViewById(R.id.btnCancelar);
        Button btConfirmar = findViewById(R.id.btnOkConfig);

        preferences = getSharedPreferences(getString(R.string.pref_configuracoes), MODE_PRIVATE);
        editServidor.setText(preferences.getString(getString(R.string.pref_servidor), getString(R.string.ip_servidor)));
        editDiretorio.setText(preferences.getString(getString(R.string.pref_diretorio), getString(R.string.diretorio_name)));
        editFilial.setText(preferences.getString(getString(R.string.pref_filial), "-1"));


        btCancelar.setOnClickListener(v -> finish());

        btConfirmar.setOnClickListener(v -> {
            if (Metodo.verificaCamposNulos(new EditText[]{editDiretorio, editServidor, editFilial})) {

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(getString(R.string.pref_servidor), editServidor.getText().toString());
                editor.putString(getString(R.string.pref_diretorio), editDiretorio.getText().toString());
                editor.putString(getString(R.string.pref_filial), editFilial.getText().toString());

                editor.apply();
                editor.commit();
                startActivity(new Intent(ConfiguracaoActivity.this, SplashActivity.class));
                finish();

            } else {
                Metodo.toastCamposObrigatorios(ConfiguracaoActivity.this);
            }
        });


    }
}
