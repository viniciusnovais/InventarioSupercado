package pdasolucoes.com.br.inventariosupercado.Data;

import android.provider.BaseColumns;

public class Contrato {


    public static final class InventarioEntry implements BaseColumns{

        public static final String TABLE_NAME = "PDA_TB_INVENTARIO";

        public static final String COLUMN_COD_FILIAL = "codFilial";
        public static final String COLUMN_FILIAL = "filial";
        public static final String COLUMN_AUTORIZACAO = "autorizacao";
        public static final String COLUMN_STATUS = "status";
        public static final String COLUMN_PESO_QTDE = "pesoQtde";
        public static final String COLUMN_TAM_SKU_DE = "tamSkuDe";
        public static final String COLUMN_TAM_SKU_ATE = "tamSkuAte";
        public static final String COLUMN_TAM_PRECO_DE = "tamPrecoDe";
        public static final String COLUMN_TAM_PRECO_ATE = "tamPrecoAte";
        public static final String COLUMN_DIGITO_INICIAL = "digitoInicial";
        public static final String COLUMN_TAM_ETIQUETA = "tamEtiqueta";
        public static final String COLUMN_CASAS_DECIMAIS = "casasDecimais";
        public static final String COLUMN_CASAS_INTEIRAS = "casasInteiras";
        public static final String COLUMN_TIPO = "tipo";

    }

    public static final class DepartamentoEntry implements BaseColumns{

        public static final String TABLE_NAME = "PDA_TB_DEPARTAMENTO";

        public static final String COLUMN_DEPARTAMENTO = "Departamento";
        public static final String COLUMN_ID_METODO_AUDITORIA= "IdMetodoAuditoria";
        public static final String COLUMN_ID_INVENTARIO = "IdInventario";
        public static final String COLUMN_ID_METODO_LEITURA= "IdMetodoLeitura";
        public static final String COLUMN_METODO_AUDITORIA = "MetodoAuditoria";
        public static final String COLUMN_METODO_CONTAGEM = "MetodoContagem";
        public static final String COLUMN_ID_METODO_CONTAGEM = "IdMetodoContagem";
        public static final String COLUMN_QUANTIDADE = "Quantidade";

    }

    public static final class SetorEntry implements BaseColumns{

        public static final String TABLE_NAME = "PDA_TB_SETOR";

        public static final String COLUMN_SETOR = "Setor";
        public static final String COLUMN_ID_DEPARTAMENTO ="IdDepartamento";
        public static final String COLUMN_ID_METODO_AUDITORIA= "IdMetodoAuditoria";
        public static final String COLUMN_ID_INVENTARIO = "IdInventario";
        public static final String COLUMN_ID_METODO_LEITURA= "IdMetodoLeitura";
        public static final String COLUMN_METODO_AUDITORIA = "MetodoAuditoria";
        public static final String COLUMN_METODO_CONTAGEM = "MetodoContagem";
        public static final String COLUMN_ID_METODO_CONTAGEM = "IdMetodoContagem";
        public static final String COLUMN_QUANTIDADE = "Quantidade";

    }


    public static final class EnderecoEntry implements BaseColumns{

        public static final String TABLE_NAME = "PDA_TB_ENDERECO";

        public static final String COLUMN_COD_ENDERECO = "Endereco";
        public static final String COLUMN_SETOR = "Setor";
        public static final String COLUMN_DEPARTAMENTO = "Departamento";
        public static final String COLUMN_ID_DEPARTAMENTO ="IdDepartamento";
        public static final String COLUMN_ID_SETOR ="IdSetor";
        public static final String COLUMN_ID_METODO_AUDITORIA= "IdMetodoAuditoria";
        public static final String COLUMN_ID_INVENTARIO = "IdInventario";
        public static final String COLUMN_ID_METODO_LEITURA= "IdMetodoLeitura";
        public static final String COLUMN_METODO_AUDITORIA = "MetodoAuditoria";
        public static final String COLUMN_METODO_CONTAGEM = "MetodoContagem";
        public static final String COLUMN_ID_METODO_CONTAGEM = "IdMetodoContagem";
        public static final String COLUMN_QUANTIDADE = "Quantidade";
        public static final String COLUMN_DATA_HORA = "DataHora";

    }

    public static final class ProdutoEntry implements BaseColumns{

        public static final String TABLE_NAME = "PDA_TB_SKU";

        public static final String COLUMN_DESC_SKU = "desc_sku";
        public static final String COLUMN_COD_AUTOMACAO = "cod_automacao";
        public static final String COLUMN_COD_SKU = "cod_sku";
        public static final String COLUMN_PRECO ="preco";
        public static final String COLUMN_SECAO ="secao";
        public static final String COLUMN_SUB_SECAO= "subsecao";
        public static final String COLUMN_TIPO = "tipo";
        public static final String COLUMN_SUB_TIPO= "subtipo";
        public static final String COLUMN_PESAVEL = "pesavel";

    }
}
