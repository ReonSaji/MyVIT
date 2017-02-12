package io.vit.vitio.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import io.vit.vitio.Activities.CoursePageActivity;
import io.vit.vitio.R;

/**
 * Created by Prince Bansal Local on 31-07-2016.
 */

public class FileExplorerFragment extends DialogFragment {

    // Stores names of traversed directories
    ArrayList<String> str = new ArrayList<String>();

    // Check if the first level of the directory structure is the one showing
    private Boolean firstLvl = true;

    private static final String TAG = "F_PATH";

    private Item[] fileList;
    private File path = new File(Environment.getExternalStorageDirectory() + "/My VIT");
    private String chosenFile;
    private static final int DIALOG_LOAD_FILE = 1000;

    ListAdapter adapter;
    private boolean openDialog=false;

    private OnRefreshListener onRefreshListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadFileList();
    }


    private void loadFileList() {
        try {
            path.mkdirs();
        } catch (SecurityException e) {
            Log.e(TAG, "unable to write on the sd card ");
        }

        // Checks whether path exists
        if (path.exists()) {
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    // Filters based on whether the file is hidden or not
                    return (sel.isFile() || sel.isDirectory())
                            && !sel.isHidden();

                }
            };

            String[] fList = path.list(filter);
            fileList = new Item[fList.length];
            for (int i = 0; i < fList.length; i++) {
                fileList[i] = new Item(fList[i], R.drawable.ic_web_black_24dp);

                // Convert into file path
                File sel = new File(path, fList[i]);

                // Set drawables
                if (sel.isDirectory()) {
                    fileList[i].icon = R.drawable.ic_folder_black;
                    Log.d("DIRECTORY", fileList[i].file);
                } else {
                    Log.d("FILE", fileList[i].file);
                }
            }

            if (!firstLvl) {
                Item temp[] = new Item[fileList.length + 1];
                for (int i = 0; i < fileList.length; i++) {
                    temp[i + 1] = fileList[i];
                }
                temp[0] = new Item("Up", R.drawable.ic_arrow_upward_black_24dp);
                fileList = temp;
            }
        } else {
            Log.e(TAG, "path does not exist");
        }

        adapter = new ArrayAdapter<Item>(getActivity(),
                android.R.layout.select_dialog_item, android.R.id.text1,
                fileList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // creates view
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view
                        .findViewById(android.R.id.text1);

                // put the image on the text view
                textView.setCompoundDrawablesWithIntrinsicBounds(
                        fileList[position].icon, 0, 0, 0);

                // add margin between image and text (support various screen
                // densities)
                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                textView.setCompoundDrawablePadding(dp5);

                return view;
            }
        };

    }

    private class Item {
        public String file;
        public int icon;

        public Item(String file, Integer icon) {
            this.file = file;
            this.icon = icon;
        }

        @Override
        public String toString() {
            return file;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if(openDialog){
            this.show(getActivity().getFragmentManager(),"show");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (fileList == null) {
            Log.e(TAG, "No files loaded");
            dialog = builder.create();
            return dialog;
        }
        builder.setTitle("Choose your file");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chosenFile = fileList[which].file;
                File sel = new File(path + "/" + chosenFile);
                if (sel.isDirectory()) {
                    firstLvl = false;

                    // Adds chosen directory to list
                    str.add(chosenFile);
                    fileList = null;
                    path = new File(sel + "");

                    loadFileList();

                    onRefreshListener.onRefresh(true);
                    Log.d(TAG, path.getAbsolutePath());

                }

                // Checks if 'up' was clicked
                else if (chosenFile.equalsIgnoreCase("up") && !sel.exists()) {

                    // present directory removed from list
                    String s = str.remove(str.size() - 1);

                    // path modified to exclude present directory
                    path = new File(path.toString().substring(0,
                            path.toString().lastIndexOf(s)));
                    fileList = null;

                    // if there are no more directories in the list, then
                    // its the first level
                    if (str.isEmpty()) {
                        firstLvl = true;
                    }
                    loadFileList();
                    onRefreshListener.onRefresh(true);
                    Log.d(TAG, path.getAbsolutePath());

                }
                // File picked
                else {
                    CoursePageActivity.openFile(getActivity(),new File(path+"/"+chosenFile));
                }

            }
        });
        dialog = builder.show();
        return dialog;
    }

    public void setOnRefreshListener(OnRefreshListener listener){
        onRefreshListener=listener;
    }

    public interface OnRefreshListener{
        void onRefresh(boolean shouldShow);
    }

}
