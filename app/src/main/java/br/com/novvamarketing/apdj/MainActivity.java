package br.com.novvamarketing.apdj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import br.com.novvamarketing.apdj.entidades.Versiculo;
import br.com.novvamarketing.apdj.rede.Conexao;
import br.com.novvamarketing.apdj.rede.VerificarAtualização;
import br.com.novvamarketing.apdj.ui.PlaylistAdapter;
import br.com.novvamarketing.apdj.utils.Arquivo;
import br.com.novvamarketing.apdj.utils.DataUtil;
import br.com.novvamarketing.apdj.utils.MyStorage;

public class MainActivity extends AppCompatActivity {

    public static final String arquivo = "id_videos";

    private final String CHANNEL_ID = "personal_notifications";
    private final int NOTIFICATION_ID = 1;

    public static ArrayList<String> idVideos;

    private RecyclerView recyclerView1, recyclerView2;
    private PlaylistAdapter adapter1, adapter2;

    private View progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        idVideos = (ArrayList<String>) Arquivo.carregar(this, arquivo);
        if(idVideos == null) {
            idVideos = new ArrayList<>();
            Arquivo.salvar(this, idVideos, arquivo);
        }

        progress = findViewById(R.id.progress);

        recyclerView1 = findViewById(R.id.recycler_view);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter1 = new PlaylistAdapter(this,
                "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=PLkJSl5TMNYQZZw-dM_cXA3lmwl4ZiCcSi&key=AIzaSyBPU3NeLiD0Gnk6ifCh-dx_zb49awQGWqs",
                progress, recyclerView1);

        recyclerView2 = findViewById(R.id.recycler_view2);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter2 = new PlaylistAdapter(this,
                "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=50&playlistId=PLkJSl5TMNYQasNu9OPyWy7KtKtUxum4NL&key=AIzaSyBPU3NeLiD0Gnk6ifCh-dx_zb49awQGWqs",
                progress, recyclerView2);

        Versiculo versiculo = (Versiculo) Arquivo.carregar(this, "versiculo.apdj");
        Calendar hoje = Calendar.getInstance();

        if(versiculo != null && DataUtil.comparar(hoje, versiculo.getData()) == DataUtil.IGUAIS) {
            findViewById(R.id.versiculo_progress).setVisibility(View.GONE);
            carregarVersiculo(versiculo);
        } else {
            new Conexao("https://novvamarketing.com.br/apps/apdj/versiculo_do_dia.php", new Conexao.Atualizador() {
                @Override
                public void sucesso(String result) throws JSONException, ParseException {
                    JSONObject obj = new JSONObject(result);

                    Calendar data = new GregorianCalendar();
                    data.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(obj.getString("data")));

                    Versiculo versiculo = new Versiculo(obj.getString("texto"), obj.getString("referencia"), data);
                    Arquivo.salvar(MainActivity.this, versiculo, "versiculo.apdj");

                    carregarVersiculo(versiculo);
                }

                @Override
                public void erro(String msg) {
                    findViewById(R.id.versiculo_erro).setVisibility(View.VISIBLE);
                }

            }, findViewById(R.id.versiculo_progress)).execute();
        }

        new VerificarAtualização(this).execute();

        AdView adView = findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);

    }

    private void carregarVersiculo(Versiculo versiculo) {
        ((TextView) findViewById(R.id.versiculo_texto)).setText(versiculo.getTexto());
        ((TextView) findViewById(R.id.versiculo_referencia)).setText(versiculo.getReferencia());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_sobre:

                String versao = "Indisponível";
                try {
                    PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
                    versao = info.versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                View view = getLayoutInflater().inflate(R.layout.layout_sobre, null);
                ((TextView) view.findViewById(R.id.sobre_versao)).setText("Versão "+versao);
                ((TextView) view.findViewById(R.id.sobre_texto)).setText(Html.fromHtml("Um projeto do programa Aos Pés de Jesus que visa refletir a palavra de Deus.<br><i>Por Othoniel Martins</i><br><br>Aplicativo desenvolvido por <b>Novva Marketing Integrado</b><br>Programador: Felipe Neres<br>Idealizador: Paulo de Tarso Martins<br><br>Copyright© 1984–2020 | Todos os direitos reservados."));

                AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setView(view)
                        .setPositiveButton(R.string.entendi, null);
                builder.show();
                break;

        }
        return true;
    }

    public void recomendar(View view) {

        Intent it = new Intent(Intent.ACTION_SEND);
        it.setType("text/plain");
        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        it.putExtra(Intent.EXTRA_SUBJECT, "Recomendar Minuto de Educação Bíblica");
        it.putExtra(Intent.EXTRA_TEXT, "Baixe este aplicativo gratuitamente:\nhttps://play.google.com/store/apps/details?id=br.com.novvamarketing.apdj");
        startActivity(Intent.createChooser(it, "Compartilhar este aplicativo"));

    }

    public void clickSobre(View view) {

        String endereco;

        switch (view.getId()) {
            case R.id.sobre_bt_avaliar:
                endereco = "https://play.google.com/store/apps/details?id=br.com.novvamarketing.apdj";
                break;
            case R.id.ic_facebook:
                endereco = "https://www.facebook.com/aospesdejesus/";
                break;
            case R.id.ic_instagram:
                endereco = "https://www.instagram.com/programaaospesdejesus/";
                break;
            case R.id.sobre_bt_novva:
                endereco = "http://www.novvamarketing.com.br";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(endereco)));

    }

    private void displayNotification() {

        createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        Intent it = new Intent(this, MainActivity.class);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pit = PendingIntent.getActivity(this, 0, it, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setSmallIcon(R.drawable.ic_book)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Notificação de exemplo...")
                .setPriority(NotificationManagerCompat.IMPORTANCE_DEFAULT)
                .setContentIntent(pit)
                .setAutoCancel(true);

        NotificationManagerCompat nmc = NotificationManagerCompat.from(this);
        nmc.notify(NOTIFICATION_ID, builder.build());

    }

    private void createNotificationChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Personal Notifications";
            String description = "Inclui todas as notificações pessoais";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(channel);
        }

    }

    public void abrirPlaylist(View view) {

        Intent it = new Intent(this, GridActivity.class);

        switch (view.getId()) {

            case R.id.playlist_programas:
                it.putExtra("titulo", "Programas completos");
                MyStorage.storage.put("list_videos", adapter1.getListVideos());
                break;

            case R.id.playlist_minuto:
                it.putExtra("titulo", "Minuto de educação bíblica");
                MyStorage.storage.put("list_videos", adapter2.getListVideos());
                break;

        }

        startActivity(it);

    }
}
