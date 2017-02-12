package io.vit.vitio.Extras.BubbleViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * Created by Prince Bansal Local on 09-08-2016.
 */

public class BubbleViewParent extends ViewGroup {

    private BubbleView[] mBubbleViews;

    public BubbleViewParent(Context context) {
        super(context);
    }

    public BubbleViewParent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BubbleViewParent(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
