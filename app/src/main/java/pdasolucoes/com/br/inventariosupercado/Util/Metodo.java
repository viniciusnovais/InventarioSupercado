package pdasolucoes.com.br.inventariosupercado.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import android.widget.Toast;


import java.util.Objects;

import pdasolucoes.com.br.inventariosupercado.Inventario.Interface.ItemFoco;
import pdasolucoes.com.br.inventariosupercado.R;


/**
 * Created by PDA on 26/09/2017.
 */

public class Metodo {

    private static void fecharTeclado(Context context) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);

        View focusedView = ((Activity)context).getCurrentFocus();
        if (focusedView !=null){
            Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(focusedView.getWindowToken(),
                    0);
        }
    }

    public static void abrirTeclado(Context context, EditText editText) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) context.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        editText.requestFocus();
    }

    public static void toastCamposObrigatorios(Context context) {
        Toast.makeText(context, context.getString(R.string.campos_branco), Toast.LENGTH_SHORT).show();
    }

    public static void toastMsg(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void limparCampos(EditText[] editTexts) {
        for (EditText e : editTexts) {
            e.setText("");
        }
    }

    public static boolean verificaCamposNulos(EditText[] editTexts) {
        for (int i = 0; i < editTexts.length; i++) {
            if (editTexts[i].getText().toString().equals("")) {
                return false;
            }
        }
        return true;
    }

    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new
                ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setIndeterminate(true);
        dialog.setCancelable(true);
        dialog.show();
        dialog.setContentView(R.layout.load_transparente);

        return dialog;
    }

    public static ProgressDialog progressDialogCarregamento(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(context.getString(R.string.carregando));
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(false);

        return progressDialog;
    }

    public static void popupMensgam(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.msg));
        builder.setMessage(msg);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm).getActiveNetworkInfo() != null;
    }

    public static void focoEditText(Context context, final EditText editText) {
        editText.requestFocus();
        editText.setText("");
        editText.postDelayed(editText::requestFocusFromTouch, 200);
        fecharTeclado(context);

    }

    public static void focoEditTextComTeclado(Context context, final EditText editText) {
        editText.requestFocus();
        editText.setText("");
        editText.postDelayed(editText::requestFocusFromTouch, 200);
        abrirTeclado(context,editText);

    }

    public static void focoEditTextComTecladoSemApagar(Context context, final EditText editText) {
        editText.requestFocus();
        editText.postDelayed(editText::requestFocusFromTouch, 200);
        abrirTeclado(context,editText);

    }

    public static ProgressDialog progressDialogImportacao(Context context, String msg) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle(msg);
        progressDialog.setMessage("");
        progressDialog.setProgress(0);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setCancelable(false);

        return progressDialog;
    }

//    public static void popupImage(Context context) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(150, 150);
//        float density = context.getResources().getDisplayMetrics().density;
//        int paddingPixel = (int) (16 * density);
//        ImageView imageView = new ImageView(context);
//        imageView.setPadding(paddingPixel, paddingPixel, paddingPixel, paddingPixel);
//        imageView.setLayoutParams(params);
//        builder.setView(imageView);
//
//        Picasso.get().load(R.drawable.ic_no_photos).error(R.drawable.ic_no_photos).into(imageView);
//
//        builder.create().show();
//
//    }

    public static boolean verificaBuscaCodigo(Context context, Object event, int key, EditText editText) {

        if (key != KeyEvent.KEYCODE_ENTER) {

            if (((MotionEvent)event).getAction() == MotionEvent.ACTION_DOWN) {
                if ((((MotionEvent)event).getRawX() >= editText.getRight() - editText.getTotalPaddingRight())) {
                    if (!TextUtils.isEmpty(editText.getText().toString())) {
                        return true;
                    } else {
                        Metodo.toastCamposObrigatorios(context);
                    }
                }
            }
        } else {
            if (((KeyEvent)event).getAction() == MotionEvent.ACTION_DOWN) {
                if (!TextUtils.isEmpty(editText.getText().toString())) {
                    return true;
                } else {
                    Metodo.toastCamposObrigatorios(context);
                }
            }
        }

        return false;
    }

    public static void vibrator(Context context) {

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (v != null) {
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
            }
        } else {
            if (v != null) {
                v.vibrate(500);
            }
        }

    }

}
