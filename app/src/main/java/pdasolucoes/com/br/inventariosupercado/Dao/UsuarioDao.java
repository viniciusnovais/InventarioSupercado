package pdasolucoes.com.br.inventariosupercado.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import pdasolucoes.com.br.inventariosupercado.Model.Usuario;

@Dao
public interface UsuarioDao {


    @Insert
    void inserir(Usuario usuario);

    @Query("DELETE FROM PDA_TB_USUARIO")
    void deletar();
}
