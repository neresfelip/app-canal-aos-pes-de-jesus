package br.com.novvamarketing.apdj.entidades;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.Date;

public class VideoItem {

    private Drawable thumb;
    private String titulo;
    private String id;
    private Date data;
    private boolean assistido;

    public VideoItem(Drawable thumb, String titulo, String id, Date data) {
        this.thumb = thumb;
        this.titulo = titulo;
        this.id = id;
        this.data = data;
        this.assistido = false;
    }

    public Drawable getThumb() {
        return thumb;
    }

    public void setThumb(Drawable thumb) {
        this.thumb = thumb;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getId() {
        return id;
    }

    public Date getData() {
        return data;
    }

    public boolean isAssistido() {
        return assistido;
    }

    public void assistir(){
        assistido = true;
    }

}
