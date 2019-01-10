package pdasolucoes.com.br.inventariosupercado.Inventario.Loader;

import android.app.ProgressDialog;
import android.arch.lifecycle.LifecycleService;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.CircularProgressDrawable;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import pdasolucoes.com.br.inventariosupercado.Dao.DataBase;
import pdasolucoes.com.br.inventariosupercado.R;
import pdasolucoes.com.br.inventariosupercado.Util.Constante;
import pdasolucoes.com.br.inventariosupercado.Util.Metodo;

public class CallListString {


    private GetLista getLista;

    public interface GetLista {
        void onLista(List<String> lista);
    }

    public void setOnGetLista(GetLista getLista) {
        this.getLista = getLista;
    }

    public static final String SECAO = "secao";
    public static final String SUBSECAO = "subsecao";
    public static final String GRUPO = "grupo";
    public static final String SUBGRUPO = "subgrupo";

    private Context context;
    private String filtro;
    private String tipoFiltro;
    private DataBase dataBase;
    private List<String> lista = new ArrayList<>();
    private List<String> listaSecaoSub = new ArrayList<>();

    public CallListString(Context context, String tipoFiltro, String filtro) {
        this.context = context;
        this.tipoFiltro = tipoFiltro;
        this.filtro = filtro;
        this.dataBase = DataBase.getInstancia(context);
        this.lista.add(context.getString(R.string.selecione));
    }


    public void startLista() {

        new AsynGetLista().execute();
    }


    private class AsynGetLista extends AsyncTask {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Metodo.progressDialogCarregamento(context);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            SharedPreferences preferencesInv = context.getSharedPreferences(context.getString(R.string.preference_inv), Context.MODE_PRIVATE);
            switch (tipoFiltro) {

                case SECAO:

                    if (preferencesInv.getInt(context.getString(R.string.preference_tipo_atividade), -1) == Constante.ATIVIDADE_DIVERGENCIA)
                        listaSecaoSub.addAll(dataBase.produtoDao().listarSecaoDiv());
                    else
                        listaSecaoSub.addAll(dataBase.produtoDao().listarSecao());
                    return listaSecaoSub;
                case SUBSECAO:
                    if (preferencesInv.getInt(context.getString(R.string.preference_tipo_atividade), -1) == Constante.ATIVIDADE_DIVERGENCIA)
                        listaSecaoSub.addAll(dataBase.produtoDao().listarSubSecaoDiv(filtro));
                    else
                        listaSecaoSub.addAll(dataBase.produtoDao().listarSubSecao(filtro));
                    return listaSecaoSub;
                case GRUPO:
                    if (preferencesInv.getInt(context.getString(R.string.preference_tipo_atividade), -1) == Constante.ATIVIDADE_DIVERGENCIA)
                        lista.addAll(dataBase.produtoDao().listarGrupoDiv(filtro));
                    else
                        lista.addAll(dataBase.produtoDao().listarGrupo(filtro));
                    return lista;
                case SUBGRUPO:
                    if (preferencesInv.getInt(context.getString(R.string.preference_tipo_atividade), -1) == Constante.ATIVIDADE_DIVERGENCIA)
                        lista.addAll(dataBase.produtoDao().listarSubGrupoDiv(filtro));
                    else
                        lista.addAll(dataBase.produtoDao().listarSubGrupo(filtro));
                    return lista;
                default:
                    return lista;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();

                getLista.onLista((List<String>) o);

            }
        }
    }


}
