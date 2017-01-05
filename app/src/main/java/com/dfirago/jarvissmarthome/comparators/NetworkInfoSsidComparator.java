package com.dfirago.jarvissmarthome.comparators;

import com.dfirago.jarvissmarthome.web.model.NetworkInfo;

import java.util.Comparator;

/**
 * Created by dmfi on 05/01/2017.
 */

public class NetworkInfoSsidComparator implements Comparator<NetworkInfo> {

    private static NetworkInfoSsidComparator instance;

    @Override
    public int compare(NetworkInfo lhs, NetworkInfo rhs) {
        String lhsSsid = lhs.getSsid();
        String rhsSsid = rhs.getSsid();
        return lhsSsid.compareTo(rhsSsid);
    }

    public static synchronized NetworkInfoSsidComparator getInstance() {
        if (instance == null) {
            instance = new NetworkInfoSsidComparator();
        }
        return instance;
    }

    private NetworkInfoSsidComparator() {

    }
}
