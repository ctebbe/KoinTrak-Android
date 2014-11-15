package com.plutolabs.kointrak;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import so.chain.entity.Network;

/**
 * @author ct.
 */
public class AddressArrayAdapter  extends ArrayAdapter<AddressField> {

    private final int resource;
    private final Context context;
    private final AddressField[] values;

    public AddressArrayAdapter(Context context, int resource, AddressField[] values) {
        super(context, resource, values);
        this.context = context;
        this.values = values;
        this.resource = resource;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, parent, false);

        TextView addressView = (TextView) view.findViewById(R.id.address);
        addressView.setText(values[position].address.getAddress());
        TextView balanceView = (TextView) view.findViewById(R.id.balance);
        balanceView.setText((int) values[position].balance);

        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageResource(findIcon(values[position].icon));

        return view;
    }

    private int findIcon(Network network) {
        switch(network) {
            case BTC: return R.drawable.bitcoin;
            case DOGE: return R.drawable.dogecoin;
            case LTC: return R.drawable.litecoin;
        } return R.drawable.ic_launcher;
    }
}
