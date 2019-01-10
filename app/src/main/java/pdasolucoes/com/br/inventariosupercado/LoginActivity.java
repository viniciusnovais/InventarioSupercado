package pdasolucoes.com.br.inventariosupercado;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import pdasolucoes.com.br.inventariosupercado.Inventario.AutorizaoActivity;
import pdasolucoes.com.br.inventariosupercado.Util.Async.AsyncTaskLogin;
import pdasolucoes.com.br.inventariosupercado.Util.Metodo;
import pdasolucoes.com.br.inventariosupercado.Util.XmlParser;


public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsuario, editextSenha;
    SharedPreferences preferences;
    SharedPreferences preferencesConfig;
    private static final int REQUEST_CODE = 0x11;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principal_activity_login);

        preferences = getSharedPreferences(getString(R.string.preference_login_file), MODE_PRIVATE);
        preferencesConfig = getSharedPreferences(getString(R.string.pref_configuracoes),MODE_PRIVATE);

        editTextUsuario = findViewById(R.id.etUser);
        editextSenha = findViewById(R.id.etPassword);
        Button btnEntrar = findViewById(R.id.btnEntrar);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

        UpdateApp updateApp = new UpdateApp();
        updateApp.execute();

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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "PERMISSÃO NEGADA", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UpdateApp extends AsyncTask<String, Void, String> {
        String PATH;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = Metodo.progressDialogCarregamentoMsg(LoginActivity.this, getString(R.string.verificando_atualizacao));
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
                            uri = FileProvider.getUriForFile(LoginActivity.this, getApplicationContext().getPackageName() + ".provider", new File(s + "inventario.apk"));
                        } else {
                            uri = Uri.fromFile(new File(s + "inventario.apk"));
                        }
                        intent.setDataAndType(uri, "application/vnd.android.package-archive");
                        startActivity(intent);

                    } catch (android.content.ActivityNotFoundException anfe) {
                        Metodo.popupMensgam(LoginActivity.this, anfe.getMessage());
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
