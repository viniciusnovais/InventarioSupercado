package pdasolucoes.com.br.inventariosupercado.Inventario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pdasolucoes.com.br.inventariosupercado.Dao.DataBase;
import pdasolucoes.com.br.inventariosupercado.Inventario.Loader.CallListString;
import pdasolucoes.com.br.inventariosupercado.PrincipalActivity;
import pdasolucoes.com.br.inventariosupercado.R;

public class SetorActivity extends PrincipalActivity {

    Spinner spinnerSecao;
    Spinner spinnerSubSecao;
    Spinner spinnerGrupo;
    Spinner spinnerSubGrupo;
    DataBase dataBase;
    CallListString callListString;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventario_setor_activity);

        dataBase = DataBase.getInstancia(this);

        SharedPreferences preferencesFiltros = getSharedPreferences(getString(R.string.filtros), MODE_PRIVATE);

        spinnerSecao = findViewById(R.id.spinnerSecao);
        spinnerSubSecao = findViewById(R.id.spinnerSubSecao);
        spinnerGrupo = findViewById(R.id.spinnerGrupo);
        spinnerSubGrupo = findViewById(R.id.spinnerSubGrupo);

        Button btVoltar = findViewById(R.id.btVoltar);
        Button btOk = findViewById(R.id.btOk);

        btVoltar.setOnClickListener(v -> finish());

        btOk.setOnClickListener(v -> {
            startActivity(new Intent(SetorActivity.this, ContagemAcitivity.class));

            SharedPreferences.Editor editor = preferencesFiltros.edit();
            editor.putString(getString(R.string.secao), spinnerSecao.getSelectedItem().toString());
            editor.putString(getString(R.string.subsecao), spinnerSubSecao.getSelectedItem().toString());
            editor.putString(getString(R.string.grupo), spinnerGrupo.getSelectedItem().toString().equals(getString(R.string.selecione))
                    ? null : spinnerGrupo.getSelectedItem().toString());
            editor.putString(getString(R.string.subgrupo), spinnerSubGrupo.getSelectedItem().toString().equals(getString(R.string.selecione))
                    ? null : spinnerSubGrupo.getSelectedItem().toString());

            editor.apply();
        });

        callListString = new CallListString(this, CallListString.SECAO, "");
        callListString.startLista();
        callListString.setOnGetLista(this::secao);

    }

    private void secao(List<String> secao) {

        ArrayAdapter<String> adapterSecao = new ArrayAdapter<>(this
                , android.R.layout.simple_list_item_1, secao);
        spinnerSecao.setAdapter(adapterSecao);

        spinnerSecao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                callListString = new CallListString(SetorActivity.this
                        , CallListString.SUBSECAO, parent.getItemAtPosition(position).toString());
                callListString.startLista();
                callListString.setOnGetLista(lista -> subSecao(lista));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void subSecao(List<String> subSecao) {

        ArrayAdapter<String> adapterSubSecao = new ArrayAdapter<>(this
                , android.R.layout.simple_list_item_1, subSecao);
        spinnerSubSecao.setAdapter(adapterSubSecao);

        spinnerSubSecao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                callListString = new CallListString(SetorActivity.this
                        , CallListString.GRUPO, parent.getItemAtPosition(position).toString());
                callListString.startLista();
                callListString.setOnGetLista(lista -> grupo(lista));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void grupo(List<String> grupo) {

        ArrayAdapter<String> adapterGrupo = new ArrayAdapter<>(this
                , android.R.layout.simple_list_item_1, grupo);
        spinnerGrupo.setAdapter(adapterGrupo);

        spinnerGrupo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                callListString = new CallListString(SetorActivity.this, CallListString.SUBGRUPO
                        , parent.getItemAtPosition(position).toString());
                callListString.startLista();
                callListString.setOnGetLista(lista -> subGrupo(lista));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void subGrupo(List<String> subGrupo) {

        ArrayAdapter<String> adapterSubGrupo = new ArrayAdapter<>(this
                , android.R.layout.simple_list_item_1, subGrupo);
        spinnerSubGrupo.setAdapter(adapterSubGrupo);
    }
}
