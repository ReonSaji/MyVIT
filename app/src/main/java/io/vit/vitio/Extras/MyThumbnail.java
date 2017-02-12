package io.vit.vitio.Extras;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nantaphop.hovertouchview.HoverTouchAble;

public class MyThumbnail extends TextView implements HoverTouchAble {
    public MyThumbnail(Context context) {
        super(context);
    }

    public MyThumbnail(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyThumbnail(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public View getHoverView() {
        return new MyThumbnailExpand(getContext(), "Description Text For Photo");
    }

    @Override
    public int getHoverAnimateDuration() {
        return 300;
    }

    @Override
    public void onStartHover() {
        //Toast.makeText(getContext(), "Start Hover", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStopHover() {
        //Toast.makeText(getContext(), "Stop Hover", Toast.LENGTH_SHORT).show();
    }
}