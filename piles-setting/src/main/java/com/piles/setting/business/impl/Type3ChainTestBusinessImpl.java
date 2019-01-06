package com.piles.setting.business.impl;

import com.google.common.primitives.Bytes;
import com.piles.common.business.IBusiness;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.CRC16Util;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 链路测试帧(U 格式)(双向 交直流共用) 充电桩  运营管理系统
 */
@Slf4j
@Service("type3ChainTestBusiness")
public class Type3ChainTestBusinessImpl implements IBusiness {

    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        log.info("接收到【type3】链路测试帧报文");
        byte[] head = BytesUtil.copyBytes(msg, 0, 6);
        byte[] cmd = BytesUtil.intToBytesLittle(101);
        int serial = BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, 44, 2));
        if (serial == 65535) {
            serial = 1;
        } else {
            serial++;
        }

        byte[] data = Bytes.concat(BytesUtil.rightPadBytes(new byte[4], 4, (byte) 0x00),
                BytesUtil.intToBytesLittle(serial, 2));
        byte[] crc = new byte[]{CRC16Util.getType3CRC(Bytes.concat(cmd, data))};
        int length = head.length + cmd.length + data.length + crc.length;
        byte[] lengths = BytesUtil.intToBytesLittle(length);
        head[2] = lengths[0];
        head[3] = lengths[1];
        return Bytes.concat(head, cmd, data, crc);
    }

    public static void main(String[] args) {
        byte[] test = new byte[]{(byte) 0xFF, (byte) 0xFF};
        System.out.println(BytesUtil.bytesToIntLittle(test));
    }


}
