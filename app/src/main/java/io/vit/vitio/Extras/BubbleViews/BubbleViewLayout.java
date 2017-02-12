package io.vit.vitio.Extras.BubbleViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Prince Bansal Local on 09-08-2016.
 */

public class BubbleViewLayout extends FrameLayout {

    private BubbleViewParent[] mBubbleViewParents;

    public BubbleViewLayout(Context context) {
        super(context);
    }

    public BubbleViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BubbleViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
