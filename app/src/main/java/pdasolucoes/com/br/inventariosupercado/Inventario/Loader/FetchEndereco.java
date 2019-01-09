package pdasolucoes.com.br.inventariosupercado.Inventario.Loader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import org.ksoap2.serialization.SoapObject;

import java.util.List;

import pdasolucoes.com.br.inventariosupercado.Dao.DataBase;
import pdasolucoes.com.br.inventariosupercado.Model.Endereco;
import pdasolucoes.com.br.inventariosupercado.R;
import pdasolucoes.com.br.inventariosupercado.Services.InventarioService;
import pdasolucoes.com.br.inventariosupercado.Util.Metodo;

public class FetchEndereco {

    private Context context;
    private Bundle bundle;
    private GetEndereco getEndereco;

    public interface GetEndereco {
        void getEndereco(List<Endereco> enderecoList);
    }

    public void setOnEndereco(GetEndereco getEndereco) {
        this.getEndereco = getEndereco;
    }

    public FetchEndereco(Context context, Bundle bundle) {
        this.context = context;
        this.bundle = bundle;
    }

    public void startLoadEndereco() {
        new AsyncGetEndereco().execute();
    }

    private class AsyncGetEndereco extends AsyncTask<Object, Integer, Object> {

        ProgressDialog progressDialog;
        DataBase dataBase;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Metodo.progressDialogImportacao(context, context.getString(R.string.importando_enderecos));
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object... objects) {

            try {

                dataBase = DataBase.getInstancia(context);

                SoapObject response = new InventarioService(context).GetEndereco(
                        new Endereco(bundle.getInt(context.getString(R.string.bundle_id_inventario))));

                for (int i = 0; i < response.getPropertyCount(); i++) {

                    SoapObject item = (SoapObject) response.getProperty(i);

                    Endereco end = new Endereco(
                            Integer.parseInt(item.getPropertyAsString("IdInventario"))
                            , Integer.parseInt(item.getPropertyAsString("IdEndereco"))
                            , Integer.parseInt(item.getPropertyAsString("IdDepartamento"))
                            , Integer.parseInt(item.getPropertyAsString("IdSetor"))
                            , Integer.parseInt(item.getPropertyAsString("IdMetodoContagem"))
                            , Integer.parseInt(item.getPropertyAsString("IdMetodoAuditoria"))
                            , Integer.parseInt(item.getPropertyAsString("IdMetodoLeitura"))
                            , item.getPropertyAsString("Endereco")
                            , item.getPropertyAsString("Departamento")
                            , item.getPropertyAsString("Setor")
                            , item.getPropertyAsString("MetodoContagem")
                            , item.getPropertyAsString("MetodoAuditoria")
                            , Float.parseFloat(item.getPropertyAsString("Quantidade"))
                            , item.getPropertyAsString("DataHora")
                    );

                    publishProgress((int) (((float) i / response.getPropertyCount()) * 100));

                    //incluindo endereco
                    dataBase.enderecoDao().inserirEndereco(end);


                }
            } catch (Exception e) {
                return e;
            }

            return dataBase.enderecoDao().listarEndereco();
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
                    //interface
                    List<Endereco> endereco = (List<Endereco>) o;
                    getEndereco.getEndereco(endereco);
                } else {
                    Metodo.popupMensgam(context, o.toString());
                }
            }
        }
    }
}
