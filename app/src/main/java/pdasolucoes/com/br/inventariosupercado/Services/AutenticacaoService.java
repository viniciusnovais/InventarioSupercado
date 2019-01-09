package pdasolucoes.com.br.inventariosupercado.Services;

import android.content.Context;
import android.content.SharedPreferences;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import pdasolucoes.com.br.inventariosupercado.Model.Usuario;
import pdasolucoes.com.br.inventariosupercado.R;

/**
 * Created by PDA_Vinicius on 06/02/2018.
 */

public class AutenticacaoService {

    private static String URL;
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_NAME_AUTENT = "GetAutenticacao";

    public AutenticacaoService(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_configuracoes), Context.MODE_PRIVATE);
        URL = "http://" + preferences.getString(context.getString(R.string.pref_servidor), "") + "/" + preferences.getString(context.getString(R.string.pref_diretorio), "") + "/" + "Autenticacao.asmx";

    }

    public static Usuario Autenticacao(String usuario, String senha) throws Exception {

        SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_NAME_AUTENT);
        Usuario u = new Usuario();

        soapObject.addProperty("login_", usuario);
        soapObject.addProperty("senha_", senha);


        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE transportSE = new HttpTransportSE(URL);
        transportSE.call(NAMESPACE + METHOD_NAME_AUTENT, envelope);

        SoapObject response = (SoapObject) envelope.getResponse();

        u.setCodigo(Integer.parseInt(response.getPropertyAsString("Codigo")));
        u.setCodigoPerfil(Integer.parseInt(response.getPropertyAsString("CodigoPerfil")));
        u.setNome(response.getPropertyAsString("Nome"));
        u.setCodigoFilial(response.getPropertyAsString("CodigoFilial"));
        u.setDescPerfil(response.getPropertyAsString("DescricaoPefil"));
        u.setNomeFilial(response.getPropertyAsString("NomeFilial"));

        return u;
    }
}
