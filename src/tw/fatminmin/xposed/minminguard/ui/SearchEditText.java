package tw.fatminmin.xposed.minminguard.ui;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

public class SearchEditText extends EditText{
    
    private Drawable cancel;
    private int fuzz = 10;
    
    public SearchEditText(Context context) {
        super(context);
        init();
    }
    
    public SearchEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
 
    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        
        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            this.clearFocus();
        }
        
        return super.onKeyPreIme(keyCode, event);
    }
    
    void init() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
 
                SearchEditText et = SearchEditText.this;
 
                if (et.getCompoundDrawables()[2] == null)
                    return false;
                 
                if (event.getAction() != MotionEvent.ACTION_UP)
                    return false;
                 
                cancel = et.getCompoundDrawables()[2];
                final int x = (int) event.getX();
                final int y = (int) event.getY();
                final Rect bounds = cancel.getBounds();
                
                if (x >= (v.getRight() - bounds.width() - fuzz) && x <= (v.getRight() - v.getPaddingRight() + fuzz)
                        && y >= (v.getPaddingTop() - fuzz) && y <= (v.getHeight() - v.getPaddingBottom()) + fuzz) {
                    
                    et.setText("");
                    
                }
                return false;
            }
        });
    }
    
}
