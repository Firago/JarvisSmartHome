package com.dfirago.jarvissmarthome.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dfirago.jarvissmarthome.R;
import com.dfirago.jarvissmarthome.web.model.NetworkInfo;
import com.dfirago.jarvissmarthome.web.model.NetworkSelectRequest;
import com.dfirago.jarvissmarthome.web.services.ConfigurationService;
import com.dfirago.jarvissmarthome.web.services.RestServiceFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkDetailsActivity extends BaseActivity {

    private static final String TAG = "NetworkDetailsActivity";

    private static final int CHOOSE_NETWORK_REQUEST_CODE = 1;

    private LinearLayout spinnerLayout;
    private EditText ssidEditText;
    private EditText passwordEditText;

    private ConfigurationService configurationService =
            RestServiceFactory.createService(ConfigurationService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_details);
        spinnerLayout = (LinearLayout) findViewById(R.id.network_details_spinner_layout);
        ssidEditText = (EditText) findViewById(R.id.ssid_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
    }

    public void chooseNetwork(View view) {
        Intent intent = new Intent(this, NetworksListActivity.class);
        startActivityForResult(intent, CHOOSE_NETWORK_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CHOOSE_NETWORK_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                NetworkInfo networkInfo = (NetworkInfo) data.getSerializableExtra("networkInfo");
                ssidEditText.setText(networkInfo.getSsid());
            }
        }
    }

    public void updateConfiguration(View view) {
        spinnerLayout.setVisibility(View.VISIBLE);
        NetworkSelectRequest request = new NetworkSelectRequest();
        request.setSsid(ssidEditText.getText().toString());
        request.setPassword(passwordEditText.getText().toString());
        Call<Void> responseCall = configurationService.selectNetwork(request);
        responseCall.enqueue(new SelectNetworkCallback());
    }

    private class SelectNetworkCallback implements Callback<Void> {

        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            Toast.makeText(getApplicationContext(),
                    "Configuration changed successfully", Toast.LENGTH_LONG).show();
            spinnerLayout.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            Log.e(TAG, "Failed to update device configuration", t);
            Toast.makeText(getApplicationContext(),
                    "Failed to update device configuration", Toast.LENGTH_LONG).show();
            spinnerLayout.setVisibility(View.GONE);
        }
    }
}
