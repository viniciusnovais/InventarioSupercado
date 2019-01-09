package pdasolucoes.com.br.inventariosupercado.Util;

import android.content.Context;
import android.net.ConnectivityManager;

import java.util.Objects;

/**
 * Created by PDA on 08/02/2017.
 */

public class VerificaConexao {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm).getActiveNetworkInfo() != null;
    }

}
