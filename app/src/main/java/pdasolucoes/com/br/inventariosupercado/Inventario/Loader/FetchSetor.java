package pdasolucoes.com.br.inventariosupercado.Inventario.Loader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import org.ksoap2.serialization.SoapObject;

import java.sql.DatabaseMetaData;
import java.util.List;

import pdasolucoes.com.br.inventariosupercado.Dao.DataBase;
import pdasolucoes.com.br.inventariosupercado.Dao.SetorDao;
import pdasolucoes.com.br.inventariosupercado.Model.Setor;
import pdasolucoes.com.br.inventariosupercado.R;
import pdasolucoes.com.br.inventariosupercado.Services.InventarioService;
import pdasolucoes.com.br.inventariosupercado.Util.Metodo;

public class FetchSetor {

    private Context context;
    private Bundle bundle;
    private GetSetor getSetor;

    public interface GetSetor{
        void getSetor(List<Setor> setorList);
    }

    public void setOnSetor(GetSetor getSetor){
        this.getSetor = getSetor;
    }

    public FetchSetor(Context context, Bundle bundle) {
        this.context = context;
        this.bundle = bundle;
    }

    public void startLoadSetor() {
        new AsyncGetSetor().execute();
    }

    private class AsyncGetSetor extends AsyncTask<Object, Integer, Object> {

        ProgressDialog progressDialog;
        DataBase dataBase;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Metodo.progressDialogImportacao(context, context.getString(R.string.importando_setor));
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object... objects) {

            try {

                dataBase = DataBase.getInstancia(context);

                SoapObject response =  new InventarioService(context).GetSetor(
                        new Setor(bundle.getInt(context.getString(R.string.bundle_id_inventario))));

                for (int i = 0; i < response.getPropertyCount(); i++) {

                    SoapObject item = (SoapObject) response.getProperty(i);

                    Setor s = new Setor(
                            Integer.parseInt(item.getPropertyAsString("IdInventario"))
                            , Integer.parseInt(item.getPropertyAsString("IdDepartamento"))
                            , Integer.parseInt(item.getPropertyAsString("IdSetor"))
                            , Integer.parseInt(item.getPropertyAsString("IdMetodoContagem"))
                            , Integer.parseInt(item.getPropertyAsString("IdMetodoAuditoria"))
                            , Integer.parseInt(item.getPropertyAsString("IdMetodoLeitura"))
                            , item.getPropertyAsString("Setor")
                            , item.getPropertyAsString("MetodoContagem")
                            , item.getPropertyAsString("MetodoAuditoria")
                            , item.getPropertyAsString("MetodoLeitura")
                            , Float.parseFloat(item.getPropertyAsString("Quantidade")));

                    publishProgress((int) (((float) i / response.getPropertyCount()) * 100));

                    dataBase.setorDao().inserir(s);

                }
            } catch (Exception e) {
                return e;
            }

            return dataBase.setorDao().listar();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(Integer.parseInt(values[0].toString()));
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();

                if (!(o instanceof Exception)){

                    getSetor.getSetor((List<Setor>) o);
                }else{
                    Metodo.popupMensgam(context,o.toString());
                }
            }
        }
    }
}
