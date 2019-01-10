package pdasolucoes.com.br.inventariosupercado.Inventario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import pdasolucoes.com.br.inventariosupercado.Dao.DataBase;
import pdasolucoes.com.br.inventariosupercado.Inventario.Loader.FetchInventario;
import pdasolucoes.com.br.inventariosupercado.Inventario.Loader.FetchProduto;
import pdasolucoes.com.br.inventariosupercado.Inventario.Loader.FetchPutFile;
import pdasolucoes.com.br.inventariosupercado.Model.ColetaItem;
import pdasolucoes.com.br.inventariosupercado.PrincipalActivity;
import pdasolucoes.com.br.inventariosupercado.R;
import pdasolucoes.com.br.inventariosupercado.Util.AppExecutors;
import pdasolucoes.com.br.inventariosupercado.Util.Constante;
import pdasolucoes.com.br.inventariosupercado.Util.Metodo;
import pdasolucoes.com.br.inventariosupercado.Util.PreLollipop;
import pdasolucoes.com.br.inventariosupercado.Util.VerificaConexao;

public class PrincipalInvActivity extends PrincipalActivity {


    private TextView textEnviosPend;
    private SharedPreferences preferencesInv;
    int idInventario = 0;
    String autorizacao = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventario_principal_activity);

        preferencesInv = getSharedPreferences(getString(R.string.preference_inv), MODE_PRIVATE);

        textEnviosPend = findViewById(R.id.textEnviosPendentes);
        Button btContagem = findViewById(R.id.btnContagem);
        Button btEnviarContagem = findViewById(R.id.btnEnviarContagem);
        Button btEnviarRecontagem = findViewById(R.id.btnEnviarRecontagem);
        Button btRecontagem = findViewById(R.id.btnRecontagem);
        Button btSair = findViewById(R.id.btnSair);

        PreLollipop.setVectorForPreLollipop(btContagem, R.drawable.ic_list_with_dots, this, Constante.DRAWABLE_LEFT);
        PreLollipop.setVectorForPreLollipop(btEnviarContagem, R.drawable.ic_export, this, Constante.DRAWABLE_LEFT);
        PreLollipop.setVectorForPreLollipop(btEnviarRecontagem, R.drawable.ic_export, this, Constante.DRAWABLE_LEFT);
        PreLollipop.setVectorForPreLollipop(btRecontagem, R.drawable.ic_list_with_dots, this, Constante.DRAWABLE_LEFT);
        PreLollipop.setVectorForPreLollipop(btSair, R.drawable.ic_power_white_24dp, this, Constante.DRAWABLE_LEFT);

        btContagem.setOnClickListener(v -> {
            Intent i = new Intent(PrincipalInvActivity.this, SetorActivity.class);

            SharedPreferences.Editor editor = preferencesInv.edit();
            editor.putInt(getString(R.string.preference_tipo_atividade), Constante.ATIVIDADE_CONTAGEM);
            editor.apply();

            startActivity(i);
        });

        btRecontagem.setOnClickListener(v -> {
            verificaContagemPendente();
        });

        btEnviarContagem.setOnClickListener(v -> AppExecutors.getsInstance().diskIO().execute(() -> {
            DataBase mDb = DataBase.getInstancia(PrincipalInvActivity.this);
            GerarAquivoContagem(mDb.coletaItemDao().listarColetasPendentes());
        }));

        btEnviarRecontagem.setOnClickListener(v -> AppExecutors.getsInstance().diskIO().execute(() -> {
            DataBase mDb = DataBase.getInstancia(PrincipalInvActivity.this);
            GerarAquivoContagem(mDb.coletaItemDao().listarColetasPendentes());
        }));

        btSair.setOnClickListener(v -> finish());

        if (savedInstanceState != null) {

            idInventario = savedInstanceState.getInt(getString(R.string.preference_id_inventario));
            autorizacao = savedInstanceState.getString(getString(R.string.autorizacao));
        } else {
            idInventario = getIntent().getIntExtra(getString(R.string.preference_id_inventario), -1);
            autorizacao = getIntent().getStringExtra(getString(R.string.autorizacao));
        }
    }


    private void verificaContagemPendente() {

        Bundle b = new Bundle();
        b.putInt(getString(R.string.bundle_id_inventario), idInventario);
        FetchInventario fetchInventario = new FetchInventario(this, b);
        fetchInventario.starLoadGetInvPend();
        fetchInventario.setOnGetInvPendencia(text -> {

            if (text.equals("OK")) {

                FetchProduto fetchProduto = new FetchProduto(this,b);
                fetchProduto.startLoadDivergencia();
                fetchProduto.setOnResultDivergencia(result -> {

                    if (result > 0){
                        Intent i = new Intent(PrincipalInvActivity.this, SetorActivity.class);

                        SharedPreferences.Editor editor = preferencesInv.edit();
                        editor.putInt(getString(R.string.preference_tipo_atividade), Constante.ATIVIDADE_DIVERGENCIA);
                        editor.apply();

                        startActivity(i);
                    }else{
                        Metodo.popupMensgam(this,getString(R.string.nao_existe_divergencia));
                    }
                });


            } else {
                Metodo.popupMensgam(PrincipalInvActivity.this, text);
            }

        });

//        AppExecutors.getsInstance().diskIO().execute(() -> {
//
//            DataBase mDb = DataBase.getInstancia(PrincipalInvActivity.this);
//
//            int valor = mDb.produtoDao().existePendencias();
//
//            if (valor > 0) {
//                runOnUiThread(() -> Metodo.popupMensgam(PrincipalInvActivity.this, getString(R.string.existe_pendencias_na_contagem)));
//            } else {
//                Intent i = new Intent(PrincipalInvActivity.this, SetorActivity.class);
//
//                SharedPreferences.Editor editor = preferencesInv.edit();
//                editor.putInt(getString(R.string.preference_tipo_atividade), Constante.ATIVIDADE_DIVERGENCIA);
//                editor.apply();
//
//                startActivity(i);
//            }
//        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putInt(getString(R.string.preference_id_inventario)
                , getIntent().getIntExtra(getString(R.string.preference_id_inventario), -1));

        outState.putString(getString(R.string.autorizacao), getIntent().getStringExtra(getString(R.string.autorizacao)));

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


        idInventario = savedInstanceState.getInt(getString(R.string.preference_id_inventario));
        autorizacao = savedInstanceState.getString(getString(R.string.autorizacao));
    }

    @Override
    protected void onResume() {
        super.onResume();

        AppExecutors.getsInstance().diskIO().execute(() -> {

            DataBase mDb = DataBase.getInstancia(PrincipalInvActivity.this);
            int envios = mDb.coletaItemDao().enviosPendentes(idInventario);
            runOnUiThread(() -> setEnviosPendentes(envios));

        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void setEnviosPendentes(int envios) {

        if (envios > 0) {
            textEnviosPend.setVisibility(View.VISIBLE);
            textEnviosPend.setText(getString(R.string.envio_pendentes).concat(" ").concat(String.format(Locale.getDefault(), "%d", envios)));
        } else {
            textEnviosPend.setVisibility(View.GONE);
        }
    }

    private void GerarAquivoContagem(List<ColetaItem> lista) {

        if (VerificaConexao.isNetworkConnected(this)) {
            if (lista.size() > 0) {
                String filepath = "COLETAITEM" + new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date()) + ".txt";
                File folder = new File(Environment.getExternalStoragePublicDirectory
                        (Environment.DIRECTORY_DOWNLOADS) + "/export/");

                if (!folder.exists()) {
                    folder.mkdirs();
                }
                File file = new File(folder, filepath);
                if (file.exists()) {
                    file.delete();
                }

                FileOutputStream fos;
                StringBuilder conteudo = new StringBuilder();
                try {
                    fos = new FileOutputStream(file);

                    for (ColetaItem c : lista) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
                        conteudo.append(c.getCodAutomacao()).append(";")//sku
                                .append(c.getCodSku()).append(";")//codautomacao
                                .append(c.getQtdeContagem()).append(";")//qtdeContagem
                                .append(c.getIdInventario()).append(";")//idInventario
                                .append(c.getIdEndereco()).append(";")//idEndereco
                                .append(c.getMetodoContagem()).append(";")//lxMetodoContagem
                                .append(c.getMetodoAuditoria()).append(";")//lxMetodoAuditoria
                                .append(c.getTipoAtividade()).append(";")//tipoAtividade
                                .append(c.getIdUsuario()).append(";")//idOperador
                                .append(sdf.format(c.getDtHora())).append(";")//dataHoraColeta
                                .append(c.getExport()).append(";")//lxExport
                                .append(1).append(";")//idLeitura
                                .append(new Random().nextInt()).append(";")//timestamp
                                .append(c.getPreco()).append(";")//preco
                                .append(0).append(";")//validade
                                .append(0).append(";")//qtdedivpreco
                                .append(autorizacao).append(";")//autorizacao
                                .append(c.getFlagAddSub())
                                .append("\r\n");

                    }

                    fos.write(conteudo.toString().getBytes());
                    fos.flush();
                    fos.close();


                    runOnUiThread(() -> {
                        Bundle bundle = new Bundle();
                        bundle.putString(getString(R.string.bundle_file_name), filepath);
                        FetchPutFile fetchPutFile = new FetchPutFile(this, file, bundle, lista);
                        fetchPutFile.startLoadPutFile();

                        textEnviosPend.setVisibility(View.GONE);
                    });


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                runOnUiThread(() -> Metodo.toastMsg(PrincipalInvActivity.this, getString(R.string.n_existe_pendenciq)));
            }
        } else {
            runOnUiThread(() -> Metodo.toastMsg(PrincipalInvActivity.this, getString(R.string.falha_conexao)));
        }
    }
}
