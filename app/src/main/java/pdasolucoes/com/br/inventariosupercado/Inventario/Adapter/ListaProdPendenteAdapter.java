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

public class ListaProdPendenteAdapter extends RecyclerView.Adapter<ListaProdPendenteAdapter.ViewHolder> {

    Context context;
    List<Produto> lista;
    LayoutInflater layoutInflater;
    ItemClick itemClick;

    public interface ItemClick{
        void onClick(int position);
    }

    public void setOnClickListener(ItemClick itemClick){
        this.itemClick = itemClick;
    }

    public ListaProdPendenteAdapter(Context context, List<Produto> lista) {
        this.context = context;
        this.lista = lista;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.adapter_list_prod_pend_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Produto p = lista.get(position);

        holder.tvDesc.setText(p.getDescSku());

        holder.tvEan.setText(p.getCodAutomacao());

        holder.tvSku.setText(p.getCodSku());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDesc;
        TextView tvEan;
        TextView tvSku;

        ViewHolder(View itemView) {
            super(itemView);

            tvDesc = itemView.findViewById(R.id.tvDescricao);
            tvEan = itemView.findViewById(R.id.tvEan);
            tvSku = itemView.findViewById(R.id.tvSku);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClick.onClick(getAdapterPosition());
        }
    }
}
