package com.piles.record.entity;

import com.piles.common.entity.type.TradeType;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 循道上传充电过程监测数据接口请求实体
 */
@Data
public class Type3UploadChargeMonitorRequest implements Serializable {
    //桩编号
    private String pileNo;
    private int pileType;
    private int gunNums;

    private int gunNo;
    private int gunType;
    // 工作状态 0空闲
    private int workStatus;

    private int soc;
    private int maxAlermCode;
    private int connectStatus;
    private BigDecimal fee;


    //充电输出电压(直 流最大输出电压)	BIN	2	精确到小数点后一位
    private BigDecimal highestAllowVoltage;
    //充电输出电流(直 流最大输出电流)	BIN	2	单位：A，精确到小数点后二位
    private BigDecimal highestAllowElectricity;

    private BigDecimal bmsAv;
    private BigDecimal bmsAn;
    private BigDecimal bmsType;

    private BigDecimal av;
    private BigDecimal bv;
    private BigDecimal cv;
    private BigDecimal an;
    private BigDecimal bn;
    private BigDecimal cn;

    private int needTime;
    private int chargeTime;
    private BigDecimal chargeQuantity;
    private int chargeByType;
    private int chargeType;
    private BigDecimal chargeData;
    private String cardNo;
    private Date startTime;

    private BigDecimal chargeW;


    /**
     * 解析报文并封装request体
     *
     * @param msg
     * @return
     */
    public static Type3UploadChargeMonitorRequest packEntity(byte[] msg) {
        Type3UploadChargeMonitorRequest request = new Type3UploadChargeMonitorRequest();
        int cursor = 0;
        int j = 0;
        while (msg[j] != 0x00 && j < 32) {
            j++;
        }
        request.setPileNo(BytesUtil.ascii2Str(BytesUtil.copyBytes(msg, cursor, j)));
        cursor += 32;
        request.setGunNums(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setGunNo(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)) - 1);
        cursor += 1;
        request.setGunType(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setWorkStatus(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setSoc(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setMaxAlermCode(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4)));
        cursor += 4;
        request.setConnectStatus(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setFee(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4))));
        cursor += 12;
        request.setHighestAllowVoltage(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setHighestAllowElectricity(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setBmsAv(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setBmsAn(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setBmsType(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1))));
        cursor += 1;
        request.setAv(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setBv(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setCv(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setAn(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setBn(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setCn(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 2;
        request.setNeedTime(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2)));
        cursor += 2;
        request.setChargeTime(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4)));
        cursor += 4;
        request.setChargeQuantity(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 12;
        request.setChargeByType(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setChargeType(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setChargeData(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 5;

        j = 0;
        while (msg[cursor + j] != 0x00 && j < 32) {
            j++;
        }
        request.setCardNo(BytesUtil.ascii2Str(BytesUtil.copyBytes(msg, cursor, j)));
        cursor += 33;

        request.setStartTime(BytesUtil.byte2Date(BytesUtil.copyBytes(msg, cursor, 7)));
        cursor += 16;
        request.setChargeW(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        request.setPileType(TradeType.HONG_JIALI.getCode());
        return request;
    }

    @Override
    public String toString() {
        return "Type3UploadChargeMonitorRequest{" +
                "pileNo='" + pileNo + '\'' +
                ", pileType=" + pileType +
                ", gunNums=" + gunNums +
                ", gunNo=" + gunNo +
                ", gunType=" + gunType +
                ", workStatus=" + workStatus +
                ", soc=" + soc +
                ", maxAlermCode=" + maxAlermCode +
                ", connectStatus=" + connectStatus +
                ", fee=" + fee +
                ", highestAllowVoltage=" + highestAllowVoltage +
                ", highestAllowElectricity=" + highestAllowElectricity +
                ", bmsAv=" + bmsAv +
                ", bmsAn=" + bmsAn +
                ", bmsType=" + bmsType +
                ", av=" + av +
                ", bv=" + bv +
                ", cv=" + cv +
                ", an=" + an +
                ", bn=" + bn +
                ", cn=" + cn +
                ", needTime=" + needTime +
                ", chargeTime=" + chargeTime +
                ", chargeQuantity=" + chargeQuantity +
                ", chargeByType=" + chargeByType +
                ", chargeType=" + chargeType +
                ", cardNo='" + cardNo + '\'' +
                ", startTime='" + startTime + '\'' +
                ", chargeW=" + chargeW +
                '}';
    }

    public static void main(String[] args) {
        byte[] msg = new byte[]{(byte) 0xaa, (byte) 0xf5, (byte) 0xc8, (byte) 0x0, (byte) 0x3, (byte) 0xa9, (byte) 0x68, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, (byte) 0x38, (byte) 0x39, (byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, (byte) 0x38, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x2, (byte) 0x1, (byte) 0x1, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x2, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x2, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x52, (byte) 0x3d, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0xff, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x48, (byte) 0x0, (byte) 0x3d, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x0, (byte) 0x4};
        byte[] dataBytes = BytesUtil.copyBytes(msg, 12, (msg.length - 12));

        //依照报文体规则解析报文
        Type3UploadChargeMonitorRequest uploadChargeMonitorRequest = Type3UploadChargeMonitorRequest.packEntity(dataBytes);
        System.out.println(uploadChargeMonitorRequest.toString());
    }
}
