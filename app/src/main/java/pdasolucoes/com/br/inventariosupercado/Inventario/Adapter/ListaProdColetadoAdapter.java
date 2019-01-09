package pdasolucoes.com.br.inventariosupercado.Inventario.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import pdasolucoes.com.br.inventariosupercado.Model.ColetaItem;
import pdasolucoes.com.br.inventariosupercado.Model.Produto;
import pdasolucoes.com.br.inventariosupercado.R;

public class ListaProdColetadoAdapter extends RecyclerView.Adapter<ListaProdColetadoAdapter.ViewHolder> {

    Context context;
    List<ColetaItem> lista;
    LayoutInflater layoutInflater;
    ItemClick itemClick;

    public interface ItemClick{
        void onClick(int position);
    }

    public void setOnClickListener(ItemClick itemClick){
        this.itemClick = itemClick;
    }

    public ListaProdColetadoAdapter(Context context, List<ColetaItem> lista) {
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

        ColetaItem p = lista.get(position);

        holder.tvDesc.setText(p.getDescSku());

        holder.tvEan.setText(p.getCodAutomacao());

        holder.tvSku.setText(p.getCodSku());

        holder.tvQtde.setText(String.format(Locale.getDefault(),"%.3f",p.getQtdeContagem()));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDesc;
        TextView tvEan;
        TextView tvSku;
        TextView tvQtde;

        ViewHolder(View itemView) {
            super(itemView);

            tvDesc = itemView.findViewById(R.id.tvDescricao);
            tvEan = itemView.findViewById(R.id.tvEan);
            tvSku = itemView.findViewById(R.id.tvSku);
            tvQtde = itemView.findViewById(R.id.tvQtd);
            tvQtde.setVisibility(View.VISIBLE);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClick.onClick(getAdapterPosition());
        }
    }
}
