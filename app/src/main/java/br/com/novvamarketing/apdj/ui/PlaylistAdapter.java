package br.com.novvamarketing.apdj.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.com.novvamarketing.apdj.MainActivity;
import br.com.novvamarketing.apdj.R;
import br.com.novvamarketing.apdj.entidades.VideoItem;
import br.com.novvamarketing.apdj.rede.Conexao;
import br.com.novvamarketing.apdj.rede.DownloadImagem;
import br.com.novvamarketing.apdj.utils.Arquivo;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.LineHolder> implements Conexao.Atualizador {

    private Context context;
    private RecyclerView recyclerView;

    private ArrayList<VideoItem> listVideos = new ArrayList<>();

    public PlaylistAdapter(Context context, ArrayList<VideoItem> listVideos, RecyclerView recyclerView) {
        this.context = context;
        this.listVideos = listVideos;
        this.recyclerView = recyclerView;
        recyclerView.setAdapter(this);
    }

    public PlaylistAdapter(Context context, String url, View progress, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
        recyclerView.setAdapter(this);
        new Conexao(url,this, progress).execute();
    }

    @NonNull
    @Override
    public LineHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {

        final View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = recyclerView.getChildLayoutPosition(view);

                VideoItem video = listVideos.get(position);

                String videoId = video.getId();

                final String url = "https://www.youtube.com/watch?v=" + videoId;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.putExtra("force_fullscreen",true);
                intent.putExtra("finish_on_ended", true);
                context.startActivity(intent);

                video.assistir();
                if(!MainActivity.idVideos.contains(videoId)) {
                    Log.i("teste", "entrou");
                    MainActivity.idVideos.add(videoId);
                    Arquivo.salvar(context, MainActivity.idVideos, MainActivity.arquivo);
                }
                notifyDataSetChanged();

            }

        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setItems(new String[]{"Compartilhar"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int position = recyclerView.getChildLayoutPosition(view);

                        String videoId = listVideos.get(position).getId();

                        final String texto = "Assista a esta pregação do pastor Othoniel Martins!\n\nhttps://www.youtube.com/watch?v=" + videoId;

                        Intent it = new Intent(Intent.ACTION_SEND);
                        it.setType("text/plain");
                        it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        it.putExtra(Intent.EXTRA_SUBJECT, "Enviar pregação");
                        it.putExtra(Intent.EXTRA_TEXT, texto);
                        context.startActivity(Intent.createChooser(it, "Compartilhar"));
                    }
                });
                builder.show();
                return true;
            }
        });

        return new LineHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.LineHolder holder, int position) {

        VideoItem video = listVideos.get(position);
        if(video.getThumb() != null) {
            holder.video_thumb.setImageDrawable(video.getThumb());
            holder.video_progress.setVisibility(View.GONE);
        }
        holder.video_titulo.setText(video.getTitulo());

        for(String id: MainActivity.idVideos) {
            if(id.equals(video.getId())) {
                Log.i("teste", video.getTitulo()+ " | "+ video.getId() +": assistido");
                holder.tagNovo.setVisibility(View.GONE);
                break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return listVideos.size();
    }

    @Override
    public void sucesso(String result) throws JSONException {

        JSONArray array = new JSONObject(result).getJSONArray("items");

        for (int i = 0; i < array.length(); i++) {

            try {
                JSONObject obj = array.getJSONObject(i);
                JSONObject snippet = obj.getJSONObject("snippet");
                JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                JSONObject def = thumbnails.getJSONObject("default");
                JSONObject resourceId = snippet.getJSONObject("resourceId");

                final String videoId = resourceId.getString("videoId");

                String strData = snippet.getString("publishedAt").substring(0, 10) + " "
                        + snippet.getString("publishedAt").substring(11, 19);

                Date data = null;
                try {
                    data = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strData);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                final VideoItem videoItem = new VideoItem(null, snippet.getString("title"), videoId, data);

                for (String id : MainActivity.idVideos) {

                    if (videoId.equals(id)) {
                        videoItem.assistir();
                        break;
                    }
                }

                listVideos.add(0, videoItem);

                new DownloadImagem(def.getString("url")) {

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {

                        if (bitmap != null) {
                            videoItem.setThumb(new BitmapDrawable(context.getResources(), bitmap));
                            notifyDataSetChanged();
                        }

                    }

                }.execute();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        notifyDataSetChanged();

    }

    @Override
    public void erro(String msg) {}

    public ArrayList<VideoItem> getListVideos() {
        return listVideos;
    }

    class LineHolder extends RecyclerView.ViewHolder {

        ProgressBar video_progress;
        TextView video_titulo;
        ImageView video_thumb;
        TextView tagNovo;

        LineHolder(@NonNull View itemView) {
            super(itemView);
            video_thumb = itemView.findViewById(R.id.video_thumb_image);
            video_titulo = itemView.findViewById(R.id.video_titulo);
            video_progress = itemView.findViewById(R.id.video_thumb_progress);
            tagNovo = itemView.findViewById(R.id.video_tag_novo);
        }

    }

}
