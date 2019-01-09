package pdasolucoes.com.br.inventariosupercado.Inventario.Fragment;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import pdasolucoes.com.br.inventariosupercado.Dao.DataBase;
import pdasolucoes.com.br.inventariosupercado.Inventario.Adapter.ListaProdColetadoAdapter;
import pdasolucoes.com.br.inventariosupercado.Inventario.ContagemAcitivity;
import pdasolucoes.com.br.inventariosupercado.Inventario.Interface.ItemFoco;
import pdasolucoes.com.br.inventariosupercado.Model.ColetaItem;
import pdasolucoes.com.br.inventariosupercado.Model.Produto;
import pdasolucoes.com.br.inventariosupercado.R;

public class ColetadoFragment extends Fragment {


    RecyclerView recyclerView;
    ListaProdColetadoAdapter adapter;
    Context context;
    DataBase mDb;
    SharedPreferences preferencesInv;
    ItemFoco itemFoco;

    public void setOnFocoFragment(ItemFoco itemFoco){
        this.itemFoco = itemFoco;
    }


    public static Fragment instanciar() {

        return new ColetadoFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getContext() != null)
            context = getContext();

       mDb = DataBase.getInstancia(context);
       preferencesInv = context.getSharedPreferences(context.getString(R.string.preference_inv),Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.inventario_pendente_fragment,container,false);

        recyclerView = v.findViewById(R.id.recyclerView);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        TextView tvQtdeTitulo = v.findViewById(R.id.tvQtdeTitulo);
        tvQtdeTitulo.setVisibility(View.VISIBLE);

        listarColetados();

        return v;
    }

    private void listarColetados(){

        final LiveData<List<ColetaItem>> item = mDb.coletaItemDao().listarColetas(
                preferencesInv.getInt(getString(R.string.preference_id_endereco),-1)
        ,preferencesInv.getInt(getString(R.string.preference_tipo_atividade),-1));
        item.observe(this, items -> {

            adapter = new ListaProdColetadoAdapter(context,items);
            recyclerView.setAdapter(adapter);
            adapter.setOnClickListener(position -> showFragment(items.get(position)));
        });
    }

    private void showFragment(ColetaItem c) {

        ContagemAcitivity.focoProduto(context);

        Objects.requireNonNull(Objects.requireNonNull(getActivity()).getSupportFragmentManager())
                .beginTransaction()
                .replace(R.id.frameFragment, ProdutoFragment.instanciar(c,getString(R.string.coletado)))
                .commit();
    }
}
