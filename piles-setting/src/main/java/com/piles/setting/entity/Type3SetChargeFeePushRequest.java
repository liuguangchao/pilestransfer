package com.piles.setting.entity;

import com.google.common.primitives.Bytes;
import com.piles.common.entity.BasePushRequest;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
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

    @Data
    class FeeInfo {
        private int index;//第几个
        private int startHour;//开始小时
        private int endHour;//结束小时
        private int startMin;//开始分钟
        private int endMin;//结束分钟
        private BigDecimal fee;//电价
    }

    /**
     * 封装报文体
     *
     * @param request
     * @return
     */
    public static byte[] packBytes(Type3SetChargeFeePushRequest request) {
        //TODO 设置推送信息
        byte[] result = new byte[0];
        result = Bytes.concat(result, BytesUtil.str2BcdLittle(request.getPileNo()));
        return result;
    }


}
