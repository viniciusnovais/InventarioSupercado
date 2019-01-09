package pdasolucoes.com.br.inventariosupercado.Inventario.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pdasolucoes.com.br.inventariosupercado.Model.Produto;
import pdasolucoes.com.br.inventariosupercado.R;

public class ListaProdutoAdapter extends RecyclerView.Adapter<ListaProdutoAdapter.ViewHolder> {

    private Context context;
    private List<Produto> lista;
    private LayoutInflater layoutInflater;
    private ItemProduto itemProduto;

    public interface ItemProduto {
        void onProduto(Produto p);
    }

    public void setOnProduto(ItemProduto itemProduto) {
        this.itemProduto = itemProduto;
    }

    public ListaProdutoAdapter(Context context, List<Produto> lista) {
        this.context = context;
        this.lista = lista;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ListaProdutoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ViewHolder vh = new ViewHolder(layoutInflater.inflate(R.layout.adapter_list_produto_item, parent, false));

        vh.setIsRecyclable(false);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ListaProdutoAdapter.ViewHolder holder, int position) {

        Produto p = lista.get(position);

        holder.tvDescricao.setText(p.getDescSku());

        itemProduto.onProduto(p);


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvDescricao;

        ViewHolder(View itemView) {
            super(itemView);

            tvDescricao = itemView.findViewById(R.id.tvDescricao);
        }
    }
}
