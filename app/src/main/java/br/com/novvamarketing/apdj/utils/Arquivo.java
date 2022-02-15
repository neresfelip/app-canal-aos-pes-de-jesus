package br.com.novvamarketing.apdj.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Arquivo {

    public static void salvar(Context context, Object object, String nome) {

        try {
            FileOutputStream fos = new FileOutputStream(context.getFileStreamPath(nome));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.flush(); oos.close();
            fos.flush(); fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Object carregar(Context context, String nome) {

        try {

            FileInputStream fis = new FileInputStream(context.getFileStreamPath(nome));
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            fis.close();
            ois.close();

            return object;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            Log.i("teste", "não carregou");
        }

        return null;

    }

    public static boolean apagar(Context context, String nome) {

        File file = context.getFileStreamPath(nome);
        return file.delete();

    }

}
