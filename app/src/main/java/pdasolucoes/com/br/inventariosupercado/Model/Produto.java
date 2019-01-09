package pdasolucoes.com.br.inventariosupercado.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "PDA_TB_PRODUTO")
public class Produto {

    @PrimaryKey
    @NonNull
    private String codAutomacao;
    private String codSku;
    private String descSku;
    private float preco;
    private String pesavel;
    private String secao;
    private String subSecao;
    private String tipo;
    private String subTipo;

    @Ignore
    public Produto() {
    }

    public Produto(String codAutomacao, String codSku, String descSku
            , float preco, String pesavel, String secao, String subSecao, String tipo, String subTipo) {
        this.codAutomacao = codAutomacao;
        this.codSku = codSku;
        this.descSku = descSku;
        this.preco = preco;
        this.pesavel = pesavel;
        this.secao = secao;
        this.subSecao = subSecao;
        this.tipo = tipo;
        this.subTipo = subTipo;
    }

    public String getCodAutomacao() {
        return codAutomacao;
    }

    public void setCodAutomacao(String codAutomacao) {
        this.codAutomacao = codAutomacao;
    }

    public String getCodSku() {
        return codSku;
    }

    public void setCodSku(String codSku) {
        this.codSku = codSku;
    }

    public String getDescSku() {
        return descSku;
    }

    public void setDescSku(String descSku) {
        this.descSku = descSku;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public String getPesavel() {
        return pesavel;
    }

    public void setPesavel(String pesavel) {
        this.pesavel = pesavel;
    }

    public String getSecao() {
        return secao;
    }

    public void setSecao(String secao) {
        this.secao = secao;
    }

    public String getSubSecao() {
        return subSecao;
    }

    public void setSubSecao(String subSecao) {
        this.subSecao = subSecao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSubTipo() {
        return subTipo;
    }

    public void setSubTipo(String subTipo) {
        this.subTipo = subTipo;
    }
}
