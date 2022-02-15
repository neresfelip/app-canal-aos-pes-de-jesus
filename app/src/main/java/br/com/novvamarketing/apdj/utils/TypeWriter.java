package br.com.novvamarketing.apdj.utils;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;


public class TypeWriter extends AppCompatTextView {

    private CharSequence texto;
    private int index;
    private long delay = 150;

    public TypeWriter(Context context) {
        super(context);
    }

    public TypeWriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Handler handler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(texto.subSequence(0, ++index));
            if(index < texto.length()) {
                handler.postDelayed(characterAdder, delay);
            }
        }
    };

    public void animateText(CharSequence texto) {
        this.texto = texto;
        this.index = 0;
        setText("");
        handler.removeCallbacks(characterAdder);
        handler.postDelayed(characterAdder, delay);
    }

    public void animateText(int resource) {
        animateText(getContext().getString(resource));
    }

    public void setCharacterDelay(long delay) {
        this.delay = delay;
    }

}
