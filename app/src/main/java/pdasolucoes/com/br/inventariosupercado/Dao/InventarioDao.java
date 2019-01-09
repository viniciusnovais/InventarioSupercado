package pdasolucoes.com.br.inventariosupercado.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import pdasolucoes.com.br.inventariosupercado.Data.Contrato.*;
import pdasolucoes.com.br.inventariosupercado.Model.Inventario;

@Dao
public interface InventarioDao {


    @Insert
    void inserir(Inventario inventario);

    @Query("SELECT * FROM PDA_TB_INVENTARIO WHERE AUTORIZACAO = :autorizacao")
    Inventario getInventario(String autorizacao);

    @Query("SELECT * FROM PDA_TB_INVENTARIO")
    Inventario retornaDadosInventario();

    @Query("DELETE FROM PDA_TB_INVENTARIO")
    void deletar();

}
