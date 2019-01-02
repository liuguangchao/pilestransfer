package com.piles.record.business.impl;

import com.google.common.primitives.Bytes;
import com.piles.common.business.IBusiness;
import com.piles.common.entity.type.XunDaoTypeCode;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.CRC16Util;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

/**
 * 循道时钟同步接口逻辑
 */
@Slf4j
@Component("xunDaoLockSyncBusiness")
public class Type3LockSyncBusinessImpl implements IBusiness {


    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        log.info("接收到循道时钟同步请求报文");
        //依照报文体规则解析报文
        DateTime dateTime = new DateTime();
        int ms = dateTime.getMillisOfSecond();
        int min = dateTime.getMinuteOfHour();
        int hour = dateTime.getHourOfDay();
        int day = dateTime.getDayOfMonth();
        int month = dateTime.getMonthOfYear();
        int year = dateTime.getYear() - 2000;
        byte[] data = Bytes.concat(BytesUtil.intToBytesLittle(ms), BytesUtil.intToBytesLittle(min, 1), BytesUtil.intToBytesLittle(hour, 1),
                BytesUtil.intToBytesLittle(day, 1), BytesUtil.intToBytesLittle(month, 1), BytesUtil.intToBytesLittle(year, 1));

        byte[] head = BytesUtil.copyBytes(msg, 0, 6);
        byte[] cmd = BytesUtil.intToBytesLittle(3);
        byte[] type = BytesUtil.intToBytesLittle(1, 1);
        byte[] offset = BytesUtil.intToBytesLittle(2, 4);
        byte[] offsetLen = BytesUtil.intToBytesLittle(8, 2);
        byte[] dataTemp = Bytes.concat(type, offset, offsetLen);

        byte[] crc = new byte[]{CRC16Util.getType3CRC(Bytes.concat(cmd, dataTemp))};
        int length = head.length + cmd.length + dataTemp.length + crc.length;
        byte[] lengths = BytesUtil.intToBytes(length);
        head[2] = lengths[0];
        head[3] = lengths[1];
        return Bytes.concat(head, cmd, dataTemp, crc);

    }
}
