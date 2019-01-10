package pdasolucoes.com.br.inventariosupercado.Inventario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import java.util.List;

import pdasolucoes.com.br.inventariosupercado.Dao.DataBase;
import pdasolucoes.com.br.inventariosupercado.Dao.InventarioDao;
import pdasolucoes.com.br.inventariosupercado.Inventario.Loader.FetchDepartamento;
import pdasolucoes.com.br.inventariosupercado.Inventario.Loader.FetchEndereco;
import pdasolucoes.com.br.inventariosupercado.Inventario.Loader.FetchInventario;
import pdasolucoes.com.br.inventariosupercado.Inventario.Loader.FetchProduto;
import pdasolucoes.com.br.inventariosupercado.Inventario.Loader.FetchSetor;
import pdasolucoes.com.br.inventariosupercado.LoginActivity;
import pdasolucoes.com.br.inventariosupercado.Model.Departamento;
import pdasolucoes.com.br.inventariosupercado.Model.Endereco;
import pdasolucoes.com.br.inventariosupercado.Model.Inventario;
import pdasolucoes.com.br.inventariosupercado.Model.Setor;
import pdasolucoes.com.br.inventariosupercado.PrincipalActivity;
import pdasolucoes.com.br.inventariosupercado.R;
import pdasolucoes.com.br.inventariosupercado.Util.AppExecutors;
import pdasolucoes.com.br.inventariosupercado.Util.Metodo;

public class AutorizaoActivity extends PrincipalActivity
        implements FetchInventario.GetInventario
        , FetchDepartamento.GetDepartamento
        , FetchEndereco.GetEndereco
        , FetchSetor.GetSetor
        , FetchProduto.ResultProduto {

    EditText editAutorizacao;
    FetchInventario fetchInventario;
    DataBase mDb;
    int idInventario = 0;
    String autorizacao = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventario_autorizacao_activity);

        mDb = DataBase.getInstancia(this);

        Button btVoltar = findViewById(R.id.btVoltar);
        Button btConfirmar = findViewById(R.id.btConfirmar);
        Button btLimpar = findViewById(R.id.btLimparBase);
        editAutorizacao = findViewById(R.id.editAutorizacao);

        btConfirmar.setOnClickListener(v -> {

            if (!TextUtils.isEmpty(editAutorizacao.getText().toString())) {

                autorizacao = editAutorizacao.getText().toString();
                verificarAutorizacao(autorizacao);
            } else {
                Metodo.toastMsg(AutorizaoActivity.this, getString(R.string.campos_branco));
                Metodo.focoEditText(AutorizaoActivity.this, editAutorizacao);
            }

            editAutorizacao.setText("");
        });

        btVoltar.setOnClickListener(v -> {
//            SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_login_file), MODE_PRIVATE);
//            SharedPreferences.Editor editor = preferences.edit();
//            editor.clear();
//            editor.apply();
            Intent i = new Intent(AutorizaoActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        });

        btLimpar.setOnClickListener(v -> {

            deletar();
            clearPreferencesInv();
            runOnUiThread(() -> Metodo.toastMsg(AutorizaoActivity.this,getString(R.string.base_limpa)));
        });
    }

    private void clearPreferencesInv(){

        SharedPreferences preferencesInv = getSharedPreferences(getString(R.string.preference_inv), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencesInv.edit();
        editor.clear();
        editor.apply();
    }


    private void Importanto() {

        deletar();
        clearPreferencesInv();

        Bundle bundle = new Bundle();
        bundle.putString(getString(R.string.pref_autorizacao), autorizacao);

        fetchInventario = new FetchInventario(this, getSupportLoaderManager(), bundle);
        fetchInventario.starLoadInventario();
        fetchInventario.setOnGetInventario(this);

    }


    private void verificarAutorizacao(String autorizacao) {


        AppExecutors.getsInstance().diskIO().execute(() -> ImportarOuAcessar(mDb.inventarioDao().getInventario(autorizacao)));
    }

    private void ImportarOuAcessar(Inventario inventario) {


        if (inventario != null && inventario.getAutorizacao() != null) {

            Intent i = new Intent(AutorizaoActivity.this, PrincipalInvActivity.class);
            i.putExtra(getString(R.string.preference_id_inventario), inventario.getId());
            i.putExtra(getString(R.string.autorizacao), inventario.getAutorizacao());
            startActivity(i);
        } else {

            runOnUiThread(this::Importanto);

        }
    }


    @Override
    public void getInventario(Inventario inventario) {

        Bundle bundle = new Bundle();
        idInventario = inventario.getId();
        bundle.putInt(getString(R.string.bundle_id_inventario), inventario.getId());

        if (inventario.getStatus() != 4) {

            if (inventario.getStatus() == 0) {
                bundle.putInt(getString(R.string.bundle_status_inventario), 1);
                fetchInventario = new FetchInventario(this, bundle);
                fetchInventario.startLoadUpdateStatus();
            }


            if (inventario.getTipo() != 3) {
                //importando estoque
                fetchInventario = new FetchInventario(this, bundle);
                fetchInventario.startLoadImportEstoque();
                fetchInventario.resultEstoqueListener(qtde -> {
                    if (qtde <= 0) {
                        //estoque não importado na retaguarda
                        bundle.putInt(getString(R.string.bundle_status_inventario), 0);
                        fetchInventario = new FetchInventario(this, bundle);
                        fetchInventario.startLoadUpdateStatus();
                        Metodo.toastMsg(AutorizaoActivity.this, getString(R.string.estoque_n_importado));
                        return;

                    }
                });
            }

            //importando departamento
            FetchDepartamento fetchDepartamento = new FetchDepartamento(this, bundle);
            fetchDepartamento.startLoadDepartamento();
            fetchDepartamento.setOnDepartamento(this);

            //importando setor
            FetchSetor fetchSetor = new FetchSetor(this, bundle);
            fetchSetor.startLoadSetor();
            fetchSetor.setOnSetor(this);

            //importando endereco
            FetchEndereco fetchEndereco = new FetchEndereco(this, bundle);
            fetchEndereco.startLoadEndereco();
            fetchEndereco.setOnEndereco(this);

            //importando produto
            FetchProduto fetchProduto = new FetchProduto(this, bundle);
            fetchProduto.startLoadFileProduto();
            fetchProduto.setOnResult(this);

        } else {

            Metodo.popupMensgam(this, getString(R.string.inventario_fechado));
        }

    }

    private void deletar() {

        AppExecutors.getsInstance().diskIO().execute(() -> {
            mDb.enderecoDao().deletar();
            mDb.inventarioDao().deletar();
            mDb.departamentoDao().deletar();
            mDb.setorDao().deletar();
            mDb.produtoDao().deletar();
            mDb.coletaItemDao().deletar();
        });
    }

    @Override
    public void getDepartamento(List<Departamento> departamentoList) {

        Log.i("departamento", departamentoList.get(0).getDepartamento());
    }


    @Override
    public void getEndereco(List<Endereco> enderecoList) {

        Log.i("endereço", enderecoList.get(0).getEndereco());
    }

    @Override
    public void getSetor(List<Setor> setorList) {

        Log.i("setor", setorList.get(0).getSetor());
    }

    @Override
    public void onResult(int result) {

        Log.i("produto", result + "");

        if (result > 0) {
            Intent i = new Intent(AutorizaoActivity.this, PrincipalInvActivity.class);
            i.putExtra(getString(R.string.preference_id_inventario), idInventario);
            i.putExtra(getString(R.string.autorizacao), autorizacao);
            startActivity(i);
        } else {
            deletar();
            clearPreferencesInv();

            Metodo.popupMensgam(this, getString(R.string.estoque_produtos_zerados));
        }
    }
}
