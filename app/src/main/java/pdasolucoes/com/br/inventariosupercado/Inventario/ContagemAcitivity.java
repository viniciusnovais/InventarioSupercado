package pdasolucoes.com.br.inventariosupercado.Inventario;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.PolicyNode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import pdasolucoes.com.br.inventariosupercado.Dao.DataBase;
import pdasolucoes.com.br.inventariosupercado.Inventario.Adapter.PagerAdapterColeta;
import pdasolucoes.com.br.inventariosupercado.Inventario.Fragment.ColetadoFragment;
import pdasolucoes.com.br.inventariosupercado.Inventario.Fragment.PendenteFragment;
import pdasolucoes.com.br.inventariosupercado.Inventario.Fragment.ProdutoFragment;
import pdasolucoes.com.br.inventariosupercado.Inventario.Interface.ItemFoco;
import pdasolucoes.com.br.inventariosupercado.Inventario.Loader.FetchPutFile;
import pdasolucoes.com.br.inventariosupercado.Model.ColetaItem;
import pdasolucoes.com.br.inventariosupercado.Model.Endereco;
import pdasolucoes.com.br.inventariosupercado.Model.Inventario;
import pdasolucoes.com.br.inventariosupercado.Model.Produto;
import pdasolucoes.com.br.inventariosupercado.PrincipalActivity;
import pdasolucoes.com.br.inventariosupercado.R;
import pdasolucoes.com.br.inventariosupercado.Util.AppExecutors;
import pdasolucoes.com.br.inventariosupercado.Util.Constante;
import pdasolucoes.com.br.inventariosupercado.Util.Metodo;
import pdasolucoes.com.br.inventariosupercado.Util.PreLollipop;
import pdasolucoes.com.br.inventariosupercado.Util.VerificaConexao;

public class ContagemAcitivity extends PrincipalActivity {


    private LinearLayout linearBarPreco;
    private Produto produto;
    private static TextView tvProduto;
    private static TextView tvPendente;
    private static TextView tvColetado;
    EditText editEndCod;
    static SharedPreferences preferencesInv;
    Button btFecharVoltar;
    Button btZerarPendente;
    private static final int REQUEST_CODE = 0x11;
    static DataBase mDb;
    static Inventario inventario;
    static int focoPosition = 0;
    static TextView tvStatusEndCod;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventario_contagem_activity);

        preferencesInv = getSharedPreferences(getString(R.string.preference_inv), MODE_PRIVATE);

        editEndCod = findViewById(R.id.editEndCod);
        tvProduto = findViewById(R.id.tvProduto);
        tvPendente = findViewById(R.id.tvPendente);
        tvColetado = findViewById(R.id.tvColetado);
        btFecharVoltar = findViewById(R.id.btFecharVoltar);
        btZerarPendente = findViewById(R.id.btZerarPendencia);
        tvStatusEndCod = findViewById(R.id.tvStatusEndCod);

        PreLollipop.setVectorForPreLollipop(editEndCod, R.drawable.ic_search, this, Constante.DRAWABLE_RIGHT);

        inventario = new Inventario();
        mDb = DataBase.getInstancia(ContagemAcitivity.this);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);

        AppExecutors.getsInstance().diskIO().execute(() -> inventario = mDb.inventarioDao().retornaDadosInventario());

        statusEndProduto(this);

        editEndCod.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if ((event.getRawX() >= editEndCod.getRight() - editEndCod.getTotalPaddingRight())) {
                    if (!editEndCod.getText().toString().equals("")) {
                        requestEndCod(editEndCod.getText().toString());
                    }
                }
            }
            return false;
        });

        editEndCod.setOnKeyListener((v, keyCode, event) -> {

            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (!editEndCod.getText().toString().equals("")) {
                        requestEndCod(editEndCod.getText().toString());
                    }
                }
            }
            return false;
        });

        showFragment(ProdutoFragment.instanciar());
        focoProduto(this);

        tvProduto.setOnClickListener(v -> {
            showFragment(ProdutoFragment.instanciar());
            focoProduto(this);
        });

        tvPendente.setOnClickListener(v -> {
            showFragment(PendenteFragment.instanciar());
            focoPendente();
        });

        tvColetado.setOnClickListener(v -> {
            showFragment(ColetadoFragment.instanciar());
            focoColetado();
        });

        btZerarPendente.setOnClickListener(v -> {
            if (focoPosition == 1) {
                popupDesejaZerarPendentes();
            } else {
                Metodo.toastMsg(this, getString(R.string.va_para_aba_pendentes));
            }
        });

        btFecharVoltar.setOnClickListener(v -> {

            if (btFecharVoltar.getText().toString().equals(getString(R.string.fechar_endereco)))
                popupFinalizarEndereco();
            else
                finish();
        });
    }

    private void requestEndCod(String endCod) {
        showFragment(ProdutoFragment.instanciar(endCod));
        focoProduto(this);
    }

    public static void focoProduto(Context context) {
        tvProduto.setTextAppearance(context, R.style.TextAppearance_Medium_Blue);
        tvPendente.setTextAppearance(context, R.style.TextAppearance_Small_White);
        tvColetado.setTextAppearance(context, R.style.TextAppearance_Small_White);

        focoPosition = 0;
    }

    public void focoPendente() {
        tvPendente.setTextAppearance(this, R.style.TextAppearance_Medium_Blue);
        tvProduto.setTextAppearance(this, R.style.TextAppearance_Small_White);
        tvColetado.setTextAppearance(this, R.style.TextAppearance_Small_White);

        focoPosition = 1;
    }

    public void focoColetado() {
        tvColetado.setTextAppearance(this, R.style.TextAppearance_Medium_Blue);
        tvPendente.setTextAppearance(this, R.style.TextAppearance_Small_White);
        tvProduto.setTextAppearance(this, R.style.TextAppearance_Small_White);

        focoPosition = 2;
    }

    private void popupFinalizarEndereco() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.finalizar_contagem))
                .setMessage(getString(R.string.deseja_finalizar_contagem))
                .setPositiveButton(getString(R.string.sim), (dialog, which) -> {
                    dialog.dismiss();
                    int idEndereco = preferencesInv.getInt(getString(R.string.preference_id_endereco), -1);
                    AppExecutors.getsInstance().diskIO().execute(() -> GerarAquivoContagem(
                            mDb.coletaItemDao().listar(idEndereco)));

                    limparArquivoPref();

                    showFragment(ProdutoFragment.instanciar());
                    focoProduto(this);

                })
                .setNegativeButton(getString(R.string.nao), (dialog, which) -> dialog.dismiss())
                .create().show();
    }

    private void popupDesejaZerarPendentes() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.zerar))
                .setMessage(getString(R.string.deseja_zerar_produtos_pendentes))
                .setPositiveButton(getString(R.string.sim), (dialog, which) -> {
                    dialog.dismiss();
                    //zerar prodtuos pendentes
                    adicionarColetaZeradas();
                })
                .setNegativeButton(getString(R.string.nao), (dialog, which) -> dialog.dismiss())
                .create().show();
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
                                .append(inventario.getAutorizacao()).append(";")//autorizacao
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
                    });

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            runOnUiThread(() -> Metodo.toastMsg(ContagemAcitivity.this, getString(R.string.endereco_fechado)));
        }

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

    public static void statusEndProduto(Context context) {
        AppExecutors.getsInstance().diskIO().execute(() -> {

            int idEndereco = preferencesInv.getInt(context.getString(R.string.preference_id_endereco), -1);
            int tipoAtividade = preferencesInv.getInt(context.getString(R.string.preference_tipo_atividade), -1);
            SharedPreferences preferencesFiltro = context.getSharedPreferences(context.getString(R.string.filtros),MODE_PRIVATE);

            String secao = preferencesFiltro.getString(context.getString(R.string.secao), "");
            String subSecao = preferencesFiltro.getString(context.getString(R.string.subsecao), "");
            String grupo = preferencesFiltro.getString(context.getString(R.string.grupo), null);
            String subGrupo = preferencesFiltro.getString(context.getString(R.string.subgrupo), null);

            if (preferencesInv.getInt(context.getString(R.string.invEnderecoAberto), -1)
                    == Constante.ENDERECO_ABERTO) {

                int qtdeTotalProdutos = mDb.produtoDao().qtdeTotalProdutos(secao,subSecao,grupo,subGrupo);
                int qtdeProdutosContados = mDb.coletaItemDao().qtdeProdutosContadosPorEndereco(idEndereco, tipoAtividade);
                ((Activity) context).runOnUiThread(() -> tvStatusEndCod.setText(String.format(Locale.getDefault(), "%d", qtdeProdutosContados)
                        .concat(" - ").concat(String.format(Locale.getDefault(), "%d", qtdeTotalProdutos))));
            } else {

                int qtdeTotalEnderecos = mDb.enderecoDao().qtdeTotalEnderecos();
                int qtdeEnderecosContagem = mDb.coletaItemDao().qtdeEnderecosContagem(inventario.getId(), tipoAtividade);
                ((Activity) context).runOnUiThread(() -> tvStatusEndCod.setText(String.format(Locale.getDefault(), "%d", qtdeEnderecosContagem)
                        .concat(" - ").concat(String.format(Locale.getDefault(), "%d", qtdeTotalEnderecos))));

            }
        });
    }

    private void showFragment(Fragment f) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameFragment, f)
                .commit();
    }

    private void limparArquivoPref() {


        SharedPreferences.Editor editor = preferencesInv.edit();
        editor.putInt(getString(R.string.preference_id_endereco), -1);
        editor.putInt(getString(R.string.preference_inv_auditoria), -1);
        editor.putString(getString(R.string.preference_nome_secao), getString(R.string.local));
        editor.putInt(getString(R.string.tipo_leitura), -1);
        editor.putInt(getString(R.string.preference_inv_contagem), -1);
        editor.putInt(getString(R.string.invEnderecoAberto), -1);
        editor.apply();

        btFecharVoltar.setText(getString(R.string.voltar));
        editEndCod.setHint(getString(R.string.endereco));
        statusEndProduto(this);
    }

    @Override
    public void onBackPressed() {

        if (btFecharVoltar.getText().toString().equals(getString(R.string.fechar_endereco)))
            popupFinalizarEndereco();
        else
            finish();
    }

    private void adicionarColetaZeradas() {

        AppExecutors.getsInstance().diskIO().execute(() -> {

            try {

                SharedPreferences preferencesLogin = getSharedPreferences(getString(R.string.preference_login_file), MODE_PRIVATE);
                SharedPreferences preferencesFiltro = getSharedPreferences(getString(R.string.filtros), Context.MODE_PRIVATE);

                String secao = preferencesFiltro.getString(getString(R.string.secao), "");
                String subSecao = preferencesFiltro.getString(getString(R.string.subsecao), "");
                String grupo = preferencesFiltro.getString(getString(R.string.grupo), null);
                String subGrupo = preferencesFiltro.getString(getString(R.string.subgrupo), null);

                for (Produto produto : mDb.produtoDao().listarPendente(preferencesInv.getInt(getString(R.string.preference_id_endereco), -1)
                        , preferencesInv.getInt(getString(R.string.preference_tipo_atividade), -1)
                        , secao
                        , subSecao
                        , grupo
                        , subGrupo)) {

                    ColetaItem coletaItem = new ColetaItem(produto.getCodSku()
                            , produto.getCodAutomacao()
                            , 0
                            , preferencesInv.getInt(getString(R.string.preference_id_endereco), -1)
                            , new Date()
                            , produto.getDescSku()
                            , preferencesInv.getInt(getString(R.string.preference_inv_contagem), -1)
                            , preferencesInv.getInt(getString(R.string.preference_inv_auditoria), -1)
                            , preferencesInv.getInt(getString(R.string.preference_tipo_atividade), -1)
                            , Constante.FLAG_EXPORT_PENDENTE
                            , produto.getPreco()
                            , preferencesLogin.getInt(getString(R.string.preference_cod_usuario), -1)
                            , 0
                            , inventario.getId());

                    mDb.coletaItemDao().inserir(coletaItem);
                }

                runOnUiThread(() -> {
                    showFragment(ColetadoFragment.instanciar());
                    focoColetado();
                    Metodo.focoEditText(this, editEndCod);
                    Metodo.toastMsg(this, getString(R.string.salvo_com_sucesso));
                });
            } catch (Exception e) {
                runOnUiThread(() -> Metodo.toastMsg(this, getString(R.string.problema_ao_salvar)));
            }


        });
    }
}
