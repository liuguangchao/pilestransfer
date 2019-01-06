package com.piles.record.entity;

import com.google.common.collect.Lists;
import com.piles.common.entity.type.TradeType;
import com.piles.common.util.BytesUtil;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 循道上传充电记录接口请求实体
 */
@Data
public class Type3UploadRecordRequest implements Serializable {

    /**
     * 桩编号 8位 BCD
     */
    private String pileNo;
    //充电枪类型	BCD 码16Byte
    private int gunType;
    private int gunNo;
    //物理卡号 BIN 码 8Byte
    private long cardNo;
    //开始时间 BIN 码 7Byte 小端 CP56Time2a 格式
    private Date startTime;
    //结束时间 BIN 码 7Byte 小端 CP56Time2a 格式
    private Date endTime;
    private long chargeTime;
    //开始 SOC BIN 码 1Byte 精确到 1%，1 表示 SOC=1% 类 推
    private int beginSoc;
    //结束 SOC BIN 码 1Byte 精确到 1%，1 表示 SOC=1% 类 推
    private int endSoc;
    // 0x06-人工正常停止 0x07-输出失败 0x08-系统故障 0x09-未结账 0x0A-CP 异常 0x0B-意外断电
    private int stopChargeReason;

    //总电量 BIN 码 4Byte 小端 精确到小数点后两位
    private BigDecimal totalAmmeterDegree;
    private long innerNo;
    private String vin;
    //每半小时电量 BIN 码 2Byte 精确到小数点后两位 0:00-0:30 电量----23:30-24:00 电量
    private List<BigDecimal> everyHalfHourDegress;
    //计量示数类型 BCD 码 2Byte  小端  0002-充电量
    private String serial;

    //订单号 ascii 32位小端
    private String orderNo;

    /**
     * 解析报文并封装request体
     *
     * @param msg
     * @return
     */
    public static Type3UploadRecordRequest packEntity(byte[] msg) {
        Type3UploadRecordRequest request = new Type3UploadRecordRequest();
        int cursor = 0;
        int j = 0;
        while (msg[j] != 0x00 && j < 32) {
            j++;
        }
        request.setPileNo(BytesUtil.ascii2Str(BytesUtil.copyBytes(msg, cursor, j)));
        cursor += 32;
        request.setGunType(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setGunNo(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)) - 1);
        cursor += 1;
        j = 0;
        while (msg[cursor + j] != 0x00 && j < 32) {
            j++;
        }
        String cardNo = BytesUtil.ascii2Str(BytesUtil.copyBytes(msg, cursor, j));
        request.setCardNo(StringUtils.isEmpty(cardNo) ? 0L : Long.valueOf(cardNo));
        cursor += 32;
        j = 0;

        request.setStartTime(BytesUtil.byte2Date(BytesUtil.copyBytes(msg, cursor, 7)));
        cursor += 8;

        request.setEndTime(BytesUtil.byte2Date(BytesUtil.copyBytes(msg, cursor, 7)));
        cursor += 8;
        request.setChargeTime(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4)));
        cursor += 4;
        request.setBeginSoc(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setEndSoc(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 1)));
        cursor += 1;
        request.setStopChargeReason(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4)));
        cursor += 4;
        request.setTotalAmmeterDegree(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        cursor += 16;
        request.setInnerNo(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 4)));
        cursor += 22;
        while (msg[cursor + j] != 0x00 && j < 17) {
            j++;
        }
        request.setVin(BytesUtil.ascii2Str(BytesUtil.copyBytes(msg, cursor, j)));
        cursor += 25;
        //处理每半个小时 共24小时 48个
        List<BigDecimal> list = Lists.newArrayList();
        for (int i = 0; i < 48; i++) {
            list.add(BigDecimal.valueOf(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, cursor, 2))).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
            cursor += 2;
        }
        request.setSerial(String.valueOf(request.getCardNo()));

        return request;
    }

    @Override
    public String toString() {
        return "Type3UploadRecordRequest{" +
                "pileNo='" + pileNo + '\'' +
                ", gunType=" + gunType +
                ", gunNo=" + gunNo +
                ", cardNo=" + cardNo +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", chargeTime=" + chargeTime +
                ", beginSoc=" + beginSoc +
                ", endSoc=" + endSoc +
                ", stopChargeReason=" + stopChargeReason +
                ", totalAmmeterDegree=" + totalAmmeterDegree +
                ", innerNo=" + innerNo +
                ", vin='" + vin + '\'' +
                ", everyHalfHourDegress=" + everyHalfHourDegress +
                ", serial='" + serial + '\'' +
                ", orderNo='" + orderNo + '\'' +
                '}';
    }


}
