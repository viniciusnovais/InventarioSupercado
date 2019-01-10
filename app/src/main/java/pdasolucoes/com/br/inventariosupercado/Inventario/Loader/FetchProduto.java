package pdasolucoes.com.br.inventariosupercado.Inventario.Loader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import org.apache.commons.codec.binary.Base64;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import pdasolucoes.com.br.inventariosupercado.Dao.DataBase;
import pdasolucoes.com.br.inventariosupercado.Dao.ProdutoDao;
import pdasolucoes.com.br.inventariosupercado.Model.Produto;
import pdasolucoes.com.br.inventariosupercado.R;
import pdasolucoes.com.br.inventariosupercado.Services.InventarioService;
import pdasolucoes.com.br.inventariosupercado.Util.Metodo;

public class FetchProduto {

    private Context context;
    private Bundle bundle;
    private ResultProduto resultProduto;
    private ResultDivergencia resultDivergencia;

    public interface ResultProduto {
        void onResult(int result);
    }

    public interface ResultDivergencia{
        void onResult(int result);
    }

    public void setOnResultDivergencia(ResultDivergencia resultDivergencia){
        this.resultDivergencia = resultDivergencia;
    }

    public void setOnResult(ResultProduto result) {
        this.resultProduto = result;
    }

    public FetchProduto(Context context, Bundle bundle) {
        this.context = context;
        this.bundle = bundle;
    }

    public void startLoadDivergencia(){

        new AsynGetDivergencia().execute();
    }

    public void startLoadFileProduto() {
        new AsyncFileNameProduto().execute();
    }

    private class AsyncFileNameProduto extends AsyncTask<Object, Integer, List<String>> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = Metodo.progressDialogImportacao(context, context.getString(R.string.importando_arquivos));
            progressDialog.show();
        }

        @Override
        protected List<String> doInBackground(Object... objects) {

            List<String> lista = new ArrayList<>();
            try {
                SoapObject response = new InventarioService(context).
                        GetFileProduto(bundle.getInt(context.getString(R.string.bundle_id_inventario)));

                for (int i = 0; i < response.getPropertyCount(); i++) {

                    SoapObject item = (SoapObject) response.getProperty(i);

                    String s;
                    s = item.getPropertyAsString("NomeArquivo").replaceAll("ZIP","TXT");
                    publishProgress((int) (((float) i / response.getPropertyCount()) * 100));
                    lista.add(s);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return lista;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(Integer.parseInt(values[0].toString()));
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();

                if (strings != null) {
                    bundle.putStringArrayList(context.getString(R.string.bundle_file_zip), (ArrayList<String>) strings);
                    starLoadProduto();
                }

            }
        }
    }

    private void starLoadProduto() {
        new AsyncGetProduto().execute();
    }


    private class AsyncGetProduto extends AsyncTask<Object, Integer, Object> {

        ProgressDialog progressDialog;
        Produto prod = new Produto();
        DataBase mDb;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = Metodo.progressDialogImportacao(context, context.getString(R.string.importando_produto));
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object... objects) {
            try {

                List<String> arquivos = bundle.getStringArrayList(context.getString(R.string.bundle_file_zip));

                for (String a : Objects.requireNonNull(arquivos)) {

                    //PEGANDO O BYTE DO SERVIÇO
                    byte[] bytes = new InventarioService(context).
                            GetProduto(a);
                    //DECODIFICANDO
                    byte[] decoded = Base64.decodeBase64(bytes);
                    //TRANSFORMANDO O BYTE EM STRING
                    String data = new String(decoded, "UTF-8");

                    Runnable changeMessage = () -> progressDialog.setMessage(context.getString(R.string.gravando_usuarios) + a);
                    ((Activity)context).runOnUiThread(changeMessage);

                    mDb = DataBase.getInstancia(context);

                    String[] produtos = data.split("\r\n");
                    int contador = 0;
                    for (String produto1 : produtos) {
                        String[] produto = produto1.split(";");
                        int cnt = 0;
                        while (cnt < produto.length) {

                            prod = new Produto(
                                    produto[1]//codAutomacao
                                    , produto[0]//SKU
                                    , produto[2]// Descrição SKU
                                    , Float.parseFloat(produto[3].replaceAll("[,]", "."))//Preço
                                    , produto[8]//Pesavel
                                    , produto[4]//seção
                                    , produto[5]//subseção
                                    , produto[6]//tipo
                                    , produto[7]);//subtipo

                            cnt++;
                        }
                        contador++;
                        mDb.produtoDao().inserir(prod);
                        publishProgress((int) (((float) contador / produtos.length) * 100));
                    }
                }

            } catch (Exception e) {
                return e;
            }

            return 1;
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

                    resultProduto.onResult(Integer.parseInt(o.toString()));
                } else {
                    resultProduto.onResult(0);
                    Metodo.toastMsg(context, o.toString());
                }
            }
        }
    }

    private class AsynGetDivergencia extends AsyncTask{

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
                return new InventarioService(context).GetDivergencia(
                        bundle.getInt(context.getString(R.string.bundle_id_inventario)));
            } catch (Exception e) {
                return e;
            }
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (progressDialog.isShowing()){
                progressDialog.dismiss();

                if (!(o instanceof Exception)){

                    if (Long.parseLong(o.toString()) > 0){
                        resultDivergencia.onResult(1);
                    }else{
                       resultDivergencia.onResult(0);
                    }

                }else{
                    Metodo.toastMsg(context,o.toString());
                }

            }
        }
    }


}
