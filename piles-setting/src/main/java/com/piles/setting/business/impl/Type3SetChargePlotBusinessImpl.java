package com.piles.setting.business.impl;

import com.piles.common.business.IBusiness;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.setting.entity.SetChargePlotRequest;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 设置策略
 */
@Slf4j
@Service("type3SetChargePlotBusiness")
public class Type3SetChargePlotBusinessImpl implements IBusiness {

    @Override
    public byte[] process(byte[] msg, Channel incoming) {

        log.info("接收到【type3】设置电价策略报文");
        String order = String.valueOf(BytesUtil.type3ControlByte2Int(BytesUtil.copyBytes(msg, 5, 1)));
        //依照报文体规则解析报文
        SetChargePlotRequest setChargePlotRequest = SetChargePlotRequest.packEntity(msg);
        log.info("接收到【type3】设置电价策略报文返回结果" + setChargePlotRequest.toString());
        ChannelResponseCallBackMap.callBack(incoming, order, setChargePlotRequest);
        return null;

    }

    public static void main(String[] args) {
        byte[] test = new byte[]{(byte) 0xFF, (byte) 0xFF};
        System.out.println(BytesUtil.bytesToIntLittle(test));
    }


}
