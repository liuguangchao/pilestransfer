package com.piles.record.service.impl;

import com.google.common.primitives.Bytes;
import com.piles.common.business.IPushBusiness;
import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.common.entity.BasePushRequest;
import com.piles.common.entity.type.ECommandCode;
import com.piles.common.entity.type.EPushResponseCode;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.CRC16Util;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.record.entity.XunDaoChargeMonitorRequest;
import com.piles.record.service.IChargeMonitorPushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 远程开始充电给充电桩发送消息实现类
 */
@Slf4j
@Service("chargeStatusPushService_4")
public class Type3ChargeStatusPushServiceImpl implements IChargeMonitorPushService {
    @Resource(name = "type3PushBusinessImpl")
    IPushBusiness pushBusiness;


    /**
     * 默认1分钟超时
     */
    @Value("${timeout:60000}")
    private long timeout;

    @Override
    public BasePushCallBackResponse<XunDaoChargeMonitorRequest> doPush(BasePushRequest basePushRequest) {
        byte[] pushMsg = packBytesType3(basePushRequest);
        BasePushCallBackResponse<XunDaoChargeMonitorRequest> basePushCallBackResponse = new BasePushCallBackResponse();
        basePushCallBackResponse.setSerial(basePushRequest.getSerial());
        boolean flag = pushBusiness.push(pushMsg, basePushRequest.getTradeTypeCode(), basePushRequest.getPileNo(), basePushCallBackResponse, ECommandCode.REMOTE_CHARGE_CODE);
        if (!flag) {
            basePushCallBackResponse.setCode(EPushResponseCode.CONNECT_ERROR);
            return basePushCallBackResponse;
        }
        try {
            basePushCallBackResponse.getCountDownLatch().await(timeout, TimeUnit.MILLISECONDS);
            ChannelResponseCallBackMap.remove(basePushRequest.getTradeTypeCode(), basePushRequest.getPileNo(), basePushRequest.getSerial());
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return basePushCallBackResponse;
    }

    /**
     * 封装报文体
     *
     * @param request
     * @return
     */
    public static byte[] packBytesType3(BasePushRequest request) {

        byte[] data = Bytes.concat(new byte[]{0x00, 0x00, 0x00, 0x00}, BytesUtil.intToBytesLittle(request.getGunNo(), 1), new byte[]{0x01}
        );
        byte[] serial = BytesUtil.intToBytesLittle(Integer.parseInt(request.getSerial()), 1);

        byte[] head = new byte[]{(byte) 0xAA, (byte) 0xF5, 0x00, 0x00, 0x10};
        head = Bytes.concat(head, serial);
        byte[] cmd = BytesUtil.intToBytesLittle(113);


        byte[] crc = new byte[]{CRC16Util.getType3CRC(Bytes.concat(cmd, data))};
        int length = head.length + cmd.length + data.length + crc.length;
        byte[] lengths = BytesUtil.intToBytesLittle(length);
        head[2] = lengths[0];
        head[3] = lengths[1];
        return Bytes.concat(head, cmd, data, crc);

        //组装返回报文体

    }
}
