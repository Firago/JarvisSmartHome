package com.dfirago.jarvissmarthome.comparators;

import com.dfirago.jarvissmarthome.web.model.NetworkInfo;

import java.util.Comparator;

/**
 * Created by dmfi on 05/01/2017.
 */

public class NetworkInfoRssiComparator implements Comparator<NetworkInfo> {

    private static NetworkInfoRssiComparator instance;

    @Override
    public int compare(NetworkInfo lhs, NetworkInfo rhs) {
        Integer lhsRssi = lhs.getRssi();
        Integer rhsRssi = rhs.getRssi();
        return rhsRssi.compareTo(lhsRssi);
    }

    public static synchronized NetworkInfoRssiComparator getInstance() {
        if (instance == null) {
            instance = new NetworkInfoRssiComparator();
        }
        return instance;
    }

    private NetworkInfoRssiComparator() {

    }
}