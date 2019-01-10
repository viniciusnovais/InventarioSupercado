package pdasolucoes.com.br.inventariosupercado.Dao;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import pdasolucoes.com.br.inventariosupercado.Model.ColetaItem;
import pdasolucoes.com.br.inventariosupercado.Model.Departamento;
import pdasolucoes.com.br.inventariosupercado.Model.Divergencia;
import pdasolucoes.com.br.inventariosupercado.Model.Endereco;
import pdasolucoes.com.br.inventariosupercado.Model.Inventario;
import pdasolucoes.com.br.inventariosupercado.Model.Produto;
import pdasolucoes.com.br.inventariosupercado.Model.Setor;
import pdasolucoes.com.br.inventariosupercado.Model.Usuario;
import pdasolucoes.com.br.inventariosupercado.Util.DateTypeConverter;

@Database(entities = {
        Endereco.class
        , Inventario.class
        , Departamento.class
        , Setor.class
        , Produto.class
        , Usuario.class
        , ColetaItem.class
        , Divergencia.class}, version = 18, exportSchema = false)
@TypeConverters({DateTypeConverter.class})
public abstract class DataBase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DATA_BASE = "inventario.db";
    private static DataBase instancia;


    public static DataBase getInstancia(Context context) {

        if (instancia == null) {

            synchronized (LOCK) {
                instancia = Room.databaseBuilder(context.getApplicationContext()
                        , DataBase.class, DataBase.DATA_BASE)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }

        return instancia;
    }

    public abstract EnderecoDao enderecoDao();

    public abstract InventarioDao inventarioDao();

    public abstract DepartamentoDao departamentoDao();

    public abstract SetorDao setorDao();

    public abstract ProdutoDao produtoDao();

    public abstract UsuarioDao usuarioDao();

    public abstract ColetaItemDao coletaItemDao();

    public abstract DivergenciaDao divergenciaDao();
}
