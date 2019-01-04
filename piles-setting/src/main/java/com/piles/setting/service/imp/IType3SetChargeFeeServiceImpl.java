package com.piles.setting.service.imp;

import com.alibaba.fastjson.JSON;
import com.piles.common.business.IPushBusiness;
import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.entity.type.EPushResponseCode;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.setting.entity.SetChargePlotRequest;
import com.piles.setting.entity.Type3SetChargeFeePushRequest;
import com.piles.setting.entity.XunDaoModifyIPPushRequest;
import com.piles.setting.entity.XunDaoModifyIPRequest;
import com.piles.setting.service.IType3SetChargeFeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by lgc on 19/1/4.
 */
@Slf4j
@Service
public class IType3SetChargeFeeServiceImpl implements IType3SetChargeFeeService {
    @Resource(name = "type3PushBusinessImpl")
    IPushBusiness pushBusiness;

    //类型标识 0x85
    private int typeCode = 133;

    //记录类型 0x47
    private int recordType = 71;


    /**
     * 默认1分钟超时
     */
    @Value("${timeout:60000}")
    private long timeout;

    @Override
    public BasePushCallBackResponse<SetChargePlotRequest> doPush(Type3SetChargeFeePushRequest type3SetChargeFeePushRequest) {
        log.info("进入推送修改IP地址接口,参数:{}", JSON.toJSONString(type3SetChargeFeePushRequest));
        byte[] pushMsg = Type3SetChargeFeePushRequest.packBytes(type3SetChargeFeePushRequest);
        BasePushCallBackResponse<SetChargePlotRequest> basePushCallBackResponse = new BasePushCallBackResponse();
        basePushCallBackResponse.setSerial(type3SetChargeFeePushRequest.getSerial());
        //设置桩号
        basePushCallBackResponse.setPileNo(type3SetChargeFeePushRequest.getPileNo());
        boolean flag = pushBusiness.push(pushMsg, type3SetChargeFeePushRequest.getTradeTypeCode(), type3SetChargeFeePushRequest.getPileNo(), basePushCallBackResponse, ECommandCode.REMOTE_CHARGE_CODE);
        if (!flag) {
            log.error("推送修改IP地址,厂商类型:{},桩号:{} 无法获取到长连接,请检查充电桩连接状态", type3SetChargeFeePushRequest.getTradeTypeCode(), type3SetChargeFeePushRequest.getPileNo());
            basePushCallBackResponse.setCode(EPushResponseCode.CONNECT_ERROR);
            return basePushCallBackResponse;
        }
        try {
            CountDownLatch countDownLatch = basePushCallBackResponse.getCountDownLatch();
            countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
            if (countDownLatch.getCount() > 0) {
                log.error("修改IP地址推送失败超时，厂商类型:{},桩号:{}", type3SetChargeFeePushRequest.getTradeTypeCode(), type3SetChargeFeePushRequest.getPileNo());
            }
            ChannelResponseCallBackMap.remove(type3SetChargeFeePushRequest.getTradeTypeCode(), type3SetChargeFeePushRequest.getPileNo(), type3SetChargeFeePushRequest.getSerial());
        } catch (InterruptedException e) {
            log.error("修改IP地址推送异常:{}", e);
        }
        return basePushCallBackResponse;
    }
}
