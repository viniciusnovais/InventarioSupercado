package pdasolucoes.com.br.inventariosupercado.Inventario.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Locale;
import java.util.function.BinaryOperator;

import pdasolucoes.com.br.inventariosupercado.Dao.DataBase;
import pdasolucoes.com.br.inventariosupercado.Inventario.Adapter.ListaProdutoAdapter;
import pdasolucoes.com.br.inventariosupercado.Inventario.ContagemAcitivity;
import pdasolucoes.com.br.inventariosupercado.Inventario.Interface.ItemFoco;
import pdasolucoes.com.br.inventariosupercado.Model.ColetaItem;
import pdasolucoes.com.br.inventariosupercado.Model.Endereco;
import pdasolucoes.com.br.inventariosupercado.Model.Inventario;
import pdasolucoes.com.br.inventariosupercado.Model.Produto;
import pdasolucoes.com.br.inventariosupercado.R;
import pdasolucoes.com.br.inventariosupercado.Util.AppExecutors;
import pdasolucoes.com.br.inventariosupercado.Util.Constante;
import pdasolucoes.com.br.inventariosupercado.Util.Metodo;

public class ProdutoFragment extends Fragment implements View.OnKeyListener, CompoundButton.OnCheckedChangeListener {

    private String endCod;
    private int tipoAtividade;
    private Endereco endereco = new Endereco();
    private Inventario inventario = new Inventario();
    private Produto produto = new Produto();
    private int tipoLeitura;
    private int invMetodoAuditoria;
    private int invMetodoContagem;
    private int position = 0;
    private DataBase mDb;
    private List<Produto> listarProduto = new ArrayList<>();
    //private RecyclerView recyclerView;
    private TextView tvProduto;
    private SharedPreferences preferencesFiltro;
    private Context context;
    private SharedPreferences preferencesInv;
    private SharedPreferences preferencesLogin;
    float qtde = -1;
    String origem = "";
    EditText editQtde;
    TextView tvEan;
    TextView tvSku;
    TextView tvSecao;
    ImageView imageLeft;
    ImageView imageRight;
    CheckBox checkAddUp;
    EditText editEndCod;
    Button btVoltarFechar;
    EditText editDecimal;
    static boolean primeiroAcesso = true;
    //boolean isPPV = false;
    String codigoPPV = "";
    float qtdeColetaPPV;
    String qtdePPV;

    public static Fragment instanciar(String endCod) {

        ProdutoFragment f = new ProdutoFragment();
        Bundle b = new Bundle();
        b.putString("endCod", endCod);
        f.setArguments(b);
        primeiroAcesso = true;
        return f;
    }

    public static Fragment instanciar(ColetaItem coletaItem, String origem) {

        ProdutoFragment f = new ProdutoFragment();
        Bundle b = new Bundle();
        b.putString("endCod", coletaItem.getCodAutomacao());
        b.putFloat("qtde", coletaItem.getQtdeContagem());
        b.putString("origem", origem);
        f.setArguments(b);
        primeiroAcesso = true;
        return f;
    }


    public static Fragment instanciar() {

        ProdutoFragment f = new ProdutoFragment();
        primeiroAcesso = false;
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getContext();

        if (context != null) {

            preferencesFiltro = context.getSharedPreferences(getString(R.string.filtros), Context.MODE_PRIVATE);
            preferencesInv = context.getSharedPreferences(getString(R.string.preference_inv), Context.MODE_PRIVATE);
            preferencesLogin = context.getSharedPreferences(getString(R.string.preference_login_file), Context.MODE_PRIVATE);
            if (getArguments() != null) {
                endCod = getArguments().getString("endCod");
                qtde = getArguments().getFloat("qtde");
                origem = getArguments().getString("origem");
            }

            editEndCod = ((Activity) context).findViewById(R.id.editEndCod);
            btVoltarFechar = ((Activity) context).findViewById(R.id.btFecharVoltar);

            mDb = DataBase.getInstancia(getContext());
            tipoAtividade = preferencesInv.getInt(getString(R.string.preference_tipo_atividade), -1);

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.inventario_produto_fragment, container, false);

        editQtde = v.findViewById(R.id.editQtde);
        tvEan = v.findViewById(R.id.tvEan);
        tvSku = v.findViewById(R.id.tvSku);
        tvSecao = v.findViewById(R.id.tvSecao);
        tvProduto = v.findViewById(R.id.tvProduto);
        imageLeft = v.findViewById(R.id.imageLeft);
        imageRight = v.findViewById(R.id.imageRight);
        checkAddUp = v.findViewById(R.id.checkAddUp);
        editDecimal = v.findViewById(R.id.editDecimal);

        editQtde.setOnKeyListener(this);
        checkAddUp.setOnCheckedChangeListener(this);

        tvSecao.setText(preferencesInv.getString(getString(R.string.preference_nome_secao), getString(R.string.local)));

        if (primeiroAcesso)
            validaEnderecoCodigo();
        else
            listarProdutos();


        if(listarProduto.size() == 1)
            imageRight.setVisibility(View.GONE);
        else
            imageRight.setVisibility(View.VISIBLE);

        imageLeft.setVisibility(View.GONE);
        imageRight.setOnClickListener(v1 -> {

            position++;

            if (position == (listarProduto.size() - 1)) {
                imageRight.setVisibility(View.INVISIBLE);
            } else {
                imageLeft.setVisibility(View.VISIBLE);
            }

            if (listarProduto.size() > 0)
                setInfoProduto(listarProduto.get(position));
        });



        imageLeft.setOnClickListener(v12 -> {

            position--;
            if (position == 0) {
                imageLeft.setVisibility(View.INVISIBLE);
            } else {
                imageRight.setVisibility(View.VISIBLE);
            }

            if (listarProduto.size() > 0)
                setInfoProduto(listarProduto.get(position));
        });

        return v;
    }

    private void verificarEndereco() {

        AppExecutors.getsInstance().diskIO().execute(() ->
                {
                    endereco = mDb.enderecoDao().pesquisarEndereco(endCod);

                    if (endereco != null && !TextUtils.isEmpty(endereco.getDepartamento())) {

                        tipoLeitura = endereco.getIdMetodoLeitura();

                        if (tipoAtividade == Constante.ATIVIDADE_DIVERGENCIA) {
                            invMetodoContagem = 0;
                            invMetodoAuditoria = endereco.getIdMetodoAuditoria();
                        } else {
                            invMetodoAuditoria = 0;
                            invMetodoContagem = endereco.getIdMetodoContagem();
                        }

                        abreEndereco();

                        ((Activity) context).runOnUiThread(() -> {
                            editEndCod.setHint(context.getString(R.string.ean_plu));
                            btVoltarFechar.setText(getString(R.string.fechar_endereco));
                            tvSecao.setText(endereco.getDepartamento());
                            ContagemAcitivity.statusEndProduto(context);
                        });

                        listarProdutos();

                    } else {
                        ((Activity) context).runOnUiThread(() -> {
                            Metodo.focoEditText(context, editEndCod);
                            Metodo.popupMensgam(context, getString(R.string.endereco_n_existe));
                        });

                        listarProdutos();
                    }
                }

        );
    }

    private void validaEnderecoCodigo() {


        if (preferencesInv.getInt(getString(R.string.invEnderecoAberto), -1)
                != Constante.ENDERECO_ABERTO) {
            verificarEndereco();
        } else {

            Metodo.focoEditText(context, editQtde);

            if (endereco == null
                    || TextUtils.isEmpty(endereco.getDepartamento())) {
                verificaCodigo();
            } else {
                Metodo.popupMensgam(context, getString(R.string.codigo_e_endereco));
                Metodo.focoEditText(context, editEndCod);
            }
        }
    }

    private void verificaCodigo() {

        AppExecutors.getsInstance().diskIO().execute(() -> {
            inventario = mDb.inventarioDao().retornaDadosInventario();

            if (endCod.substring(0, 1).equals(String.format(Locale.getDefault(), "%d", inventario.getDigitoInicial()))
                    && endCod.length() == inventario.getTamanhoEtiqueta()) {
                //ppv
                //isPPV = true;

                codigoPPV = endCod;

                endCod = String.format(Locale.getDefault(), "%d", Integer.parseInt(
                        endCod.substring(inventario.getTamanhoSkuDe(), (inventario.getTamanhoSkuAte() + 1))));

                if (inventario.getCasasDecimais() > 0) {

                    qtdePPV = codigoPPV.substring(inventario.getTamanhoPrecoDe(), (inventario.getTamanhoPrecoDe() + inventario.getCasaInteiras()))
                            .concat(".").concat(codigoPPV.substring(inventario.getTamanhoPrecoAte(), (inventario.getTamanhoPrecoAte() + inventario.getCasasDecimais())));

                } else {

                    qtdePPV = codigoPPV.substring(inventario.getTamanhoPrecoDe(), (inventario.getTamanhoPrecoDe() + inventario.getCasasDecimais()))
                            .concat(codigoPPV.substring(inventario.getTamanhoPrecoAte(), (inventario.getTamanhoPrecoAte() + inventario.getCasasDecimais())));
                }

                buscaProdutoLista();

            } else {

                buscaProdutoLista();
            }
        });
    }

    private void buscaProdutoLista() {

        AppExecutors.getsInstance().diskIO().execute(() -> {

            String secao = preferencesFiltro.getString(getString(R.string.secao), "");
            String subSecao = preferencesFiltro.getString(getString(R.string.subsecao), "");
            String grupo = preferencesFiltro.getString(getString(R.string.grupo), null);
            String subGrupo = preferencesFiltro.getString(getString(R.string.subgrupo), null);

            listarProduto = mDb.produtoDao().listar(secao, subSecao, grupo, subGrupo);

            pesquisaCodAutomacao();

        });
    }

    private void adicionarColeta(int flagAddSub) {

        AppExecutors.getsInstance().diskIO().execute(() -> {

            try {

                ColetaItem coletaItem = new ColetaItem(
                        produto.getCodSku()
                        , produto.getCodAutomacao()
                        , editDecimal.getText().toString().equals("") ? Float.parseFloat(editQtde.getText().toString()) :
                        Float.parseFloat(editQtde.getText().toString().concat(".").concat(editDecimal.getText().toString()))
                        , preferencesInv.getInt(getString(R.string.preference_id_endereco), -1)
                        , new Date()
                        , produto.getDescSku()
                        , preferencesInv.getInt(getString(R.string.preference_inv_contagem), -1)
                        , preferencesInv.getInt(getString(R.string.preference_inv_auditoria), -1)
                        , tipoAtividade
                        , Constante.FLAG_EXPORT_PENDENTE
                        , produto.getPreco()
                        , preferencesLogin.getInt(getString(R.string.preference_cod_usuario), -1)
                        , flagAddSub
                        , inventario.getId());

                mDb.coletaItemDao().inserir(coletaItem);

                ContagemAcitivity.statusEndProduto(context);

                ((Activity) context).runOnUiThread(() -> {
                    editQtde.setText("");
                    editDecimal.setText("");
                    Metodo.focoEditText(context, editEndCod);
                    Metodo.toastMsg(context, getString(R.string.salvo_com_sucesso));
                    produto = new Produto();
                });
            } catch (Exception e) {
                ((Activity) context).runOnUiThread(() -> Metodo.toastMsg(context, getString(R.string.problema_ao_salvar)));
            }


        });

    }

    private void listarProdutos() {

        AppExecutors.getsInstance().diskIO().execute(() -> {

            String secao = preferencesFiltro.getString(getString(R.string.secao), "");
            String subSecao = preferencesFiltro.getString(getString(R.string.subsecao), "");
            String grupo = preferencesFiltro.getString(getString(R.string.grupo), null);
            String subGrupo = preferencesFiltro.getString(getString(R.string.subgrupo), null);

            listarProduto = mDb.produtoDao().listar(secao, subSecao, grupo, subGrupo);

            ((Activity) context).runOnUiThread(() ->
                    {
                        if (listarProduto.size() > 0)
                            setInfoProduto(listarProduto.get(0));
                        Metodo.focoEditText(context, editEndCod);
                    }
            );

        });
    }

    private void setInfoProduto(Produto p) {

        tvSku.setText(p.getCodSku());
        tvEan.setText(p.getCodAutomacao());
        tvProduto.setText(p.getDescSku());
    }

    private void abreEndereco() {

        SharedPreferences.Editor editor = preferencesInv.edit();
        editor.putInt(getString(R.string.invEnderecoAberto), Constante.ENDERECO_ABERTO);
        editor.apply();

        salvarInfoEndereco();
    }

    private void pesquisaCodAutomacao() {
        int cnt = 0;
        int index;
        final boolean[] cadastrado = {false};
        for (Produto p : listarProduto) {

            if (p.getCodAutomacao().equals(endCod)) {
                index = cnt;
                ((Activity) context).runOnUiThread(() -> {
                    produto = listarProduto.get(index);
                    if (produto != null) {

                        if (produto.getPesavel().equals("N")) {
                            //desabilita fracionado
                            editDecimal.setVisibility(View.GONE);
                            editQtde.setEms(10);

                            Metodo.focoEditTextComTeclado(context, editQtde);
                        } else {
                            if (!TextUtils.isEmpty(codigoPPV) && codigoPPV.length() > 7) {
                                if (inventario.getPesoQtde() == 0) {
                                    if (produto.getPreco() != 0) {

                                        qtdeColetaPPV = (Float.parseFloat(qtdePPV) / produto.getPreco());
                                        Log.i("qtdeColetaPPV", qtdeColetaPPV + "");
                                    } else {
                                        qtdeColetaPPV = (float) 0.0;
                                    }
                                    produto.setCodAutomacao(codigoPPV);
                                    editQtde.setText(String.format(Locale.getDefault(), "%f", qtdeColetaPPV).split("[.]")[0]);
                                    editDecimal.setText(String.format(Locale.getDefault(), "%f", qtdeColetaPPV).split("[.]")[1]);
                                    Metodo.focoEditTextComTecladoSemApagar(context, editDecimal);
                                }
                            } else {
                                editDecimal.setText("");
                                Metodo.focoEditTextComTeclado(context, editQtde);
                            }

                            editDecimal.setVisibility(View.VISIBLE);
                            editQtde.setEms(5);

                            editDecimal.setOnKeyListener(this);
                        }
                        cadastrado[0] = true;
                        setInfoProduto(produto);
                    }
                    if (origem != null
                            && origem.equals(getString(R.string.coletado))) {
                        checkAddUp.setChecked(false);
                        if (produto.getPesavel().equals("N"))
                            editQtde.setText(String.format(Locale.getDefault(), "%.0f", qtde));
                        else {
                            editQtde.setText(String.format(Locale.getDefault(), "%f", qtde).split("[.]")[0]);
                            editDecimal.setText(String.format(Locale.getDefault(), "%f", qtde).split("[.]")[1]);
                        }
                    }
                });
                return;
            }
            cnt++;
        }

        if (!cadastrado[0])
            ((Activity) context).runOnUiThread(() -> Metodo.popupMensgam(context, getString(R.string.produto_n_cadastrado)));

    }

    private void salvarInfoEndereco() {

        SharedPreferences.Editor editor = preferencesInv.edit();
        editor.putInt(getString(R.string.preference_id_endereco), endereco.getIdEndereco());
        editor.putInt(getString(R.string.preference_inv_contagem), invMetodoContagem);
        editor.putInt(getString(R.string.preference_inv_auditoria), invMetodoAuditoria);
        editor.putString(getString(R.string.preference_nome_secao), endereco.getDepartamento());
        editor.putFloat(getString(R.string.preference_qtde_max_cont), endereco.getQuantidade());
        editor.putInt(getString(R.string.tipo_leitura), tipoLeitura);
        editor.apply();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_ENTER) {

                if (v.getId() == R.id.editQtde) {

                    if (editDecimal.getVisibility() == View.VISIBLE) {
                        Metodo.focoEditTextComTecladoSemApagar(context, editDecimal);
                    } else {
                        if (!editQtde.getText().toString().equals("")) {
                            if (produto.getCodAutomacao() == null) {
                                editQtde.setText("");
                                Metodo.popupMensgam(context, getString(R.string.informe_o_produto));
                            } else {
                                if (Float.parseFloat(editQtde.getText().toString()) <= preferencesInv.getFloat(getString(R.string.preference_qtde_max_cont), 5000))
                                    verificaFlagAddUp();
                                else{
                                    Metodo.focoEditTextComTeclado(context,editQtde);
                                    Metodo.popupMensgam(context, getString(R.string.quantidade_excedida));
                                }
                            }
                        }
                    }
                } else {
                    if (!editQtde.getText().toString().equals("")
                            && !editDecimal.getText().toString().equals("")) {
                        if (produto.getCodAutomacao() == null) {
                            editQtde.setText("");
                            editDecimal.setText("");
                            Metodo.popupMensgam(context, getString(R.string.informe_o_produto));
                        } else {
                            if (Float.parseFloat(editQtde.getText().toString().concat(".").concat(editDecimal.getText().toString()))
                                    <= preferencesInv.getFloat(getString(R.string.preference_qtde_max_cont), 5000))
                                verificaFlagAddUp();
                            else{
                                editDecimal.setText("");
                                Metodo.focoEditTextComTeclado(context,editQtde);
                                Metodo.popupMensgam(context, getString(R.string.quantidade_excedida));
                            }
                        }
                    }
                }
            }
        }
        return false;
    }


    private void verificaFlagAddUp() {
        if (checkAddUp.isChecked()) {
            adicionarColeta(0);
        } else {

            AppExecutors.getsInstance().diskIO().execute(() -> {

                if (mDb.coletaItemDao().existeColeta(produto.getCodSku()
                        ,preferencesInv.getInt(getString(R.string.preference_id_endereco),-1)) == 1) {
                    mDb.coletaItemDao().deletar(produto.getCodSku());
                    adicionarColeta(1);
                } else {
                    ((Activity) context).runOnUiThread(() -> {
                        Metodo.popupMensgam(context, getString(R.string.para_substituir_deve_existir_contagem));
                        editQtde.setText("");
                        checkAddUp.setChecked(true);
                    });

                }
            });

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            checkAddUp.setText(getString(R.string.adicionando_item));
        } else {
            checkAddUp.setText(getString(R.string.substituir_item));
        }
    }
}
