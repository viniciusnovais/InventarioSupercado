package pdasolucoes.com.br.inventariosupercado.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.Set;

import pdasolucoes.com.br.inventariosupercado.Data.Contrato.*;
import pdasolucoes.com.br.inventariosupercado.Model.Departamento;
import pdasolucoes.com.br.inventariosupercado.Model.Setor;

@Dao
public interface SetorDao {


    @Insert
    void inserir(Setor setor);

    @Query("SELECT * FROM PDA_TB_SETOR")
    List<Setor> listar();

    @Query("DELETE FROM PDA_TB_SETOR")
    void deletar();

}
