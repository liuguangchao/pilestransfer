package com.piles.entity.vo;

import com.alibaba.fastjson.JSON;
import com.piles.setting.entity.FeeInfo;
import lombok.Data;
import org.springframework.web.util.WebUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
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


    public static void main(String[] args) {
        ChargeFeeRequest chargeFeeRequest = new ChargeFeeRequest();
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
        System.out.println(JSON.toJSONString(chargeFeeRequest));

    }
}

