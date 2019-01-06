package com.piles.setting.entity;

import com.google.common.primitives.Bytes;
import com.piles.common.entity.BasePushRequest;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.CRC16Util;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 循道 下发修改ip地址 后台--》充电桩
 */
@Data
public class Type3SetChargeFeePushRequest extends BasePushRequest implements Serializable {

    /**
     * 对应厂商类型  1:蔚景 2: 循道
     */
    private int tradeTypeCode;
    /**
     * 桩号
     */
    private String pileNo;

    /**
     * 默认必填流水号
     */
    private String serial;
    private List<FeeInfo> feeInfoList;


    /**
     * 封装报文体
     *
     * @param request
     * @return
     */
    public static byte[] packBytes(Type3SetChargeFeePushRequest request) {
        Collections.sort(request.getFeeInfoList());
        byte[] data = new byte[]{};
        for (FeeInfo feeInfo : request.getFeeInfoList()) {
            byte[] startHour = BytesUtil.intToBytes(feeInfo.getStartHour(), 1);
            byte[] startMin = BytesUtil.intToBytes(feeInfo.getStartMin(), 1);
            byte[] endHour = BytesUtil.intToBytes(feeInfo.getEndHour(), 1);
            byte[] endMin = BytesUtil.intToBytes(feeInfo.getEndMin(), 1);
            byte[] dataint = BytesUtil.intToBytesLittle(feeInfo.getFee().multiply(new BigDecimal(100)).intValue(), 4);
            data = Bytes.concat(data, startHour, startMin, endHour, endMin, dataint);
        }
        byte[] serial = BytesUtil.intToBytes(Integer.parseInt(request.getSerial()), 1);

        byte[] head = new byte[]{(byte) 0xAA, (byte) 0xF5, 0x00, 0x00, 0x10};
        head = Bytes.concat(head, serial);
        byte[] cmd = BytesUtil.intToBytesLittle(1103);


        byte[] crc = new byte[]{CRC16Util.getType3CRC(Bytes.concat(cmd, data))};
        int length = head.length + cmd.length + data.length + crc.length;
        byte[] lengths = BytesUtil.intToBytesLittle(length);
        head[2] = lengths[0];
        head[3] = lengths[1];
        return Bytes.concat(head, cmd, data, crc);
    }

    public static void main(String[] args) {
        byte[] msg = BytesUtil.intToBytesLittle(1103);
        String temp = "";
        for (byte b : msg) {
            temp += " " + Integer.toHexString(Byte.toUnsignedInt(b));
        }
        System.out.println(temp);
        Type3SetChargeFeePushRequest chargeFeeRequest = new Type3SetChargeFeePushRequest();
        chargeFeeRequest.setSerial("123");
        chargeFeeRequest.setPileNo("12345678912345678");
        chargeFeeRequest.setTradeTypeCode(4);
        List<FeeInfo> feeInfoList = new ArrayList<>();
        for (int i = 0; i < 16; i++) {
            FeeInfo feeInfo = new FeeInfo();
            feeInfo.setIndex(i);
            feeInfo.setStartHour(i);
            feeInfo.setStartMin(i);
            feeInfo.setEndHour(i);
            feeInfo.setEndMin(i);
            feeInfo.setFee(new BigDecimal(i));
            feeInfoList.add(feeInfo);
        }
        Collections.sort(feeInfoList);
        chargeFeeRequest.setFeeInfoList(feeInfoList);
        msg = packBytes(chargeFeeRequest);
        temp = "";
        for (byte b : msg) {
            temp += " " + Integer.toHexString(Byte.toUnsignedInt(b));
        }
        System.out.println(temp);
    }


}
