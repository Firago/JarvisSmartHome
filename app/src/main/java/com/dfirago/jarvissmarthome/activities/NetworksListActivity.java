package com.dfirago.jarvissmarthome.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.dfirago.jarvissmarthome.R;
import com.dfirago.jarvissmarthome.adapters.NetworkInfoArrayAdapter;
import com.dfirago.jarvissmarthome.comparators.NetworkInfoRssiComparator;
import com.dfirago.jarvissmarthome.comparators.NetworkInfoSsidComparator;
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

import static android.widget.AdapterView.OnItemClickListener;

/**
 * Created by dmfi on 05/01/2017.
 */

public class NetworksListActivity extends BaseActivity {

    private final static String TAG = "NetworkDetailsActivity";

    private ConfigurationService configurationService =
            RestServiceFactory.createService(ConfigurationService.class);

    private NetworkInfoArrayAdapter networkInfoArrayAdapter;

    private LinearLayout spinnerLayout;
    private ListView networkListView;
    private Button refreshButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networks_list);
        spinnerLayout = (LinearLayout) findViewById(R.id.network_select_spinner_layout);
        networkInfoArrayAdapter = new NetworkInfoArrayAdapter(getApplicationContext(),
                R.layout.item_networks_list);

        networkListView = (ListView) findViewById(R.id.network_list_view);
        networkListView.setAdapter(networkInfoArrayAdapter);
        networkListView.setOnItemClickListener(new NetworkListOnItemClickListener());

        refreshButton = (Button) findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAvailableNetworks();
            }
        });

        loadAvailableNetworks();
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
            Toast.makeText(getApplicationContext(),
                    "Failed to retrieve networks list from the device", Toast.LENGTH_LONG).show();
            // hide spinner after failure
            spinnerLayout.setVisibility(View.GONE);
        }
    }

    private class NetworkListOnItemClickListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            NetworkInfo itemValue = (NetworkInfo) networkListView.getItemAtPosition(position);
            Intent returnIntent = new Intent();
            returnIntent.putExtra("networkInfo", itemValue);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }
}
