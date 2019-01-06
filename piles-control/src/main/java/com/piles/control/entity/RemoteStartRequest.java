package com.piles.control.entity;

import com.google.common.primitives.Bytes;
import com.piles.common.entity.BasePushResponse;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 远程开始充电
 */
@Data
public class RemoteStartRequest extends BasePushResponse implements Serializable
{
    /**
     * 订单号 8位 BIN
     */
    private long orderNo;
    /**
     * 结果 1位 BIN    0: 启动成功 1: 枪被预约 2: 其他原因失败
     */
    private int result;

    /**
     * 解析报文并封装request体
     * @param msg
     * @return
     */
    public static RemoteStartRequest packEntity(byte[] msg){
        RemoteStartRequest request=new RemoteStartRequest();
        request.setOrderNo(BytesUtil.byte2Long(BytesUtil.copyBytes(msg,0,8)));
        request.setResult(Integer.parseInt(BytesUtil.binary(BytesUtil.copyBytes(msg,8,1),10)));
        return request;
    }

    /**
     * 解析报文并封装request体
     *
     * @param msg
     * @return
     */
    public static RemoteStartRequest type3PackEntity(byte[] msg) {
        RemoteStartRequest request = new RemoteStartRequest();
        byte[] serials = BytesUtil.copyBytes(msg, 49, 32);
        int i = 0;
        while (serials[i] != 0x00) {
            i++;
        }
        request.setOrderNo(Long.valueOf(BytesUtil.ascii2Str(BytesUtil.copyBytes(serials, 0, i))));
        request.setResult(BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, 45, 4)));
        return request;
    }



    public static void main(String[] args) {

    }



}
