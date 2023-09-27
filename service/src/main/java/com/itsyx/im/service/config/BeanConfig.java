package com.itsyx.im.service.config;

import com.itsyx.im.common.config.AppConfig;
import com.itsyx.im.common.enums.ImUrlRouteWayEnum;
import com.itsyx.im.common.enums.RouteHashMethodEnum;
import com.itsyx.im.common.route.RouteHandle;
import com.itsyx.im.common.route.algorithm.consistenthash.AbstractConsistentHash;
import com.itsyx.im.common.enums.ImUrlRouteWayEnum;
//import com.itsyx.im.common.enums.RouteHashMethodEnum;
import com.itsyx.im.common.route.RouteHandle;
import com.itsyx.im.common.route.algorithm.consistenthash.AbstractConsistentHash;
import com.itsyx.im.common.route.algorithm.consistenthash.ConsistentHashHandle;
import com.itsyx.im.common.route.algorithm.consistenthash.TreeMapConsistentHash;
import com.itsyx.im.common.route.algorithm.loop.LoopHandle;
import com.itsyx.im.common.route.algorithm.random.RandomHandle;
//import com.itsyx.im.service.utils.SnowflakeIdWorker;
import com.itsyx.im.service.utils.SnowflakeIdWorker;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

/**
 * @description:
 * @author: syx
 * @version: 1.0
 */
@Configuration
public class BeanConfig {

    @Autowired
    AppConfig appConfig;

    @Bean
    public ZkClient buildZKClient() {
        return new ZkClient(appConfig.getZkAddr(), appConfig.getZkConnectTimeOut());
    }

    @Bean
    public RouteHandle routeHandle() throws Exception {

        Integer imRouteWay = appConfig.getImRouteWay(); //获得负载均衡算法类型
        String routWay = "";

        ImUrlRouteWayEnum handler = ImUrlRouteWayEnum.getHandler(imRouteWay);
        routWay = handler.getClazz();

        RouteHandle routeHandle = (RouteHandle) Class.forName(routWay).newInstance();
        if(handler == ImUrlRouteWayEnum.HASH){
            Method setHash = Class.forName(routWay).getMethod("setHash", AbstractConsistentHash.class);
            Integer consistentHashWay = appConfig.getConsistentHashWay();
            String hashWay = "";

            RouteHashMethodEnum hashHandler = RouteHashMethodEnum.getHandler(consistentHashWay);
            hashWay = hashHandler.getClazz();
            AbstractConsistentHash consistentHash = (AbstractConsistentHash) Class.forName(hashWay).newInstance();
            setHash.invoke(routeHandle,consistentHash);
        }

        return routeHandle;
    }

    @Bean
    public EasySqlInjector easySqlInjector () {
        return new EasySqlInjector();
    }

    // 雪花算法
    @Bean
    public SnowflakeIdWorker buildSnowflakeSeq() throws Exception {
        return new SnowflakeIdWorker(0);
    }


}
