package com.dfirago.jarvissmarthome.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.dfirago.jarvissmarthome.R;
import com.dfirago.jarvissmarthome.adapters.NetworkInfoArrayAdapter;
import com.dfirago.jarvissmarthome.comparators.NetworkInfoRssiComparator;
import com.dfirago.jarvissmarthome.comparators.NetworkInfoSsidComparator;
import com.dfirago.jarvissmarthome.listeners.OnNetworkSelectedListener;
import com.dfirago.jarvissmarthome.web.model.NetworkInfo;
import com.dfirago.jarvissmarthome.web.model.NetworksResponse;
import com.dfirago.jarvissmarthome.web.services.ConfigurationService;
import com.dfirago.jarvissmarthome.web.services.RestServiceFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dmfi on 30/10/2016.
 */
public class NetworkSelectFragment extends Fragment {

    public static final String TAG = "NetworkSelectFragment";

    private ConfigurationService configurationService =
            RestServiceFactory.createService(ConfigurationService.class);

    private OnNetworkSelectedListener networkSelectedListener;
    private NetworkInfoArrayAdapter networkInfoArrayAdapter;

    private LinearLayout spinnerLayout;
    private ListView networkListView;
    private Button refreshButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_network_select, container, false);

        Fragment mainFragment = getFragmentManager().findFragmentByTag(MainFragment.TAG);
        networkSelectedListener = (OnNetworkSelectedListener) mainFragment;

        spinnerLayout = (LinearLayout) view.findViewById(R.id.network_select_spinner_layout);
        networkListView = (ListView) view.findViewById(R.id.network_list_view);
        networkInfoArrayAdapter = new NetworkInfoArrayAdapter(view.getContext(), R.layout.item_network_info);

        loadAvailableNetworks();

        networkListView.setAdapter(networkInfoArrayAdapter);
        networkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NetworkInfo itemValue = (NetworkInfo) networkListView.getItemAtPosition(position);
                networkSelectedListener.onNetworkSelected(itemValue);
            }
        });

        refreshButton = (Button) view.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loadAvailableNetworks();
            }
        });

        return view;
    }

    private void loadAvailableNetworks() {
        // show spinner before starting loading
        spinnerLayout.setVisibility(View.VISIBLE);
        Call<NetworksResponse> networksCall = configurationService.getNetworks();
        networksCall.enqueue(new LoadAvailableNetworksCallback());
    }

    private class LoadAvailableNetworksCallback implements Callback<NetworksResponse> {

        @Override
        public void onResponse(Call<NetworksResponse> call, Response<NetworksResponse> response) {
            networkInfoArrayAdapter.clear();
            List<NetworkInfo> networks = response.body().getNetworks();
            // create TreeSet in order remove networks with duplicated SSID (NetworkInfoSsidComparator)
            Set<NetworkInfo> networksUnique = new TreeSet<>(NetworkInfoSsidComparator.getInstance());
            networksUnique.addAll(networks);
            // create a List from the set and sort it (Set does not preserve order)
            List<NetworkInfo> networksUniqueSorted = new ArrayList<>(networksUnique);
            Collections.sort(networksUniqueSorted, NetworkInfoRssiComparator.getInstance());
            // put List of networks to the adapter which will create ListView
            networkInfoArrayAdapter.addAll(networksUniqueSorted);
            // hide spinner after loading is completed
            spinnerLayout.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(Call<NetworksResponse> call, Throwable t) {
            Log.e(TAG, "Failed to retrieve available networks", t);
            networkInfoArrayAdapter.clear();
            Toast.makeText(getActivity().getApplicationContext(),
                    "Failed to retrieve networks list from the device", Toast.LENGTH_LONG).show();
            // hide spinner after failure
            spinnerLayout.setVisibility(View.GONE);
        }
    }
}