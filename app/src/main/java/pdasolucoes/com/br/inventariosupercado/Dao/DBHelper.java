package pdasolucoes.com.br.inventariosupercado.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pdasolucoes.com.br.inventariosupercado.Data.Contrato.*;

public class DBHelper extends SQLiteOpenHelper {

    private final static String DATA_BASE = "varandas.db";
    private final static int VERSION = 5;


    DBHelper(Context context) {
        super(context, DATA_BASE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_INVENTARIO_TABLE = "CREATE TABLE " + InventarioEntry.TABLE_NAME + " (" +
                InventarioEntry._ID + " INTEGER," +
                InventarioEntry.COLUMN_AUTORIZACAO + " TEXT," +
                InventarioEntry.COLUMN_COD_FILIAL + " INTEGER," +
                InventarioEntry.COLUMN_FILIAL + " TEXT," +
                InventarioEntry.COLUMN_STATUS + " INTEGER," +
                InventarioEntry.COLUMN_PESO_QTDE + " INTEGER," +
                InventarioEntry.COLUMN_TAM_SKU_DE + " INTEGER," +
                InventarioEntry.COLUMN_TAM_SKU_ATE + " INTEGER," +
                InventarioEntry.COLUMN_TAM_PRECO_DE + " INTEGER," +
                InventarioEntry.COLUMN_TAM_PRECO_ATE + " INTEGER," +
                InventarioEntry.COLUMN_DIGITO_INICIAL + " INTEGER," +
                InventarioEntry.COLUMN_TAM_ETIQUETA + " INTEGER," +
                InventarioEntry.COLUMN_CASAS_DECIMAIS + " INTEGER," +
                InventarioEntry.COLUMN_CASAS_INTEIRAS + " INTEGER," +
                InventarioEntry.COLUMN_TIPO + " INTEGER" +
                ");";

        final String SQL_CREATE_DEPTO_TABLE = "CREATE TABLE " + DepartamentoEntry.TABLE_NAME + " (" +
                DepartamentoEntry._ID + " INTEGER," +
                DepartamentoEntry.COLUMN_DEPARTAMENTO + " TEXT," +
                DepartamentoEntry.COLUMN_ID_INVENTARIO + " INTEGER," +
                DepartamentoEntry.COLUMN_METODO_CONTAGEM + " TEXT," +
                DepartamentoEntry.COLUMN_ID_METODO_CONTAGEM + " INTEGER," +
                DepartamentoEntry.COLUMN_ID_METODO_AUDITORIA + " INTEGER," +
                DepartamentoEntry.COLUMN_METODO_AUDITORIA + " TEXT," +
                DepartamentoEntry.COLUMN_QUANTIDADE + " REAL," +
                DepartamentoEntry.COLUMN_ID_METODO_LEITURA + " INTEGER" +
                ");";

        final String SQL_CREATE_SETOR_TABLE = "CREATE TABLE " + SetorEntry.TABLE_NAME + " (" +
                SetorEntry._ID + " INTEGER," +
                SetorEntry.COLUMN_SETOR + " TEXT," +
                SetorEntry.COLUMN_ID_DEPARTAMENTO + " INTEGER," +
                SetorEntry.COLUMN_ID_INVENTARIO + " INTEGER," +
                SetorEntry.COLUMN_METODO_CONTAGEM + " TEXT," +
                SetorEntry.COLUMN_ID_METODO_CONTAGEM + " INTEGER," +
                SetorEntry.COLUMN_ID_METODO_AUDITORIA + " INTEGER," +
                SetorEntry.COLUMN_METODO_AUDITORIA + " TEXT," +
                SetorEntry.COLUMN_QUANTIDADE + " REAL," +
                SetorEntry.COLUMN_ID_METODO_LEITURA + " INTEGER" +
                ");";

        final String SQL_CREATE_ENDERECO_TABLE = "CREATE TABLE " + EnderecoEntry.TABLE_NAME + " (" +
                EnderecoEntry._ID + " INTEGER," +
                EnderecoEntry.COLUMN_COD_ENDERECO + " TEXT," +
                EnderecoEntry.COLUMN_SETOR + " TEXT," +
                EnderecoEntry.COLUMN_DEPARTAMENTO + " TEXT," +
                EnderecoEntry.COLUMN_ID_SETOR + " INTEGER," +
                EnderecoEntry.COLUMN_ID_DEPARTAMENTO + " INTEGER," +
                EnderecoEntry.COLUMN_ID_INVENTARIO + " INTEGER," +
                EnderecoEntry.COLUMN_METODO_CONTAGEM + " TEXT," +
                EnderecoEntry.COLUMN_ID_METODO_CONTAGEM + " INTEGER," +
                EnderecoEntry.COLUMN_ID_METODO_AUDITORIA + " INTEGER," +
                EnderecoEntry.COLUMN_METODO_AUDITORIA + " TEXT," +
                EnderecoEntry.COLUMN_QUANTIDADE + " REAL," +
                EnderecoEntry.COLUMN_ID_METODO_LEITURA + " INTEGER," +
                EnderecoEntry.COLUMN_DATA_HORA+" TEXT"+
                ");";

        final String SQL_CREATE_PRODUTO_TABLE = "CREATE TABLE "+ ProdutoEntry.TABLE_NAME+ " ("+
                ProdutoEntry.COLUMN_COD_AUTOMACAO+" TEXT,"+
                ProdutoEntry.COLUMN_COD_SKU+" TEXT,"+
                ProdutoEntry.COLUMN_DESC_SKU+" TEXT,"+
                ProdutoEntry.COLUMN_PESAVEL+" TEXT,"+
                ProdutoEntry.COLUMN_SECAO+" TEXT,"+
                ProdutoEntry.COLUMN_SUB_SECAO+" TEXT,"+
                ProdutoEntry.COLUMN_TIPO+" TEXT,"+
                ProdutoEntry.COLUMN_SUB_TIPO+" TEXT,"+
                ProdutoEntry.COLUMN_PRECO+" REAL"+
                " );";


        db.execSQL(SQL_CREATE_INVENTARIO_TABLE);
        db.execSQL(SQL_CREATE_DEPTO_TABLE);
        db.execSQL(SQL_CREATE_SETOR_TABLE);
        db.execSQL(SQL_CREATE_ENDERECO_TABLE);
        db.execSQL(SQL_CREATE_PRODUTO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + InventarioEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DepartamentoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + SetorEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EnderecoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ProdutoEntry.TABLE_NAME);
        onCreate(db);
    }
}
