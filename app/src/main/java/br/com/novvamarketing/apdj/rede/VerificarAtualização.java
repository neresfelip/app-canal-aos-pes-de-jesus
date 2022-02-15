package br.com.novvamarketing.apdj.rede;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class VerificarAtualização extends AsyncTask<Void, String, String> {

    private Context context;
    private String currentVersion;

    public VerificarAtualização(Context context) {
        this.context = context;
        try {
            this.currentVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override

    protected String doInBackground(Void... voids) {

        String newVersion = null;

        try {
            Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName())
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get();
            if (document != null) {
                Elements element = document.getElementsContainingOwnText("Current Version");
                for (Element ele : element) {
                    if (ele.siblingElements() != null) {
                        Elements sibElemets = ele.siblingElements();
                        for (Element sibElemet : sibElemets) {
                            newVersion = sibElemet.text();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newVersion;

    }


    @Override

    protected void onPostExecute(String onlineVersion) {

        super.onPostExecute(onlineVersion);

        if (onlineVersion != null && !onlineVersion.isEmpty()) {

            if (!currentVersion.equals(onlineVersion)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Há uma atualização disponível para este aplicativo na Play Store!\n\n" +
                        "É recomendável baixar esta nova atualização para obter melhorias, novas funções e evitar que ela possa deixar de funcionar devido à mudanças");
                builder.setPositiveButton("Baixar agora", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=br.com.novvamarketing.apdj")));
                    }
                });
                builder.setNegativeButton("Cancelar", null);
                builder.show();
            }

        }

    }
}