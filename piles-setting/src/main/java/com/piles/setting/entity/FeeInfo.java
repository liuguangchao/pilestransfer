package com.piles.setting.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by lgc on 19/1/6.
 */
@Data
public class FeeInfo implements Comparable<FeeInfo> {
    private int index;//第几个
    private int startHour;//开始小时
    private int endHour;//结束小时
    private int startMin;//开始分钟
    private int endMin;//结束分钟
    private BigDecimal fee;//电价

    @Override
    public int compareTo(FeeInfo o) {
        return this.index - o.index;
    }
}
