package com.dfirago.jarvissmarthome.adapters;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dfirago.jarvissmarthome.R;
import com.dfirago.jarvissmarthome.web.model.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmfi on 30/10/2016.
 */

public class NetworkInfoArrayAdapter extends ArrayAdapter<NetworkInfo> {

    private static final int SIGNAL_LEVELS = 5;

    private static final List<Integer> SIGNAL_LEVEL_DRAWABLES = new ArrayList<Integer>(SIGNAL_LEVELS) {{
        add(R.drawable.ic_signal_cellular_0_bar_white_36dp);
        add(R.drawable.ic_signal_cellular_1_bar_white_36dp);
        add(R.drawable.ic_signal_cellular_2_bar_white_36dp);
        add(R.drawable.ic_signal_cellular_3_bar_white_36dp);
        add(R.drawable.ic_signal_cellular_4_bar_white_36dp);
    }};

    // View lookup cache
    private static class ViewHolder {
        ImageView signal;
        TextView ssid;
        TextView encryption;
    }

    public NetworkInfoArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    public NetworkInfoArrayAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public NetworkInfoArrayAdapter(Context context, int resource, NetworkInfo[] objects) {
        super(context, resource, objects);
    }

    public NetworkInfoArrayAdapter(Context context, int resource, int textViewResourceId, NetworkInfo[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public NetworkInfoArrayAdapter(Context context, int resource, List<NetworkInfo> objects) {
        super(context, resource, objects);
    }

    public NetworkInfoArrayAdapter(Context context, int resource, int textViewResourceId, List<NetworkInfo> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        NetworkInfo networkInfo = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.networks_list_item, parent, false);
            viewHolder.signal = (ImageView) convertView.findViewById(R.id.item_network_info_signal);
            viewHolder.ssid = (TextView) convertView.findViewById(R.id.item_network_info_ssid);
            viewHolder.encryption = (TextView) convertView.findViewById(R.id.item_network_info_encryption);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        int signalLevel = WifiManager.calculateSignalLevel(networkInfo.getRssi(), SIGNAL_LEVELS);
        viewHolder.signal.setImageResource(SIGNAL_LEVEL_DRAWABLES.get(signalLevel));
        viewHolder.ssid.setText(networkInfo.getSsid());
        viewHolder.encryption.setText(networkInfo.getEncryption());
        // Return the completed view to render on screen
        return convertView;
    }
}
