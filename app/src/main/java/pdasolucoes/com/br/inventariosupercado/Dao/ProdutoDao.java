package pdasolucoes.com.br.inventariosupercado.Dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import pdasolucoes.com.br.inventariosupercado.Data.Contrato.*;
import pdasolucoes.com.br.inventariosupercado.Model.Produto;
import pdasolucoes.com.br.inventariosupercado.Util.Constante;

@Dao
public interface ProdutoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserir(Produto produto);

    @Query("DELETE FROM PDA_TB_PRODUTO")
    void deletar();

    @Query("SELECT SECAO FROM PDA_TB_PRODUTO GROUP BY SECAO")
    List<String> listarSecao();

    @Query("SELECT P.SECAO FROM PDA_TB_PRODUTO P" +
            " INNER JOIN PDA_TB_DIVERGENCIA D ON d.sku = p.codSku GROUP BY SECAO")
    List<String> listarSecaoDiv();

    @Query("SELECT SUBSECAO FROM PDA_TB_PRODUTO WHERE TRIM(SECAO) LIKE TRIM(:secao) GROUP BY SUBSECAO")
    List<String> listarSubSecao(String secao);

    @Query("SELECT P.SUBSECAO FROM PDA_TB_PRODUTO P" +
            " INNER JOIN PDA_TB_DIVERGENCIA D ON d.sku = p.codSku" +
            " WHERE TRIM(SECAO) LIKE TRIM(:secao) GROUP BY SUBSECAO")
    List<String> listarSubSecaoDiv(String secao);

    @Query("SELECT TIPO FROM PDA_TB_PRODUTO WHERE TRIM(SUBSECAO) LIKE TRIM(:subSecao) GROUP BY TIPO")
    List<String> listarGrupo(String subSecao);

    @Query("SELECT P.TIPO FROM PDA_TB_PRODUTO P" +
            " INNER JOIN PDA_TB_DIVERGENCIA D ON d.sku = p.codSku" +
            " WHERE TRIM(SUBSECAO) LIKE TRIM(:subSecao) GROUP BY TIPO")
    List<String> listarGrupoDiv(String subSecao);

    @Query("SELECT SUBTIPO FROM PDA_TB_PRODUTO WHERE TRIM(TIPO) LIKE TRIM(:grupo) GROUP BY SUBTIPO")
    List<String> listarSubGrupo(String grupo);

    @Query("SELECT P.SUBTIPO FROM PDA_TB_PRODUTO P" +
            " INNER JOIN PDA_TB_DIVERGENCIA D ON d.sku = p.codSku" +
            " WHERE TRIM(TIPO) LIKE TRIM(:grupo) GROUP BY SUBTIPO")
    List<String> listarSubGrupoDiv(String grupo);

    @Query("SELECT * FROM PDA_TB_PRODUTO WHERE (TRIM(secao) = TRIM(:secao))" +
            " AND (TRIM(subSecao) = TRIM(:subSecao))" +
            " AND ((TRIM(tipo) = TRIM(:grupo)) OR (:grupo IS NULL))" +
            " AND ((TRIM(subTipo) = TRIM(:subGrupo)) OR (:subGrupo IS NULL))")
    List<Produto> listar(String secao,String subSecao,String grupo,String subGrupo);

    @Query("SELECT P.codSku, P.descSku,P.codAutomacao,P.preco,P.pesavel,P.secao,P.subSecao,P.tipo,P.subTipo FROM PDA_TB_PRODUTO P" +
            " INNER JOIN PDA_TB_DIVERGENCIA D ON d.sku = p.codSku WHERE (TRIM(secao) = TRIM(:secao))" +
            " AND (TRIM(subSecao) = TRIM(:subSecao))" +
            " AND ((TRIM(tipo) = TRIM(:grupo)) OR (:grupo IS NULL))" +
            " AND ((TRIM(subTipo) = TRIM(:subGrupo)) OR (:subGrupo IS NULL))")
    List<Produto> listarDiv(String secao,String subSecao,String grupo,String subGrupo);

    @Query("SELECT codSku, descSku,codAutomacao,preco,pesavel,secao,subSecao,tipo,subTipo" +
            " FROM PDA_TB_PRODUTO WHERE codSku NOT IN(SELECT codSku FROM PDA_TB_COLETA_ITEM " +
            " WHERE idEndereco=:idEndereco AND tipoAtividade=:tipoAtividade) " +
            " AND (TRIM(secao) = TRIM(:secao))" +
            " AND (TRIM(subSecao) = TRIM(:subSecao))" +
            " AND ((TRIM(tipo) = TRIM(:grupo)) OR (:grupo IS NULL))" +
            " AND ((TRIM(subTipo) = TRIM(:subGrupo)) OR (:subGrupo IS NULL)) GROUP BY codSku")
    List<Produto> listarPendente(int idEndereco, int tipoAtividade,String secao,String subSecao,String grupo,String subGrupo);

    @Query("SELECT P.codSku, P.descSku,P.codAutomacao,P.preco,P.pesavel,P.secao,P.subSecao,P.tipo,P.subTipo" +
            " FROM PDA_TB_PRODUTO P" +
            " INNER JOIN PDA_TB_DIVERGENCIA D ON d.sku = p.codSku WHERE codSku NOT IN(SELECT codSku FROM PDA_TB_COLETA_ITEM " +
            " WHERE idEndereco=:idEndereco AND tipoAtividade=:tipoAtividade) " +
            " AND (TRIM(secao) = TRIM(:secao))" +
            " AND (TRIM(subSecao) = TRIM(:subSecao))" +
            " AND ((TRIM(tipo) = TRIM(:grupo)) OR (:grupo IS NULL))" +
            " AND ((TRIM(subTipo) = TRIM(:subGrupo)) OR (:subGrupo IS NULL)) GROUP BY codSku")
    List<Produto> listarPendenteDiv(int idEndereco, int tipoAtividade,String secao,String subSecao,String grupo,String subGrupo);

    @Query("SELECT COUNT(countCodSku) FROM (SELECT COUNT(codSku) countCodSku FROM PDA_TB_PRODUTO" +
            " WHERE (TRIM(secao) = TRIM(:secao))" +
            " AND (TRIM(subSecao) = TRIM(:subSecao))" +
            " AND ((TRIM(tipo) = TRIM(:grupo)) OR (:grupo IS NULL))" +
            " AND ((TRIM(subTipo) = TRIM(:subGrupo)) OR (:subGrupo IS NULL)) GROUP BY codSku)")
    int qtdeTotalProdutos(String secao,String subSecao,String grupo,String subGrupo);

    @Query("SELECT COUNT(countCodSku) FROM (SELECT COUNT(p.codSku) countCodSku FROM PDA_TB_PRODUTO P" +
            " INNER JOIN PDA_TB_DIVERGENCIA D ON d.sku = p.codSku" +
            " WHERE (TRIM(secao) = TRIM(:secao))" +
            " AND (TRIM(subSecao) = TRIM(:subSecao))" +
            " AND ((TRIM(tipo) = TRIM(:grupo)) OR (:grupo IS NULL))" +
            " AND ((TRIM(subTipo) = TRIM(:subGrupo)) OR (:subGrupo IS NULL)) GROUP BY codSku)")
    int qtdeTotalProdutosDiv(String secao,String subSecao,String grupo,String subGrupo);
}
