package com.piles.entity.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 修改ip后台请求参数
 *
 * @author lizhi.zhang
 * @create 2018-04-11 上午07:32
 **/
@Data
public class ChargeFeeRequest {

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

}

