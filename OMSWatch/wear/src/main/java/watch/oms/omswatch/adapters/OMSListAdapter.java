package watch.oms.omswatch.adapters;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import watch.oms.omswatch.OMSDTO.ListScreenItemsDTO;
import watch.oms.omswatch.R;

/**
 * Created by CHANDRASAIMOHAN on 10/20/2015.
 */
public class OMSListAdapter extends WearableListView.Adapter {

    private List<Integer> mIcons;
    private final LayoutInflater mInflater;
    private List<ListScreenItemsDTO> listData;
    Context ctx;

    public OMSListAdapter(Context ctx,List<ListScreenItemsDTO> listData){
        mInflater = LayoutInflater.from(ctx);
        this.mIcons = mIcons;
        this.listData = listData;
        this.ctx = ctx;
        Log.d("OMSLISTADAPTER", "IN OMSListAdapter(Context ctx,List<ListScreenItemsDTO> listData)");
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WearableListView.ViewHolder(new WearableListItemLayout(ctx));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
        Log.d("OMSLISTADAPTER", "onBindViewHolder" + listData.get(position).getPrimaryText());
        WearableListItemLayout listViewRowView = (WearableListItemLayout) holder.itemView;

        listViewRowView.setTag(listData.get(position));
        listViewRowView.getImage().setImageResource(R.drawable.ic_action_reminder);
        listViewRowView.getText().setText(listData.get(position).getPrimaryText());
        if(!TextUtils.isEmpty(listData.get(position).getSecondaryText())){
            listViewRowView.getSecondaryText().setText(listData.get(position).getSecondaryText());
        }else {
            listViewRowView.getSecondaryText().setText("");
        }
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

   /* private static class ItemViewHolder extends WearableListView.ViewHolder {
        private CircledImageView mCircledImageView;
        private TextView mItemTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            mCircledImageView = (CircledImageView)
                    itemView.findViewById(R.id.circle);
            mItemTextView = (TextView) itemView.findViewById(R.id.name);
        }
    }*/
}
