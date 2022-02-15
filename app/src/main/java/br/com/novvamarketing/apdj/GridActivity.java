package br.com.novvamarketing.apdj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.novvamarketing.apdj.entidades.VideoItem;
import br.com.novvamarketing.apdj.ui.PlaylistAdapter;
import br.com.novvamarketing.apdj.utils.MyStorage;

public class GridActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle(getIntent().getStringExtra("titulo"));

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        ArrayList<VideoItem> listVideos = (ArrayList<VideoItem>) MyStorage.storage.get("list_videos");
        new PlaylistAdapter(this, listVideos, recyclerView);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return true;
    }
}
