package droidyue.com.addresspicker;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

class AddressItemViewHolder extends RecyclerView.ViewHolder {
    TextView mTitleView;

    AddressItemViewHolder(View itemView) {
        super(itemView);
        mTitleView = itemView.findViewById(R.id.itemTvTitle);
    }

}
