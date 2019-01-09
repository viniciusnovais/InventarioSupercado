package pdasolucoes.com.br.inventariosupercado.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

@Entity(tableName = "PDA_TB_DEPARTAMENTO")
public class Departamento implements KvmSerializable {

    @PrimaryKey
    private int id;
    private int idInventario;
    private int idMetodoContagem;
    private int idMetodoAuditoria;
    private int idMetodoLeitura;
    private String departamento;
    private String metodoContagem;
    private String metodoAuditoria;
    private float quantidade;

    @Ignore
    public Departamento() {
    }

    public Departamento(int id, int idInventario, int idMetodoContagem, int idMetodoAuditoria
            , int idMetodoLeitura, String departamento, String metodoContagem, String metodoAuditoria, float quantidade) {
        this.id = id;
        this.idInventario = idInventario;
        this.idMetodoContagem = idMetodoContagem;
        this.idMetodoAuditoria = idMetodoAuditoria;
        this.idMetodoLeitura = idMetodoLeitura;
        this.departamento = departamento;
        this.metodoContagem = metodoContagem;
        this.metodoAuditoria = metodoAuditoria;
        this.quantidade = quantidade;
    }

    @Ignore
    public Departamento(int idInventario) {
        this.idInventario = idInventario;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
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

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
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

    @Ignore
    @Override
    public Object getProperty(int i) {

        switch (i) {

            case 0:
                return idInventario;
        }

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

        switch (i) {
            case 0:
                idInventario = Integer.parseInt(o.toString());
                break;
        }
    }

    @Ignore
    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {

        switch (i) {
            case 0:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "IdInventario";
                break;
        }
    }
}
