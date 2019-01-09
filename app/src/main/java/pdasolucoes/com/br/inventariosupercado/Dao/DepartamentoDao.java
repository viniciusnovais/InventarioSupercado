package pdasolucoes.com.br.inventariosupercado.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import pdasolucoes.com.br.inventariosupercado.Data.Contrato.*;
import pdasolucoes.com.br.inventariosupercado.Model.Departamento;

@Dao
public interface DepartamentoDao {


    @Insert
    void inserir(Departamento departamento);

    @Query("SELECT * FROM PDA_TB_DEPARTAMENTO")
    List<Departamento> listar();

    @Query("DELETE FROM PDA_TB_DEPARTAMENTO")
    void deletar();
}
