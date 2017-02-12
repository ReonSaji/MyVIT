package io.vit.vitio.Extras.Blur;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.nantaphop.hovertouchview.HoverTouchAble;

import io.vit.vitio.Extras.Themes.MyTheme;
import io.vit.vitio.R;
import jp.wasabeef.blurry.Blurry;

/**
 * Created by nantaphop on 01-Jan-16.
 */
public class HoverTouchHelper {
    private static final String TAG = "HoverTouchHelper";

    public static void make(final FrameLayout root, final HoverTouchAble hoverTouchAble, final View beneath, Bundle data) {
        if (hoverTouchAble instanceof View) {
            MyTheme myTheme = new MyTheme(root.getContext());
            myTheme.refreshTheme();
            final View v = (View) hoverTouchAble.getHoverView();
            final View src = (View) hoverTouchAble;
            root.addView(v);
            v.setScaleX(0.3f);
            v.setScaleY(0.3f);
            root.removeView(v);
            final int duration = hoverTouchAble.getHoverAnimateDuration();

            TextView t1 = ((TextView) v.findViewById(R.id.code));
            t1.setText(data.getString("course_code", "N/A"));
            TextView t2 = ((TextView) v.findViewById(R.id.slot));
            t2.setText(data.getString("course_slot", "N/A"));
            TextView t3 = ((TextView) v.findViewById(R.id.faculty));
            t3.setText(data.getString("course_faculty", "N/A"));
            TextView t4 = ((TextView) v.findViewById(R.id.head));
            t4.setText(data.getString("head", "N/A"));
            TextView t5 = ((TextView) v.findViewById(R.id.file_name));
            t5.setText(data.getString("name", "N/A"));
            TextView t6 = ((TextView) v.findViewById(R.id.date));
            t6.setText(data.getString("date", "N/A"));
            TextView t7 = ((TextView) v.findViewById(R.id.day));
            t7.setText(data.getString("day", "N/A"));

            t1.setTypeface(myTheme.getMyThemeTypeface());
            t2.setTypeface(myTheme.getMyThemeTypeface());
            t3.setTypeface(myTheme.getMyThemeTypeface());
            t4.setTypeface(myTheme.getMyThemeTypeface());
            t5.setTypeface(myTheme.getMyThemeTypeface());
            t6.setTypeface(myTheme.getMyThemeTypeface());
            t7.setTypeface(myTheme.getMyThemeTypeface());

            final View hoverView = v;
            View sourceView = src;

            final Handler handler = new Handler();

            final Runnable mLongPressed = new Runnable() {
                public void run() {
                    Log.i(TAG, "onLongPress: ");
                    hoverTouchAble.onStartHover();
                    Blurry.with(root.getContext()).radius(8).sampling(2).capture(beneath).into(HoverTouchHelper.addBlurView(root));
                    if (hoverView.getParent() == null) {
                        root.addView(hoverView);
                    }
                    hoverView.animate()
                            .scaleX(1)
                            .scaleY(1)
                            .alpha(1)
                            .setListener(null)
                            .setDuration(duration)
                            .start();
                }
            };

            src.setOnTouchListener(new View.OnTouchListener() {


                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            handler.postDelayed(mLongPressed, 500);
                            return true;
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL:
                            handler.removeCallbacks(mLongPressed);
                            Log.i(TAG, "onTouch: UP");
                            HoverTouchHelper.removeBlurView(root, duration);
                            hoverView.animate().cancel();
                            hoverView.animate()
                                    .scaleX(0)
                                    .scaleY(0)
                                    .alpha(0)
                                    .setDuration(duration)
                                    .setListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animator) {
                                            root.removeView(hoverView);
                                            hoverTouchAble.onStopHover();
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animator) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animator) {

                                        }
                                    })
                                    .start();
                            break;
                        /*case MotionEvent.ACTION_MOVE:
                            Rect rect=new Rect();
                            hoverView.getDrawingRect(rect);
                            Log.i(TAG, "onTouch:rec:x:y "+rect.flattenToString()+":"+motionEvent.getX()+":"+motionEvent.getY());
                            if(!rect.contains((int)motionEvent.getX(),(int)motionEvent.getY())){
                                HoverTouchHelper.removeBlurView(root, duration);
                                hoverView.animate().cancel();
                                hoverView.animate()
                                        .scaleX(0)
                                        .scaleY(0)
                                        .alpha(0)
                                        .setDuration(duration)
                                        .setListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animator) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animator) {
                                                root.removeView(hoverView);
                                                hoverTouchAble.onStopHover();
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animator) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animator) {

                                            }
                                        })
                                        .start();
                            }*/
                        default:
                            break;
                    }
                    return true;
                }
            });


        } else {
            throw new IllegalArgumentException("HoverTouchAble must be a View");
        }
    }

    private static ImageView addBlurView(ViewGroup root) {
        ImageView blurView = (ImageView) root.findViewWithTag(TAG);
        if (blurView == null) {
            blurView = new ImageView(root.getContext());
            blurView.setTag(TAG);
            blurView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            root.addView(blurView);
        }
        return blurView;

    }


    private static void removeBlurView(final ViewGroup root, int duration) {
        final View blurView = root.findViewWithTag(TAG);
        if (blurView != null) {
            blurView.animate().alpha(0).setDuration(duration).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    root.removeView(blurView);

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).start();

        }
    }


}
