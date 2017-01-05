package com.dfirago.jarvissmarthome.web.model;

import java.util.List;

/**
 * Created by dmfi on 03/01/2017.
 */

public class NetworksResponse {

    private List<NetworkInfo> networks;

    public List<NetworkInfo> getNetworks() {
        return networks;
    }

    public void setNetworks(List<NetworkInfo> networks) {
        this.networks = networks;
    }
}
