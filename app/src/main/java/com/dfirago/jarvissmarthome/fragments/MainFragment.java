package com.dfirago.jarvissmarthome.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dfirago.jarvissmarthome.R;
import com.dfirago.jarvissmarthome.listeners.OnNetworkSelectedListener;
import com.dfirago.jarvissmarthome.web.model.NetworkInfo;
import com.dfirago.jarvissmarthome.web.model.NetworkSelectRequest;
import com.dfirago.jarvissmarthome.web.services.ConfigurationService;
import com.dfirago.jarvissmarthome.web.services.RestServiceFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by dmfi on 30/10/2016.
 */
public class MainFragment extends Fragment implements OnNetworkSelectedListener {

    public final static String TAG = "MainFragment";

    private ConfigurationService configurationService =
            RestServiceFactory.createService(ConfigurationService.class);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button confirmButton = (Button) view.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(new ConfirmButtonClickListener());

        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private class ConfirmButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            LinearLayout spinnerLayout = (LinearLayout) view.findViewById(R.id.main_spinner_layout);
            EditText ssidEditText = (EditText) view.findViewById(R.id.ssid_edit_text);
            EditText passwordEditText = (EditText) view.findViewById(R.id.password_edit_text);
            spinnerLayout.setVisibility(View.VISIBLE);
            NetworkSelectRequest request = new NetworkSelectRequest();
            request.setSsid(ssidEditText.getText().toString());
            request.setPassword(passwordEditText.getText().toString());
            Call<Void> responseCall = configurationService.selectNetwork(request);
            responseCall.enqueue(new SelectNetworkCallback());
        }
    }

    private class SelectNetworkCallback implements Callback<Void> {

        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            Toast.makeText(getContext(),
                    "Configuration changed successfully", Toast.LENGTH_LONG).show();
            LinearLayout spinnerLayout = (LinearLayout) getActivity().findViewById(R.id.main_spinner_layout);
            spinnerLayout.setVisibility(View.GONE);
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t) {
            Log.e(TAG, "Failed to update device configuration", t);
            Toast.makeText(getContext(),
                    "Failed to update device configuration", Toast.LENGTH_LONG).show();
            LinearLayout spinnerLayout = (LinearLayout) getActivity().findViewById(R.id.main_spinner_layout);
            spinnerLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNetworkSelected(NetworkInfo networkInfo) {
        getFragmentManager().popBackStackImmediate();
        EditText ssidEditText = (EditText) getActivity().findViewById(R.id.ssid_edit_text);
        ssidEditText.setText(networkInfo.getSsid());
    }
}
