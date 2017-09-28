package com.app.newwisdom.model.entity;

/**
 * Created by ninedau_zheng on 2017/8/1.
 */


/*
* "sim": {
        "addedTime": "2017-07-10 18:04:11",
        "currentPlanCode": "",
        "currentPlanExpireTime": "2017-08-31 00:00:00",
        "currentPlanName": "",
        "currentPlanStartTime": "2017-08-01 00:00:00",
        "iccid": "8986031643202889946",
        "imsi": "",
        "msisdn": "1064935682740",
        "nextPlanCode": "",
        "nextPlanName": "",
        "operatorType": 3,
        "planCateID": 3,
        "status": 5,
        "totalFlowSize": 14336,
        "usedFlowSize": 6168.12109375
    },
 "sim": {
        "iccid": "8986031644202307222",
        "status": 6,
        "usedFlowSize": 0
       },
    */
public class SimBean {

    public boolean isTargetCard = false;
    public String currentPlanExpireTime;
    public String currentPlanStartTime;
    public String currentPlanName;
//    卡状态  5 = 激活  6=关机
    public int status ;
    //总流量
    public double totalFlowSize;
    //已使用流量
    public double usedFlowSize;

    public String iccid;

    public String msisdn;

}
