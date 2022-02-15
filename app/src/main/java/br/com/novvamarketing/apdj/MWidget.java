package br.com.novvamarketing.apdj;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import br.com.novvamarketing.apdj.entidades.Versiculo;
import br.com.novvamarketing.apdj.rede.Conexao;
import br.com.novvamarketing.apdj.utils.Arquivo;

/**
 * Implementation of App Widget functionality.
 */
public class MWidget extends AppWidgetProvider {

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.mwidget);

        Log.i("teste", "abriu o widget");
        new Conexao("https://novvamarketing.com.br/apps/apdj/versiculo_do_dia.php", new Conexao.Atualizador() {

            @Override
            public void sucesso(String result) throws JSONException, ParseException {
                JSONObject obj = new JSONObject(result);

                String texto = obj.getString("texto"), referencia = obj.getString("referencia");
                Calendar data = new GregorianCalendar();
                data.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(obj.getString("data")));
                Arquivo.salvar(context, new Versiculo(texto, referencia, data), "versiculo.apdj");

                views.setTextViewText(R.id.versiculo_texto, texto);
                views.setTextViewText(R.id.versiculo_referencia, referencia);
                views.setViewVisibility(R.id.progress, View.GONE);
                appWidgetManager.updateAppWidget(appWidgetId, views);

            }

            @Override
            public void erro(String msg) {
                Log.e("teste", msg);
            }
        }, null).execute();
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

