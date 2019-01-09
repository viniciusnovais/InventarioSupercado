package pdasolucoes.com.br.inventariosupercado.Inventario.Loader;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import pdasolucoes.com.br.inventariosupercado.Dao.InventarioDao;
import pdasolucoes.com.br.inventariosupercado.Model.Inventario;
import pdasolucoes.com.br.inventariosupercado.R;
import pdasolucoes.com.br.inventariosupercado.Services.InventarioService;
import pdasolucoes.com.br.inventariosupercado.Util.Async.AsyncTaskLogin;
import pdasolucoes.com.br.inventariosupercado.Util.Metodo;

public class FetchInventario {

    private Context context;
    private static final int LOADER = 21;
    private ProgressDialog progressDialog;
    private LoaderManager loaderManager;
    private Bundle bundle;
    private ResultEstoque resultEstoque;
    private GetInventario getInventario;
    private GetResponseInvPend getResponseInvPend;


    public interface GetResponseInvPend {
        void onPend(String text);
    }

    public void setOnGetInvPendencia(GetResponseInvPend getInvPendencia) {
        this.getResponseInvPend = getInvPendencia;
    }

    public interface GetInventario {
        void getInventario(Inventario inventario);
    }

    public void setOnGetInventario(GetInventario getInventario) {
        this.getInventario = getInventario;
    }

    public interface ResultEstoque {
        void onEstoque(int qtde);
    }

    public void resultEstoqueListener(ResultEstoque resultEstoque) {
        this.resultEstoque = resultEstoque;
    }

    public FetchInventario(Context context, LoaderManager loaderManager, Bundle bundle) {
        this.context = context;
        this.loaderManager = loaderManager;
        this.bundle = bundle;
    }

    public FetchInventario(Context context, Bundle bundle) {
        this.context = context;
        this.bundle = bundle;
    }

    public void starLoadInventario() {

        new AsyncGetInventario().execute(bundle.getString(context.getString(R.string.pref_autorizacao)));
    }


    public void startLoadUpdateStatus() {

        new AsyncUpdateStatusInventario().execute();
    }

    public void starLoadGetInvPend() {
        new AsyncGetInventarioPendencia().execute();
    }

    @SuppressLint("StaticFieldLeak")
    public class AsyncUpdateStatusInventario extends AsyncTask<Object, Integer, Integer> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Metodo.progressDialogCarregamento(context);
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(Object... objects) {

            try {
                return new InventarioService(context).UpdateStatusInv(bundle.getInt(context.getString(R.string.bundle_id_inventario))
                        , bundle.getInt(context.getString(R.string.bundle_status_inventario)));
            } catch (Exception e) {
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();

                if (integer == 0) {
                    Metodo.toastMsg(context, context.getString(R.string.sucesso));
                }
            }
        }
    }

    public void startLoadImportEstoque() {

        new AsyncImportarEstoque().execute();
    }

    @SuppressLint("StaticFieldLeak")
    public class AsyncImportarEstoque extends AsyncTask<Object, Integer, Integer> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Metodo.progressDialogCarregamento(context);
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(Object... objects) {

            try {
                return new InventarioService(context).ImportEstoqueInicial(bundle.getInt(context.getString(R.string.bundle_id_inventario)));
            } catch (Exception e) {
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();

                resultEstoque.onEstoque(integer);
            }
        }
    }

    private class AsyncGetInventario extends AsyncTask<String, Integer, Object> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = Metodo.progressDialogCarregamento(context);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(String... strings) {
            try {
                return new InventarioService(context).GetInventario(strings[0]);
            } catch (Exception e) {
                return e;
            }
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();

                if (!(o instanceof Exception)) {

                    Inventario i = (Inventario) o;
                    if (i != null) {

                        getInventario.getInventario(i);
                    } else {
                        Metodo.popupMensgam(context, context.getString(R.string.autorizacao_invalida_fora_data));
                    }

                } else {
                    Metodo.popupMensgam(context, o.toString());
                }
            }
        }
    }

    private class AsyncGetInventarioPendencia extends AsyncTask<String, Integer, Object> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = Metodo.progressDialogCarregamento(context);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(String... strings) {
            try {
                return new InventarioService(context).GetInventarioPendencia(bundle.getInt(context.getString(R.string.bundle_id_inventario)));
            } catch (Exception e) {
                return e;
            }
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();

                if (!(o instanceof Exception)) {

                    Log.w("response", o.toString());

                    getResponseInvPend.onPend(o.toString());
                } else {
                    Metodo.popupMensgam(context, o.toString());
                }
            }
        }
    }
}
