package br.com.novvamarketing.apdj.entidades;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Versiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Calendar data;
    private String texto;
    private String referencia;

    public Versiculo(String texto, String referencia, Calendar data) {
        this.data = data;
        this.texto = texto;
        this.referencia = referencia;
    }

    public String getTexto() {
        return texto;
    }

    public String getReferencia() {
        return referencia;
    }

    public Calendar getData() {
        return data;
    }

}
