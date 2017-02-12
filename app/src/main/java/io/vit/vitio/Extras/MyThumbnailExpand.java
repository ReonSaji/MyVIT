package io.vit.vitio.Extras;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;

import io.vit.vitio.R;

public class MyThumbnailExpand extends LinearLayout {
  public MyThumbnailExpand(Context context, String text) {
        super(context);
        init();
    }

  private void init(){
        inflate(getContext(), R.layout.blur_dialog, this);

  }
}