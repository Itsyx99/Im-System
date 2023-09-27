package com.itsyx.im.common.route.algorithm.consistenthash;

import com.itsyx.im.common.route.RouteHandle;

import java.util.List;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
public class ConsistentHashHandle implements RouteHandle {

    //TreeMap
    private AbstractConsistentHash hash;

    public void setHash(AbstractConsistentHash hash) {
        this.hash = hash;
    }

    @Override
    public String routeServer(List<String> values, String key) {
        return hash.process(values,key);
    }
}
