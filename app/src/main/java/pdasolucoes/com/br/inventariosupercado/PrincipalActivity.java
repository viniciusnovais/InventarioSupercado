package pdasolucoes.com.br.inventariosupercado;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import pdasolucoes.com.br.inventariosupercado.Util.Metodo;

public abstract class PrincipalActivity extends AppCompatActivity {

    private LinearLayout activityContainer;
    private View viewHeader;
    private ImageView imageView;

    @Override
    public void setContentView(int layoutResID) {
        LinearLayout llparentView = (LinearLayout) getLayoutInflater().inflate(R.layout.principal_activity, null);
        initViews(llparentView);
        View view = getLayoutInflater().inflate(layoutResID, activityContainer, true);
        imageView = view.findViewById(R.id.imageCliente);

        super.setContentView(llparentView);
    }

    private void initViews(View view) {

        SharedPreferences preferences = getSharedPreferences(getString(R.string.preference_login), MODE_PRIVATE);
        SharedPreferences preferencesConfig = getSharedPreferences(getString(R.string.pref_configuracoes), MODE_PRIVATE);

        LinearLayout linearLayout = view.findViewById(R.id.parent_activity);
        Activity context = (Activity) linearLayout.getContext();

        TextView tvModulo = view.findViewById(R.id.tvModulo);
        TextView tvUsuario = view.findViewById(R.id.tvUsuario);
        TextView tvPerfil = view.findViewById(R.id.tvPerfil);
        viewHeader = view.findViewById(R.id.header);
        activityContainer = view.findViewById(R.id.linearLayoutContent);

        Bundle b = getIntent().getBundleExtra("bundle");

        if (context.getTitle().toString().equals(getString(R.string.autorizacao))){
            if (b != null) {

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(getString(R.string.preference_desc_perfil), b.getString("descPerfil"));
                editor.putString(getString(R.string.preference_nome_usuario), b.getString("nome"));
                editor.putInt(getString(R.string.preference_cod_usuario), b.getInt("codUsuario"));
                editor.putInt(getString(R.string.preference_cod_perfil), b.getInt("codPerfil"));
                editor.putString(getString(R.string.preference_nome_filial), b.getString("nomeFilial"));
                editor.putString(getString(R.string.preference_cod_filial), b.getString("codFilial"));

                editor.apply();

                SharedPreferences.Editor editorConfig = preferencesConfig.edit();
                editorConfig.putString(getString(R.string.pref_servidor), b.getString("ipServidor"));
                editorConfig.putString(getString(R.string.pref_diretorio), b.getString("diretorioName"));

                editorConfig.apply();
            }else{
                finish();
            }
        }


        tvPerfil.setText(getString(R.string.perfil_dois_pontos).
                concat(" ").
                concat(preferences.getString(getString(R.string.preference_desc_perfil), "")));
        tvUsuario.setText(getString(R.string.usuario_dois_pontos).
                concat(" ").
                concat(preferences.getString(getString(R.string.preference_nome_usuario), "")));
        tvModulo.setText(getString(R.string.modulo_dois_pontos).
                concat(" ").
                concat(context.getTitle().toString()));

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewHeader.setVisibility(View.GONE);

            if (imageView != null) {
                imageView.setVisibility(View.GONE);
            }
            //viewFooter.setVisibility(View.GONE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            viewHeader.setVisibility(View.VISIBLE);
            if (imageView != null) {
                imageView.setVisibility(View.VISIBLE);
            }
            //viewFooter.setVisibility(View.VISIBLE);
        }
    }
}
