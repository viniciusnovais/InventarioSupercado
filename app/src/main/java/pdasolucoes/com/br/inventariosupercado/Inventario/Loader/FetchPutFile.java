package pdasolucoes.com.br.inventariosupercado.Inventario.Loader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import java.io.File;
import java.util.List;

import pdasolucoes.com.br.inventariosupercado.Dao.DataBase;
import pdasolucoes.com.br.inventariosupercado.Inventario.Fragment.ColetadoFragment;
import pdasolucoes.com.br.inventariosupercado.Model.ColetaItem;
import pdasolucoes.com.br.inventariosupercado.R;
import pdasolucoes.com.br.inventariosupercado.Services.InventarioService;
import pdasolucoes.com.br.inventariosupercado.Util.AppExecutors;
import pdasolucoes.com.br.inventariosupercado.Util.Metodo;

public class FetchPutFile {

    private Context context;
    private Bundle bundle;
    File file;
    List<ColetaItem> lista;
    DataBase mDb;

    public FetchPutFile(Context context, File file, Bundle bundle, List<ColetaItem> lista) {
        this.context = context;
        this.bundle = bundle;
        this.file = file;
        this.lista = lista;
        mDb = DataBase.getInstancia(context);
    }

    public void startLoadPutFile() {

        String fileName = bundle.getString(context.getString(R.string.bundle_file_name));

        new AsyncPutFile().execute(fileName);
    }

    private class AsyncPutFile extends AsyncTask {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Metodo.progressDialogCarregamento(context);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {


            try {
                new InventarioService(context).PutFile(file, objects[0].toString());
            } catch (Exception e) {
                return e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();

                if (!(o instanceof Exception)) {
                    for (ColetaItem c : lista) {
                        AppExecutors.getsInstance().diskIO().execute(() -> mDb.coletaItemDao().coletadaRealizada(c.getId()));
                    }

                    Metodo.toastMsg(context,context.getString(R.string.enviado));
                }else{
                    Metodo.popupMensgam(context,o.toString());
                }
            }
        }
    }
}
