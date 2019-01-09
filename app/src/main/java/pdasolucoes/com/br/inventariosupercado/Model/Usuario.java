package pdasolucoes.com.br.inventariosupercado.Model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

/**
 * Created by PDA_Vinicius on 06/02/2018.
 */

@Entity(tableName = "PDA_TB_USUARIO")
public class Usuario implements KvmSerializable {

    @PrimaryKey
    private int codigo;
    private byte[] senhaByte;
    private int lider;
    private String login;
    @Ignore
    private String nome;
    @Ignore
    private int codigoPerfil;
    @Ignore
    private String codigoFilial;
    @Ignore
    private String descPerfil;
    @Ignore
    private String nomeFilial;
    @Ignore
    private String senha;


    @Ignore
    public Usuario() {
    }

    public Usuario(int codigo, byte[] senhaByte, int lider, String login) {
        this.codigo = codigo;
        this.senhaByte = senhaByte;
        this.lider = lider;
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCodigoFilial() {
        return codigoFilial;
    }

    public void setCodigoFilial(String codigoFilial) {
        this.codigoFilial = codigoFilial;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCodigoPerfil() {
        return codigoPerfil;
    }

    public void setCodigoPerfil(int codigoPerfil) {
        this.codigoPerfil = codigoPerfil;
    }

    public String getDescPerfil() {
        return descPerfil;
    }

    public void setDescPerfil(String descPerfil) {
        this.descPerfil = descPerfil;
    }

    public String getNomeFilial() {
        return nomeFilial;
    }

    public void setNomeFilial(String nomeFilial) {
        this.nomeFilial = nomeFilial;
    }

    public byte[] getSenhaByte() {
        return senhaByte;
    }

    public void setSenhaByte(byte[] senhaByte) {
        this.senhaByte = senhaByte;
    }

    public int getLider() {
        return lider;
    }

    public void setLider(int lider) {
        this.lider = lider;
    }

    @Ignore
    @Override
    public Object getProperty(int i) {

        if (i == 0) {
            return codigo;
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

        if (i == 0) {

            codigo = Integer.parseInt(o.toString());
        }
    }


    @Ignore
    @Override
    public void getPropertyInfo(int i, Hashtable hashtable, PropertyInfo propertyInfo) {


        if (i == 0) {
            propertyInfo.type = PropertyInfo.INTEGER_CLASS;
            propertyInfo.name = "Codigo";
        }
    }
}
