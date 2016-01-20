package watch.oms.omswatch.adapters;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import watch.oms.omswatch.OMSDTO.ListScreenItemsDTO;
import watch.oms.omswatch.R;
import watch.oms.omswatch.interfaces.OMSListDetailListener;

/**
 * Created by CHANDRASAIMOHAN on 10/20/2015.
 */
public class OMSListAdapter extends WearableListView.Adapter {

    private List<Integer> mIcons;
    private final LayoutInflater mInflater;
    private List<ListScreenItemsDTO> listData;
    Context ctx;
    WearableListItemLayout listViewRowView;
    private  boolean showDetail;
    OMSListDetailListener omsListDetailListener;

    public OMSListAdapter(Context ctx,List<ListScreenItemsDTO> listData,boolean showDetail,OMSListDetailListener omsListDetailListener){
        mInflater = LayoutInflater.from(ctx);
        this.mIcons = mIcons;
        this.listData = listData;
        this.ctx = ctx;
        this.showDetail = showDetail;
        this.omsListDetailListener = omsListDetailListener;
        Log.d("OMSLISTADAPTER", "IN OMSListAdapter(Context ctx,List<ListScreenItemsDTO> listData)");
    }

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WearableListView.ViewHolder(new WearableListItemLayout(ctx));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder, final int position) {
        Log.d("OMSLISTADAPTER", "onBindViewHolder" + listData.get(position).getPrimaryText());
         listViewRowView = (WearableListItemLayout) holder.itemView;

        listViewRowView.setTag(listData.get(position));
        listViewRowView.getImage().setImageResource(R.drawable.ic_action_reminder);
        listViewRowView.getText().setText(listData.get(position).getPrimaryText());
        if(!TextUtils.isEmpty(listData.get(position).getSecondaryText())){
            listViewRowView.getSecondaryText().setText(listData.get(position).getSecondaryText());
        }else {
            listViewRowView.getSecondaryText().setText("");
        }


        if(listData.get(position).getTransUsid()!=null) {
            listViewRowView.getDetailText().setTag(listData.get(position).getTransUsid());
            if(!showDetail){
                listViewRowView.getDetailText().setVisibility(View.INVISIBLE);
            }
        }else{
            listViewRowView.getDetailText().setVisibility(View.INVISIBLE);
        }
        listViewRowView.getDetailText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView newLable = (TextView)v;
                String temp = (String)newLable.getTag();
             //   Toast.makeText(ctx, "USID:" +temp.getTransUsid(), Toast.LENGTH_LONG).show();
                Toast.makeText(ctx, "USID:" +temp+":::"+"position"+position, Toast.LENGTH_LONG).show();
                if(showDetail){
                    omsListDetailListener.receiveDetail(temp);
                }
            }
        });
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
