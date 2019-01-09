package pdasolucoes.com.br.inventariosupercado.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "PDA_TB_COLETA_ITEM")
public class ColetaItem {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String codSku;
    private String codAutomacao;
    private float qtdeContagem;
    private int idEndereco;
    private Date dtHora;
    private String descSku;
    private int metodoContagem;
    private int metodoAuditoria;
    private int tipoAtividade;
    private int export;
    private float preco;
    private int idUsuario;
    private int flagAddSub;
    private int idInventario;

    public ColetaItem(String codSku, String codAutomacao
            , float qtdeContagem, int idEndereco, Date dtHora, String descSku, int metodoContagem, int metodoAuditoria
            , int tipoAtividade, int export, float preco, int idUsuario, int flagAddSub, int idInventario) {
        this.codSku = codSku;
        this.codAutomacao = codAutomacao;
        this.qtdeContagem = qtdeContagem;
        this.idEndereco = idEndereco;
        this.dtHora = dtHora;
        this.descSku = descSku;
        this.metodoContagem = metodoContagem;
        this.metodoAuditoria = metodoAuditoria;
        this.tipoAtividade = tipoAtividade;
        this.export = export;
        this.preco = preco;
        this.idUsuario = idUsuario;
        this.flagAddSub = flagAddSub;
        this.idInventario = idInventario;
    }

    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFlagAddSub() {
        return flagAddSub;
    }

    public void setFlagAddSub(int flagAddSub) {
        this.flagAddSub = flagAddSub;
    }

    public String getCodSku() {
        return codSku;
    }

    public void setCodSku(String codSku) {
        this.codSku = codSku;
    }

    public String getCodAutomacao() {
        return codAutomacao;
    }

    public void setCodAutomacao(String codAutomacao) {
        this.codAutomacao = codAutomacao;
    }

    public float getQtdeContagem() {
        return qtdeContagem;
    }

    public void setQtdeContagem(float qtdeContagem) {
        this.qtdeContagem = qtdeContagem;
    }

    public int getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(int idEndereco) {
        this.idEndereco = idEndereco;
    }

    public Date getDtHora() {
        return dtHora;
    }

    public void setDtHora(Date dtHora) {
        this.dtHora = dtHora;
    }

    public String getDescSku() {
        return descSku;
    }

    public void setDescSku(String descSku) {
        this.descSku = descSku;
    }

    public int getMetodoContagem() {
        return metodoContagem;
    }

    public void setMetodoContagem(int metodoContagem) {
        this.metodoContagem = metodoContagem;
    }

    public int getMetodoAuditoria() {
        return metodoAuditoria;
    }

    public void setMetodoAuditoria(int metodoAuditoria) {
        this.metodoAuditoria = metodoAuditoria;
    }

    public int getTipoAtividade() {
        return tipoAtividade;
    }

    public void setTipoAtividade(int tipoAtividade) {
        this.tipoAtividade = tipoAtividade;
    }

    public int getExport() {
        return export;
    }

    public void setExport(int export) {
        this.export = export;
    }

    public float getPreco() {
        return preco;
    }

    public void setPreco(float preco) {
        this.preco = preco;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
