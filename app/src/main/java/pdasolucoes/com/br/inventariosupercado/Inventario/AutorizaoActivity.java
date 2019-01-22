package pdasolucoes.com.br.inventariosupercado.Inventario;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
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
import pdasolucoes.com.br.inventariosupercado.Util.XmlParser;

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

    SharedPreferences preferencesConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventario_autorizacao_activity);

        preferencesConfig = getSharedPreferences(getString(R.string.pref_configuracoes),MODE_PRIVATE);

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
            finish();
        });

        btLimpar.setOnClickListener(v -> {

            deletar();
            clearPreferencesInv();
            runOnUiThread(() -> Metodo.toastMsg(AutorizaoActivity.this,getString(R.string.base_limpa)));
        });

        UpdateApp updateApp = new UpdateApp();
        updateApp.execute();

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
            finish();
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
            mDb.divergenciaDao().deletar();
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

    private class UpdateApp extends AsyncTask<String, Void, String> {
        String PATH;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = Metodo.progressDialogCarregamentoMsg(AutorizaoActivity.this, getString(R.string.verificando_atualizacao));
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {

                if (update()) {
                    InputStream is;
                    URL url = new URL(("http://" + preferencesConfig.getString(getString(R.string.pref_servidor), getString(R.string.ip_servidor)) + "/" + preferencesConfig.getString(getString(R.string.pref_diretorio), getString(R.string.diretorio_name)) + "/atualizacao/inventario.apk"));
                    URLConnection c = url.openConnection();

                    PATH = Environment.getExternalStorageDirectory() + "/"
                            + Environment.DIRECTORY_DOWNLOADS + "/";
                    File file = new File(PATH);
                    file.mkdirs();
                    File outputFile = new File(file, "inventario.apk");
                    if (outputFile.exists()) {
                        outputFile.delete();
                    }
                    FileOutputStream fos = new FileOutputStream(outputFile);

                    is = c.getInputStream();

                    byte[] buffer = new byte[1024];
                    int len1;
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);
                    }
                    fos.close();
                    is.close();
                }

            } catch (Exception e) {
                Log.e("UpdateAPP", "Update error! " + e.getMessage());
            }
            return PATH;
        }


        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);


            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                if (s != null) {

                    try {

                        Uri uri;
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                        if (android.os.Build.VERSION.SDK_INT > 23) {
                            uri = FileProvider.getUriForFile(AutorizaoActivity.this, getApplicationContext().getPackageName() + ".provider", new File(s + "inventario.apk"));
                        } else {
                            uri = Uri.fromFile(new File(s + "inventario.apk"));
                        }
                        intent.setDataAndType(uri, "application/vnd.android.package-archive");
                        startActivity(intent);

                    } catch (android.content.ActivityNotFoundException anfe) {
                        Metodo.popupMensgam(AutorizaoActivity.this, anfe.getMessage());
                    }

                }
            }
        }
    }

    private boolean update() {

        XmlParser parser = new XmlParser();
        String xml = parser.getXmlFromUrl("http://" + preferencesConfig.getString(getString(R.string.pref_servidor), getString(R.string.ip_servidor)) + "/" + preferencesConfig.getString(getString(R.string.pref_diretorio), getString(R.string.diretorio_name)) + "/atualizacao/inventario.xml"); // getting XML
        Document doc = parser.getDomElement(xml); // getting DOM element

        NodeList nl = doc.getElementsByTagName("AppUpdater");

        Element e = (Element) nl.item(0);

        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

            if (!versionName.equals(parser.getValue(e, "latestVersionCode"))) {
                return true;
            }

        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }

        return false;
    }
}
