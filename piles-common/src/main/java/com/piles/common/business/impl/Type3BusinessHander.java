package com.piles.common.business.impl;

import com.piles.common.business.IBusiness;
import com.piles.common.business.IBusinessFactory;
import com.piles.common.business.IBusinessHandler;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.CRC16Util;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * 用于循道报文解析
 */
@Slf4j
@Component("type3BusinessHandler")
public class Type3BusinessHander implements IBusinessHandler {

    @Resource(name = "type3BusinessFactory")
    IBusinessFactory type3BusinessFactory;

    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        //判断msg格式 I格式 S格式 U格式
        if (msg == null) {
            log.error("报文为空");
            return null;
        }

        if (msg.length < 6) {
            log.error("报文长度为{},不识别的报文格式", msg.length);
            return null;
        }
        if (0xAA != msg[0] && 0xF5 != msg[1]) {
            log.error("报文起始位不是0xAA0xFa", msg.length);
            return null;
        }
        //排除登录报文

        byte[] crcBytes = BytesUtil.copyBytes(msg, msg.length - 1, 1);
        byte checkBytes = CRC16Util.getType3CRC(BytesUtil.copyBytes(msg, 6, msg.length - 7));
        if (crcBytes[0] != checkBytes) {
            log.error("CRC验证未通过");
            return null;
        }


        IBusiness business = type3BusinessFactory.getByMsg(msg);
        if (null != business) {
            return business.process(msg, incoming);
        }
        return null;

    }
}
