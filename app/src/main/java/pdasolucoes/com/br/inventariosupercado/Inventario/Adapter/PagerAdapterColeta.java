package pdasolucoes.com.br.inventariosupercado.Inventario.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import pdasolucoes.com.br.inventariosupercado.Inventario.Fragment.PendenteFragment;
import pdasolucoes.com.br.inventariosupercado.Inventario.Fragment.ProdutoFragment;
import pdasolucoes.com.br.inventariosupercado.R;

public class PagerAdapterColeta extends FragmentStatePagerAdapter {

    private Context context;
    private String endCod;

    public PagerAdapterColeta(Context context, FragmentManager fm, String endCod) {
        super(fm);
        this.context = context;
        this.endCod = endCod;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return ProdutoFragment.instanciar(endCod);
            case 1:
                return PendenteFragment.instanciar();

        }

        return ProdutoFragment.instanciar(endCod);
    }

    @Override
    public int getCount() {
        return context.getResources().getStringArray(R.array.array_coleta).length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getStringArray(R.array.array_coleta)[position];
    }
}
