package pdasolucoes.com.br.inventariosupercado.Inventario.Fragment;

import android.app.Activity;
import android.arch.persistence.room.Database;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pdasolucoes.com.br.inventariosupercado.Dao.DataBase;
import pdasolucoes.com.br.inventariosupercado.Inventario.Adapter.ListaProdPendenteAdapter;
import pdasolucoes.com.br.inventariosupercado.Inventario.ContagemAcitivity;
import pdasolucoes.com.br.inventariosupercado.Inventario.Interface.ItemFoco;
import pdasolucoes.com.br.inventariosupercado.Model.Produto;
import pdasolucoes.com.br.inventariosupercado.R;
import pdasolucoes.com.br.inventariosupercado.Util.AppExecutors;
import pdasolucoes.com.br.inventariosupercado.Util.Constante;

public class PendenteFragment extends Fragment {


    RecyclerView recyclerView;
    ListaProdPendenteAdapter adapter;
    Context context;
    DataBase mDb;
    SharedPreferences preferencesInv;
    SharedPreferences preferencesFiltro;

    public static Fragment instanciar() {

        return new PendenteFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getContext() != null)
            context = getContext();

        preferencesFiltro = context.getSharedPreferences(getString(R.string.filtros), Context.MODE_PRIVATE);

        mDb = DataBase.getInstancia(context);
        preferencesInv = context.getSharedPreferences(getString(R.string.preference_inv), Context.MODE_PRIVATE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.inventario_pendente_fragment, container, false);

        recyclerView = v.findViewById(R.id.recyclerView);

        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        if (preferencesInv.getInt(getString(R.string.invEnderecoAberto), -1)
                == Constante.ENDERECO_ABERTO) {
            listarPendente();
        }

        return v;
    }

    private void listarPendente() {

        AppExecutors.getsInstance().diskIO().execute(() -> {

            String secao = preferencesFiltro.getString(getString(R.string.secao), "");
            String subSecao = preferencesFiltro.getString(getString(R.string.subsecao), "");
            String grupo = preferencesFiltro.getString(getString(R.string.grupo), null);
            String subGrupo = preferencesFiltro.getString(getString(R.string.subgrupo), null);

            List<Produto> lista;

            if (preferencesInv.getInt(getString(R.string.preference_tipo_atividade), -1)
                    == Constante.ATIVIDADE_DIVERGENCIA)
                lista = mDb.produtoDao().listarPendenteDiv(
                        preferencesInv.getInt(getString(R.string.preference_id_endereco), -1)
                        , preferencesInv.getInt(getString(R.string.preference_tipo_atividade), -1)
                        , secao
                        , subSecao
                        , grupo
                        , subGrupo);
            else
                lista = mDb.produtoDao().listarPendente(
                        preferencesInv.getInt(getString(R.string.preference_id_endereco), -1)
                        , preferencesInv.getInt(getString(R.string.preference_tipo_atividade), -1)
                        , secao
                        , subSecao
                        , grupo
                        , subGrupo);


            adapter = new ListaProdPendenteAdapter(context, lista);
            adapter.setOnClickListener(position -> {
                Produto p = lista.get(position);
                showFragment(p.getCodAutomacao());
            });
            ((Activity) context).runOnUiThread(() -> recyclerView.setAdapter(adapter));

        });
    }

    private void showFragment(String cod) {

        ContagemAcitivity.focoProduto(context);

        Objects.requireNonNull(Objects.requireNonNull(getActivity()).getSupportFragmentManager())
                .beginTransaction()
                .replace(R.id.frameFragment, ProdutoFragment.instanciar(cod))
                .commit();
    }
}
