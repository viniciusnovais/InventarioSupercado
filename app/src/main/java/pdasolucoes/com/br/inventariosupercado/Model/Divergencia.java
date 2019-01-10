package pdasolucoes.com.br.inventariosupercado.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "PDA_TB_DIVERGENCIA")
public class Divergencia {

    @PrimaryKey
    @NonNull
    private String codAutomocao;
    private String sku;
    private int idInventario;

    public Divergencia(String codAutomocao, String sku, int idInventario) {
        this.codAutomocao = codAutomocao;
        this.sku = sku;
        this.idInventario = idInventario;
    }

    public String getCodAutomocao() {
        return codAutomocao;
    }

    public void setCodAutomocao(String codAutomocao) {
        this.codAutomocao = codAutomocao;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }
}
