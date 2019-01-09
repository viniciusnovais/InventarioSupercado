package pdasolucoes.com.br.inventariosupercado.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import pdasolucoes.com.br.inventariosupercado.Model.ColetaItem;
import pdasolucoes.com.br.inventariosupercado.Util.Constante;

@Dao
public interface ColetaItemDao {

    @Insert
    void inserir(ColetaItem coletaItem);

    @Query("SELECT SUM(qtdeContagem) FROM PDA_TB_COLETA_ITEM WHERE idEndereco =:idEndereco" +
            " AND tipoAtividade =:tipoAtividade" +
            " AND export <> " + Constante.FLAG_EXPORT_PCPC_VERIFICACAO_ERRADA)
    int retornaQtdeTotalEnderecoAtividade(int idEndereco, int tipoAtividade);

    @Query("SELECT id,codSku,codAutomacao,SUM(qtdeContagem) qtdeContagem,idEndereco,dtHora,descSku,metodoContagem" +
            " ,metodoAuditoria,tipoAtividade,export,preco,idUsuario,flagAddSub,idInventario" +
            " FROM PDA_TB_COLETA_ITEM WHERE idEndereco = :idEndereco AND tipoAtividade =:tipoAtividade" +
            " GROUP BY codSku")
    LiveData<List<ColetaItem>> listarColetas(int idEndereco,int tipoAtividade);

    @Query("SELECT * FROM PDA_TB_COLETA_ITEM WHERE idEndereco =:idEndereco AND export = " + Constante.FLAG_EXPORT_PENDENTE)
    List<ColetaItem> listar(int idEndereco);

    @Query("SELECT * FROM PDA_TB_COLETA_ITEM WHERE export = " + Constante.FLAG_EXPORT_PENDENTE)
    List<ColetaItem> listarColetasPendentes();

    @Query("UPDATE PDA_TB_COLETA_ITEM SET export = " + Constante.FLAG_EXPORT_REALIZADO + " WHERE id =:id")
    void coletadaRealizada(int id);

    @Query("SELECT COUNT(countIdEndereco) FROM (SELECT COUNT(idEndereco) countIdEndereco FROM PDA_TB_COLETA_ITEM WHERE idInventario =:idInventario AND tipoAtividade =:tipoAtividade GROUP BY idEndereco)")
    int qtdeEnderecosContagem(int idInventario,int tipoAtividade);

    @Query("SELECT COUNT(countCodSku) FROM (SELECT COUNT(codSku) countCodSku FROM PDA_TB_COLETA_ITEM WHERE idEndereco=:idEndereco AND tipoAtividade =:tipoAtividade group by codSku)")
    int qtdeProdutosContadosPorEndereco(int idEndereco,int tipoAtividade);

    @Query("DELETE FROM PDA_TB_COLETA_ITEM WHERE codSku=:codSku")
    void deletar(String codSku);

    @Query("DELETE FROM PDA_TB_COLETA_ITEM WHERE export = "+Constante.FLAG_EXPORT_REALIZADO)
    void deletar();

    @Query("SELECT 1 FROM PDA_TB_COLETA_ITEM WHERE codSku = :codSku")
    int existeColeta(String codSku);

    @Query("SELECT COUNT(countCodSku) FROM (SELECT COUNT(codSku) countCodSku FROM PDA_TB_COLETA_ITEM WHERE IDINVENTARIO = :idInventario AND EXPORT = "+Constante.FLAG_EXPORT_PENDENTE+" GROUP BY codSku)")
    int enviosPendentes(int idInventario);

}
