package br.com.novvamarketing.apdj.utils;

import java.util.Calendar;

public class DataUtil {

    public static final int REGREDINDO = -1, AVANCANDO = 1, IGUAIS = 0;

    public static int comparar(Calendar data1, Calendar data2) {

        int ano1 = data1.get(Calendar.YEAR), ano2 = data2.get(Calendar.YEAR);
        int dia1 = data1.get(Calendar.DAY_OF_YEAR), dia2 = data2.get(Calendar.DAY_OF_YEAR);

        if(ano1 < ano2) {
            return AVANCANDO;
        } else if(ano1 > ano2) {
            return REGREDINDO;
        } else if(dia1 < dia2) {
            return AVANCANDO;
        } else if(dia1 > dia2) {
            return REGREDINDO;
        }

        return IGUAIS;

    }

}
