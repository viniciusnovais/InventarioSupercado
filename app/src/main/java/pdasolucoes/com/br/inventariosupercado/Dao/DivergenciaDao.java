package pdasolucoes.com.br.inventariosupercado.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import pdasolucoes.com.br.inventariosupercado.Model.Divergencia;

@Dao
public interface DivergenciaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Divergencia divergencia);

    @Query("DELETE FROM PDA_TB_DIVERGENCIA")
    void deletar();
}
