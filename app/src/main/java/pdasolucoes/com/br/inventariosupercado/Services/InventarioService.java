package pdasolucoes.com.br.inventariosupercado.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.MarshalBase64;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pdasolucoes.com.br.inventariosupercado.Dao.DataBase;
import pdasolucoes.com.br.inventariosupercado.Model.Departamento;
import pdasolucoes.com.br.inventariosupercado.Model.Endereco;
import pdasolucoes.com.br.inventariosupercado.Model.Inventario;
import pdasolucoes.com.br.inventariosupercado.Model.Setor;
import pdasolucoes.com.br.inventariosupercado.R;

public class InventarioService {

    private static String URL;
    private static String NAMESPACE = "http://tempuri.org/";
    private static String METHOD_GET_INV = "GetInventario";
    private static String METHOD_UP_STATUS_INV = "inventarioStatusUpdate";
    private static String METHOD_IMPORT_ESTOQUE_INICIAL = "importEstoqueInicial";
    private static String METHOD_GET_DEPARTAMENTO = "GetDepartamento";
    private static String METHOD_GET_SETOR = "GetSetor";
    private static String METHOD_GET_ENDERECO = "GetEndereco";
    private static String METHOD_GET_FILE_PROD = "GetFilenameProduto";
    private static String METHOD_PRODUTO = "GetProduto";
    private static String METHOD_PUT_FILE = "PutFile";
    private static String METHOD_GET_INV_PENDENCIA = "GetPendenciasInventario";
    private static DataBase dataBase;

    public InventarioService(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(context.getString(R.string.pref_configuracoes), Context.MODE_PRIVATE);
        URL = "http://" + preferences.getString(context.getString(R.string.pref_servidor), context.getString(R.string.ip_servidor)) + "/" + preferences.getString(context.getString(R.string.pref_diretorio), context.getString(R.string.diretorio_name)) + "/" + "wsinventario.asmx";

        dataBase = DataBase.getInstancia(context);

    }

    public static Inventario GetInventario(String autorizacao) throws Exception {

        SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_GET_INV);

        soapObject.addProperty("autorizacao_", autorizacao);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE transportSE = new HttpTransportSE(URL);
        transportSE.call(NAMESPACE + METHOD_GET_INV, envelope);

        SoapObject response = (SoapObject) envelope.getResponse();


        Inventario i = new Inventario(
                Integer.parseInt(response.getPropertyAsString("IdInventario"))
                , Integer.parseInt(response.getPropertyAsString("CodigoFilial"))
                , response.getPropertyAsString("Filial")
                , Integer.parseInt(response.getPropertyAsString("Status"))
                , response.getPropertyAsString("Autorizacao")
                , Integer.parseInt(response.getPropertyAsString("Tipo"))
                , Integer.parseInt(response.getPropertyAsString("PesoQuantidade"))
                , Integer.parseInt(response.getPropertyAsString("TamanhoSkuDe"))
                , Integer.parseInt(response.getPropertyAsString("TamanhoSkuAte"))
                , Integer.parseInt(response.getPropertyAsString("TamanhoPrecoDe"))
                , Integer.parseInt(response.getPropertyAsString("TamanhoPrecoAte"))
                , Integer.parseInt(response.getPropertyAsString("DigitoInicial"))
                , Integer.parseInt(response.getPropertyAsString("TamanhoEtiqueta"))
                , Integer.parseInt(response.getPropertyAsString("CasasDecimais"))
                , Integer.parseInt(response.getPropertyAsString("CasasInteiras")));

        dataBase.inventarioDao().inserir(i);

        return dataBase.inventarioDao().getInventario(response.getPropertyAsString("Autorizacao"));
    }

    public static int UpdateStatusInv(int idInventario, int status) throws Exception {

        SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_UP_STATUS_INV);

        soapObject.addProperty("idInventario", idInventario);
        soapObject.addProperty("status", status);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE transportSE = new HttpTransportSE(URL);
        transportSE.call(NAMESPACE + METHOD_UP_STATUS_INV, envelope);

        SoapObject response = (SoapObject) envelope.bodyIn;

        return Integer.parseInt(response.getPropertyAsString("inventarioStatusUpdateResult"));

    }

    public static int ImportEstoqueInicial(int idInventario) throws Exception {

        SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_IMPORT_ESTOQUE_INICIAL);

        soapObject.addProperty("idInventario", idInventario);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE transportSE = new HttpTransportSE(URL);
        transportSE.call(NAMESPACE + METHOD_IMPORT_ESTOQUE_INICIAL, envelope);

        SoapObject response = (SoapObject) envelope.bodyIn;

        return Integer.parseInt(response.getPropertyAsString("importEstoqueInicialResult"));

    }

    public static SoapObject GetDepartamento(Departamento departamento) throws Exception {

        SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_GET_DEPARTAMENTO);

        soapObject.addProperty("entity", departamento);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        envelope.addMapping(NAMESPACE, "entity", new Departamento().getClass());

        HttpTransportSE transportSE = new HttpTransportSE(URL);
        transportSE.call(NAMESPACE + METHOD_GET_DEPARTAMENTO, envelope);

        return (SoapObject) envelope.getResponse();

    }

    public static SoapObject GetSetor(Setor setor) throws Exception {

        SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_GET_SETOR);

        soapObject.addProperty("entity", setor);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        envelope.addMapping(NAMESPACE, "entity", new Setor().getClass());

        HttpTransportSE transportSE = new HttpTransportSE(URL);
        transportSE.call(NAMESPACE + METHOD_GET_SETOR, envelope);


        return (SoapObject) envelope.getResponse();

    }

    public static SoapObject GetEndereco(Endereco endereco) throws Exception {

        SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_GET_ENDERECO);
        List<Endereco> lista = new ArrayList<>();

        soapObject.addProperty("entity", endereco);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        envelope.addMapping(NAMESPACE, "entity", new Endereco().getClass());

        HttpTransportSE transportSE = new HttpTransportSE(URL);
        transportSE.call(NAMESPACE + METHOD_GET_ENDERECO, envelope);

        return (SoapObject) envelope.getResponse();
    }

    public static SoapObject GetFileProduto(int codigoInventario) throws Exception {

        SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_GET_FILE_PROD);

        soapObject.addProperty("codigoInventario_", codigoInventario);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE transportSE = new HttpTransportSE(URL);
        transportSE.call(NAMESPACE + METHOD_GET_FILE_PROD, envelope);

        return (SoapObject) envelope.getResponse();

    }

    public static byte[] GetProduto(String file) throws Exception {
        byte[] newByte;
        SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_PRODUTO);

        soapObject.addProperty("filename", file);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);


        new MarshalBase64().register(envelope);

        HttpTransportSE transportSE = new HttpTransportSE(URL);
        transportSE.call(NAMESPACE + METHOD_PRODUTO, envelope);

        SoapObject response = (SoapObject) envelope.bodyIn;
        newByte = response.getPropertyAsString("GetProdutoResult").getBytes();

        return newByte;

    }

    public static String GetInventarioPendencia(int idInventario) throws Exception {
        SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_GET_INV_PENDENCIA);

        soapObject.addProperty("idInventario", idInventario);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.implicitTypes = true;
        envelope.dotNet = true;
        envelope.setOutputSoapObject(soapObject);

        HttpTransportSE transportSE = new HttpTransportSE(URL);
        transportSE.call(NAMESPACE + METHOD_GET_INV_PENDENCIA, envelope);

        SoapObject response = (SoapObject) envelope.getResponse();

        if (response.toString().equals("anyType{}"))
            return "OK";
        return ((SoapObject)response.getProperty(0)).getPropertyAsString("DESCRICAO");

    }

    public void PutFile(File file, String filename) throws Exception {

        SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_PUT_FILE);


        byte[] data = fileToBytes(file);

        soapObject.addProperty("buffer", data);
        soapObject.addProperty("filename", filename);

        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.dotNet = true;
        soapEnvelope.implicitTypes = true;
        soapEnvelope.setOutputSoapObject(soapObject);

        new MarshalBase64().register(soapEnvelope);

        HttpTransportSE transport = new HttpTransportSE(URL);
        transport.call(NAMESPACE + METHOD_PUT_FILE, soapEnvelope);

        File folder = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS) + "/backup/");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File fileDs = new File(folder, filename);
        copy(file, fileDs);
        if (file.exists()) {
            file.delete();
        }

    }

    byte[] fileToBytes(File file) {
        byte[] bytes = new byte[0];
        try (FileInputStream inputStream = new FileInputStream(file)) {
            bytes = new byte[inputStream.available()];
            //noinspection ResultOfMethodCallIgnored
            inputStream.read(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }
}
