package pdasolucoes.com.br.inventariosupercado.Model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "PDA_TB_INVENTARIO")
public class Inventario {

    @PrimaryKey
    private int id;
    private int codigoFilial;
    private String filial;
    private int status;
    private String autorizacao;
    private int tipo;
    private int pesoQtde;
    private int tamanhoSkuDe;
    private int tamanhoSkuAte;
    private int tamanhoPrecoDe;
    private int tamanhoPrecoAte;
    private int digitoInicial;
    private int tamanhoEtiqueta;
    private int casasDecimais;
    private int casaInteiras;

    @Ignore
    public Inventario() {

    }

    public Inventario(int id, int codigoFilial, String filial, int status, String autorizacao, int tipo, int pesoQtde, int tamanhoSkuDe, int tamanhoSkuAte
            , int tamanhoPrecoDe, int tamanhoPrecoAte, int digitoInicial, int tamanhoEtiqueta, int casasDecimais, int casaInteiras) {
        this.id = id;
        this.codigoFilial = codigoFilial;
        this.filial = filial;
        this.status = status;
        this.autorizacao = autorizacao;
        this.tipo = tipo;
        this.pesoQtde = pesoQtde;
        this.tamanhoSkuDe = tamanhoSkuDe;
        this.tamanhoSkuAte = tamanhoSkuAte;
        this.tamanhoPrecoDe = tamanhoPrecoDe;
        this.tamanhoPrecoAte = tamanhoPrecoAte;
        this.digitoInicial = digitoInicial;
        this.tamanhoEtiqueta = tamanhoEtiqueta;
        this.casasDecimais = casasDecimais;
        this.casaInteiras = casaInteiras;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodigoFilial() {
        return codigoFilial;
    }

    public void setCodigoFilial(int codigoFilial) {
        this.codigoFilial = codigoFilial;
    }

    public String getFilial() {
        return filial;
    }

    public void setFilial(String filial) {
        this.filial = filial;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAutorizacao() {
        return autorizacao;
    }

    public void setAutorizacao(String autorizacao) {
        this.autorizacao = autorizacao;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getPesoQtde() {
        return pesoQtde;
    }

    public void setPesoQtde(int pesoQtde) {
        this.pesoQtde = pesoQtde;
    }

    public int getTamanhoSkuDe() {
        return tamanhoSkuDe;
    }

    public void setTamanhoSkuDe(int tamanhoSkuDe) {
        this.tamanhoSkuDe = tamanhoSkuDe;
    }

    public int getTamanhoSkuAte() {
        return tamanhoSkuAte;
    }

    public void setTamanhoSkuAte(int tamanhoSkuAte) {
        this.tamanhoSkuAte = tamanhoSkuAte;
    }

    public int getTamanhoPrecoDe() {
        return tamanhoPrecoDe;
    }

    public void setTamanhoPrecoDe(int tamanhoPrecoDe) {
        this.tamanhoPrecoDe = tamanhoPrecoDe;
    }

    public int getTamanhoPrecoAte() {
        return tamanhoPrecoAte;
    }

    public void setTamanhoPrecoAte(int tamanhoPrecoAte) {
        this.tamanhoPrecoAte = tamanhoPrecoAte;
    }

    public int getDigitoInicial() {
        return digitoInicial;
    }

    public void setDigitoInicial(int digitoInicial) {
        this.digitoInicial = digitoInicial;
    }

    public int getTamanhoEtiqueta() {
        return tamanhoEtiqueta;
    }

    public void setTamanhoEtiqueta(int tamanhoEtiqueta) {
        this.tamanhoEtiqueta = tamanhoEtiqueta;
    }

    public int getCasasDecimais() {
        return casasDecimais;
    }

    public void setCasasDecimais(int casasDecimais) {
        this.casasDecimais = casasDecimais;
    }

    public int getCasaInteiras() {
        return casaInteiras;
    }

    public void setCasaInteiras(int casaInteiras) {
        this.casaInteiras = casaInteiras;
    }
}
