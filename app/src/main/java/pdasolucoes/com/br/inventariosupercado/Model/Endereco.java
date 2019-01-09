package pdasolucoes.com.br.inventariosupercado.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;
import java.util.IllegalFormatCodePointException;

@Entity(tableName = "PDA_TB_ENDERECO")
public class Endereco implements KvmSerializable {

    private int idInventario;
    @PrimaryKey
    private int idEndereco;
    private int idDepartamento;
    private int idSetor;
    private int idMetodoContagem;
    private int idMetodoAuditoria;
    private int idMetodoLeitura;
    private String endereco;
    private String departamento;
    private String setor;
    private String metodoContagem;
    private String metodoAuditoria;
    private float quantidade;
    private String dataHora;


    public Endereco(int idInventario, int idEndereco, int idDepartamento, int idSetor, int idMetodoContagem
            , int idMetodoAuditoria, int idMetodoLeitura, String endereco, String departamento, String setor, String metodoContagem
            , String metodoAuditoria, float quantidade, String dataHora) {
        this.idInventario = idInventario;
        this.idEndereco = idEndereco;
        this.idDepartamento = idDepartamento;
        this.idSetor = idSetor;
        this.idMetodoContagem = idMetodoContagem;
        this.idMetodoAuditoria = idMetodoAuditoria;
        this.idMetodoLeitura = idMetodoLeitura;
        this.endereco = endereco;
        this.departamento = departamento;
        this.setor = setor;
        this.metodoContagem = metodoContagem;
        this.metodoAuditoria = metodoAuditoria;
        this.quantidade = quantidade;
        this.dataHora = dataHora;
    }

    @Ignore
    public Endereco() {
    }

    @Ignore
    public Endereco(int idInventario) {
        this.idInventario = idInventario;
    }

    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }

    public int getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(int idEndereco) {
        this.idEndereco = idEndereco;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public int getIdSetor() {
        return idSetor;
    }

    public void setIdSetor(int idSetor) {
        this.idSetor = idSetor;
    }

    public int getIdMetodoContagem() {
        return idMetodoContagem;
    }

    public void setIdMetodoContagem(int idMetodoContagem) {
        this.idMetodoContagem = idMetodoContagem;
    }

    public int getIdMetodoAuditoria() {
        return idMetodoAuditoria;
    }

    public void setIdMetodoAuditoria(int idMetodoAuditoria) {
        this.idMetodoAuditoria = idMetodoAuditoria;
    }

    public int getIdMetodoLeitura() {
        return idMetodoLeitura;
    }

    public void setIdMetodoLeitura(int idMetodoLeitura) {
        this.idMetodoLeitura = idMetodoLeitura;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getMetodoContagem() {
        return metodoContagem;
    }

    public void setMetodoContagem(String metodoContagem) {
        this.metodoContagem = metodoContagem;
    }

    public String getMetodoAuditoria() {
        return metodoAuditoria;
    }

    public void setMetodoAuditoria(String metodoAuditoria) {
        this.metodoAuditoria = metodoAuditoria;
    }

    public float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(float quantidade) {
        this.quantidade = quantidade;
    }


    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }


    @Ignore
    @Override
    public Object getProperty(int i) {

        if (i == 0)
            return idInventario;
        return null;
    }

    @Ignore
    @Override
    public int getPropertyCount() {
        return 1;
    }

    @Ignore
    @Override
    public void setProperty(int i, Object o) {

        if (i == 0)
            idInventario = Integer.parseInt(o.toString());
    }

    @Ignore
    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {


        if (i == 0) {
            propertyInfo.type = PropertyInfo.INTEGER_CLASS;
            propertyInfo.name = "IdInventario";
        }

    }
}
