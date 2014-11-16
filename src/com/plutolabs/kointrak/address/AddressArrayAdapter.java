package com.plutolabs.kointrak.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.plutolabs.kointrak.R;
import so.chain.entity.Network;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * @author ct.
 */
public class AddressArrayAdapter extends ArrayAdapter<AddressField> {

    private final int resource;
    private final Context context;
    private final ArrayList<AddressField> values;

    public AddressArrayAdapter(Context context, int resource, ArrayList<AddressField> values) {
        super(context, resource, values);
        this.context = context;
        this.values = values;
        this.resource = resource;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, parent, false);

        TextView addressView = (TextView) view.findViewById(R.id.address);
        addressView.setText(values.get(position).getAddress());
        addressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "clicked", Toast.LENGTH_LONG).show();
            }
        });
        addressView.setText(values.get(position).getAddress());

        TextView balanceView = (TextView) view.findViewById(R.id.balance);
        //balanceView.setText(String.valueOf(values.get(position).getBalance()));
        balanceView.setText(balanceFormatter(values.get(position).getBalance()));

        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageResource(findIcon(values.get(position).getIcon()));

        return view;
    }

    static DecimalFormat df = new DecimalFormat("#.########");
    private String balanceFormatter(double balance) {
        if(balance > 0.0) {
            return String.format("%6.2f", balance);
        } else if (balance == 0.0){
            return String.format("%1.2f", balance);
        } else {
            return df.format(balance);//String.format("%6.8f",balance);
        }
    }

    private int findIcon(Network network) {
        switch(network) {
            case BTC: return R.drawable.bitcoin;
            case DOGE: return R.drawable.dogecoin;
            case LTC: return R.drawable.litecoin;
        } return R.drawable.ic_launcher;
    }
}
