package pdasolucoes.com.br.inventariosupercado.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.v4.content.PermissionChecker;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

@Entity(tableName = "PDA_TB_SETOR")
public class Setor implements KvmSerializable {

    private int idInventario;
    private int idDepartamento;
    @PrimaryKey
    private int id;
    private int idMetodoContagem;
    private int idMetodoAuditoria;
    private int idMetodoLeitura;
    private String setor;
    private String metodoContagem;
    private String metodoAuditoria;
    private String metodoLeitura;
    private float quantidade;

    public Setor(int idInventario, int idDepartamento, int id, int idMetodoContagem, int idMetodoAuditoria
            , int idMetodoLeitura, String setor, String metodoContagem, String metodoAuditoria, String metodoLeitura, float quantidade) {
        this.idInventario = idInventario;
        this.idDepartamento = idDepartamento;
        this.id = id;
        this.idMetodoContagem = idMetodoContagem;
        this.idMetodoAuditoria = idMetodoAuditoria;
        this.idMetodoLeitura = idMetodoLeitura;
        this.setor = setor;
        this.metodoContagem = metodoContagem;
        this.metodoAuditoria = metodoAuditoria;
        this.metodoLeitura = metodoLeitura;
        this.quantidade = quantidade;
    }

    @Ignore
    public Setor() {
    }

    @Ignore
    public Setor(int idInventario) {
        this.idInventario = idInventario;
    }

    public int getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(int idInventario) {
        this.idInventario = idInventario;
    }

    public int getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(int idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getMetodoLeitura() {
        return metodoLeitura;
    }

    public void setMetodoLeitura(String metodoLeitura) {
        this.metodoLeitura = metodoLeitura;
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

        switch (i){
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


        switch (i){
            case 0:
                idInventario = Integer.parseInt(o.toString());
                break;
        }
    }

    @Ignore
    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {


        switch (i){
            case 0:
                propertyInfo.type = PropertyInfo.INTEGER_CLASS;
                propertyInfo.name = "IdInventario";
                break;
        }
    }
}
