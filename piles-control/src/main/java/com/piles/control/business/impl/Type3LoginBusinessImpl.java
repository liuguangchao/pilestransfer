package com.piles.control.business.impl;

import com.google.common.primitives.Bytes;
import com.piles.common.business.IBusiness;
import com.piles.common.entity.ChannelEntity;
import com.piles.common.entity.type.TradeType;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.CRC16Util;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.control.entity.LoginRequest;
import com.piles.control.service.ILoginService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 登录接口逻辑
 */
@Slf4j
@Component("type3LoginBusiness")
public class Type3LoginBusinessImpl implements IBusiness {

    @Resource
    private ILoginService loginService;


    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        log.info("接收到登录请求报文");
        //依照报文体规则解析报文
        LoginRequest loginRequest = LoginRequest.packEntityHongjiali(msg);
        loginRequest.setTradeType(TradeType.HONG_JIALI);
        log.info("接收到登录请求报文:{}", loginRequest.toString());
        //调用底层接口
        boolean flag = loginService.login(loginRequest);
        if (flag) {
            ChannelEntity channelEntity = new ChannelEntity(loginRequest.getPileNo().trim(), TradeType.fromCode(TradeType.HONG_JIALI.getCode()));
            ChannelMapByEntity.addChannel(channelEntity, incoming);
            ChannelMapByEntity.addChannel(incoming, channelEntity);

        }

        //组装返回报文体

        byte[] head = BytesUtil.copyBytes(msg, 0, 6);
        byte[] cmd = BytesUtil.intToBytesLittle(105);
        byte[] data = Bytes.concat(BytesUtil.rightPadBytes(new byte[4], 4, (byte) 0x00),
                BytesUtil.copyBytes(msg, msg.length - 10, 4),
                BytesUtil.rightPadBytes(new byte[134], 134, (byte) 0x00),
                BytesUtil.rightPadBytes(new byte[1], 1, (byte) 0x00));
        byte[] crc = new byte[]{CRC16Util.getType3CRC(Bytes.concat(cmd, data))};
        int length = head.length + cmd.length + data.length + crc.length;
        byte[] lengths = BytesUtil.intToBytesLittle(length);
        head[2] = lengths[0];
        head[3] = lengths[1];
        return Bytes.concat(head, cmd, data, crc);
    }
}
