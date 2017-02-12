package io.vit.vitio.myvitwear;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.vit.vitio.R;

public class Adapter extends WearableListView.Adapter {
    private List<Course> mDataset;
    private final Context mContext;
    private final LayoutInflater mInflater;

    public Adapter(Context context, List<Course> dataset) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDataset = dataset;
    }

    public static class ItemViewHolder extends WearableListView.ViewHolder {
        private TextView titleView;
        private ImageView typeIcon;
        public ItemViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.name);
            typeIcon=(ImageView)itemView.findViewById(R.id.type_icon);
        }
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {

        return new ItemViewHolder(mInflater.inflate(R.layout.list_item, null));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder,
                                 int position) {
        ItemViewHolder itemHolder = (ItemViewHolder) holder;

        itemHolder.titleView.setText(mDataset.get(position).getCOURSE_CODE());
        itemHolder.typeIcon.setImageResource(mDataset.get(position).getCOURSE_TYPE_SHORT().equals("L")?R.drawable.ic_lab:R.drawable.ic_theory);
        holder.itemView.setTag(position);
    }

    public int getItemCount() {
        return mDataset.size();
    }
}