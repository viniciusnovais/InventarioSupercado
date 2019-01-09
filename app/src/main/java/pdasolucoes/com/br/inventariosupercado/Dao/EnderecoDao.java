package pdasolucoes.com.br.inventariosupercado.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import pdasolucoes.com.br.inventariosupercado.Model.Endereco;

@Dao
public interface EnderecoDao {


    @Insert
    void inserirEndereco(Endereco endereco);

    @Query("SELECT * FROM PDA_TB_ENDERECO")
    List<Endereco> listarEndereco();

    @Query("DELETE FROM PDA_TB_ENDERECO")
    void deletar();

    @Query("SELECT * FROM PDA_TB_ENDERECO WHERE endereco like :endereco")
    Endereco pesquisarEndereco(String endereco);

    @Query("SELECT COUNT(idEndereco) FROM PDA_TB_ENDERECO")
    int qtdeTotalEnderecos();
}
