package br.com.novvamarketing.apdj.rede;

import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

import br.com.novvamarketing.apdj.MainActivity;
import br.com.novvamarketing.apdj.R;

public class Conexao extends AsyncTask {

    private String url;
    private final AtomicReference<View> progress = new AtomicReference<>();

    private Atualizador atualizador;

    public Conexao(String url, Atualizador atualizador, View progress) {
        this.url = url;
        this.progress.set(progress);
        this.atualizador = atualizador;
    }

    @Override
    protected void onPreExecute() {
        View view = progress.get();
        if(view != null)
            view.setVisibility(View.VISIBLE);
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        try {

            URL url = new URL(this.url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            Scanner s = new Scanner(connection.getInputStream());

            StringBuilder builder = new StringBuilder();
            while(s.hasNext()) {
                builder.append(s.nextLine());
            }

            return builder.toString();

        } catch (IOException e) {
            return e;
        }

    }

    @Override
    protected void onPostExecute(Object o) {

        if(o instanceof IOException) {
            atualizador.erro("Erro de rede");
        } else {

            try {
                atualizador.sucesso(o.toString());
            } catch (JSONException | ParseException e) {
                atualizador.erro(o.toString());
            }

        }

        View view = progress.get();
        if(view != null)
            view.setVisibility(View.GONE);
    }

    public interface Atualizador {
        public void sucesso(String result) throws JSONException, ParseException;
        public void erro(String msg);
    }

}
