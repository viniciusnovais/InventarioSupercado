package pdasolucoes.com.br.inventariosupercado.Util.Async;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import pdasolucoes.com.br.inventariosupercado.Model.Usuario;
import pdasolucoes.com.br.inventariosupercado.R;
import pdasolucoes.com.br.inventariosupercado.Services.AutenticacaoService;
import pdasolucoes.com.br.inventariosupercado.Util.Metodo;

public class AsyncTaskLogin {

    private Context context;
    private Bundle bundle;
    private ResultLogin result;

    public interface ResultLogin {
        void onResult(boolean result);
    }

    public void setResultLogin(ResultLogin result) {

        this.result = result;
    }

    public AsyncTaskLogin(Context context, Bundle bundle) {
        this.context = context;
        this.bundle = bundle;
    }

    public void startTask() {
        new Login().execute();
    }

    public class Login extends AsyncTask<Object,Integer,Object> {

        ProgressDialog progressDialog;
        Usuario usuario;
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.preference_login_file), Context.MODE_PRIVATE);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Metodo.progressDialogCarregamento(context);
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            try {
                usuario = new AutenticacaoService(context).Autenticacao(bundle.getString(context.getString(R.string.preference_login)), bundle.getString(context.getString(R.string.preference_senha)));
                usuario.setLogin(bundle.getString(context.getString(R.string.preference_login)));
                usuario.setSenha(bundle.getString(context.getString(R.string.preference_senha)));
            } catch (Exception e) {
                return e;
            }

            return usuario;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            if (progressDialog.isShowing()) {
                progressDialog.dismiss();

                if (!(o instanceof Exception)) {
                    if (usuario != null) {
                        if (usuario.getCodigo() != 0) {

                            SharedPreferences.Editor editor = preferences.edit();

                            editor.putString(context.getString(R.string.preference_login), usuario.getLogin());
                            editor.putString(context.getString(R.string.preference_senha), usuario.getSenha());
                            editor.putString(context.getString(R.string.preference_nome_usuario), usuario.getNome());
                            editor.putInt(context.getString(R.string.preference_cod_perfil), usuario.getCodigoPerfil());
                            editor.putInt(context.getString(R.string.preference_cod_usuario), usuario.getCodigo());
                            editor.putString(context.getString(R.string.preference_cod_filial), usuario.getCodigoFilial());
                            editor.putString(context.getString(R.string.preference_desc_perfil), usuario.getDescPerfil());
                            editor.putString(context.getString(R.string.preference_nome_filial), usuario.getNomeFilial());

                            editor.apply();
                            editor.commit();

                            result.onResult(true);

                        } else {
                            Metodo.toastMsg(context, context.getString(R.string.usuario_invalido));
                        }

                    }
                } else {
                    Metodo.popupMensgam(context, o.toString());
                }

            }
        }
    }
}
