package pdasolucoes.com.br.inventariosupercado.Inventario.Loader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import org.ksoap2.serialization.SoapObject;

import java.util.List;

import pdasolucoes.com.br.inventariosupercado.Dao.DataBase;
import pdasolucoes.com.br.inventariosupercado.Dao.DepartamentoDao;
import pdasolucoes.com.br.inventariosupercado.Model.Departamento;
import pdasolucoes.com.br.inventariosupercado.R;
import pdasolucoes.com.br.inventariosupercado.Services.InventarioService;
import pdasolucoes.com.br.inventariosupercado.Util.Metodo;

public class FetchDepartamento {

    private Context context;
    private Bundle bundle;
    private GetDepartamento getDepartamento;

    public interface GetDepartamento {
        void getDepartamento(List<Departamento> departamentoList);
    }

    public void setOnDepartamento(GetDepartamento getDepartamento) {
        this.getDepartamento = getDepartamento;
    }

    public FetchDepartamento(Context context, Bundle bundle) {
        this.context = context;
        this.bundle = bundle;
    }

    public void startLoadDepartamento() {
        new AsyncGetDpto().execute();
    }

    private class AsyncGetDpto extends AsyncTask<Object, Integer, Object> {

        ProgressDialog progressDialog;
        DataBase dataBase;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Metodo.progressDialogImportacao(context, context.getString(R.string.importando_departamento));
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object... objects) {

            try {
                dataBase = DataBase.getInstancia(context);

                SoapObject response =  new InventarioService(context).GetDepartamento(
                        new Departamento(bundle.getInt(context.getString(R.string.bundle_id_inventario))));

                for (int i = 0; i < response.getPropertyCount(); i++) {

                    SoapObject item = (SoapObject) response.getProperty(i);

                    Departamento d = new Departamento(
                            Integer.parseInt(item.getPropertyAsString("IdDepartamento"))
                            , Integer.parseInt(item.getPropertyAsString("IdInventario"))
                            , Integer.parseInt(item.getPropertyAsString("IdMetodoContagem"))
                            , Integer.parseInt(item.getPropertyAsString("IdMetodoAuditoria"))
                            , Integer.parseInt(item.getPropertyAsString("IdMetodoLeitura"))
                            , item.getPropertyAsString("Departamento")
                            , item.getPropertyAsString("MetodoContagem")
                            , item.getPropertyAsString("MetodoAuditoria")
                            , Float.parseFloat(item.getPropertyAsString("Quantidade")));

                    publishProgress((int) (((float) i / response.getPropertyCount()) * 100));

                    dataBase.departamentoDao().inserir(d);
                }

            } catch (Exception e) {
                return e;
            }

            return dataBase.departamentoDao().listar();
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

                if (!(o instanceof Exception)) {

                    getDepartamento.getDepartamento((List<Departamento>) o);

                } else {
                    Metodo.popupMensgam(context, o.toString());
                }
            }
        }
    }
}
