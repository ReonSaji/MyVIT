package io.vit.vitio.myvitwear;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.support.wearable.view.GridPagerAdapter;
import android.view.Gravity;

import java.util.List;

import io.vit.vitio.R;

public class SampleGridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    private List mRows;

    public SampleGridPagerAdapter(Context ctx, FragmentManager fm) {
        super(fm);
        mContext = ctx;
    }

    static final int[] BG_IMAGES = new int[]{
            R.color.grey,
            R.color.dark_grey
    };

    // A simple container for static data in each page
    private static class Page {
        // static resources


        public Page(String titleRes, String textRes, int iconRes) {
            this.titleRes = titleRes;
            this.textRes = textRes;
            this.iconRes = iconRes;
        }

        String titleRes;
        String textRes;
        int iconRes;

    }

    // Create a static set of pages in a 2D array
    private final Page[][] PAGES = {
            new Page[]{new Page("A1", "hellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohellohello", R.drawable.ic_copyright_black_24dp),
                    new Page("A2", "hello", R.drawable.ic_copyright_black_24dp),
                    new Page("A3", "hello", R.drawable.ic_copyright_black_24dp)},
            new Page[]{new Page("B1", "hello", R.drawable.ic_copyright_black_24dp),
                    new Page("B2", "hello", R.drawable.ic_copyright_black_24dp),
                    new Page("B3", "hello", R.drawable.ic_copyright_black_24dp)},
            new Page[]{new Page("C1", "hello", R.drawable.ic_copyright_black_24dp),
                    new Page("C2", "hello", R.drawable.ic_copyright_black_24dp),
                    new Page("C3", "hello", R.drawable.ic_copyright_black_24dp)}

    };

    // Obtain the UI fragment at the specified position
    @Override
    public Fragment getFragment(int row, int col) {
        Page page = PAGES[row][col];
        String title =
                page.titleRes;
        String text =
                page.textRes;
        CardFragment fragment = CardFragment.create(title, text, page.iconRes);

        // Advanced settings (card gravity, card expansion/scrolling)
        fragment.setCardGravity(Gravity.BOTTOM);
        fragment.setExpansionEnabled(true);
        fragment.setExpansionDirection(1);
        fragment.setExpansionFactor(10.0f);
        return fragment;
    }

    // Obtain the background image for the row
    @Override
    public Drawable getBackgroundForRow(int row) {
        return mContext.getResources().getDrawable(
                (BG_IMAGES[row % BG_IMAGES.length]), null);
    }


    // Obtain the background image for the specific page
    @Override
    public Drawable getBackgroundForPage(int row, int column) {
        if (row == 2 && column == 1) {
            // Place image at specified position
            return mContext.getResources().getDrawable(R.mipmap.ic_launcher, null);
        } else {
            // Default to background image for row
            return GridPagerAdapter.BACKGROUND_NONE;
        }
    }

    // Obtain the number of pages (vertical)
    @Override
    public int getRowCount() {
        return PAGES.length;
    }

    // Obtain the number of pages (horizontal)
    @Override
    public int getColumnCount(int rowNum) {
        return PAGES[rowNum].length;
    }
}
