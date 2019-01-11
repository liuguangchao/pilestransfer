package com.piles.common.util;

import com.piles.common.entity.type.TradeType;
import org.apache.commons.collections.map.HashedMap;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zlz
 * @version Id: GunWorkStatusMapUtil, v 0.1
 */
public class GunWorkStatusMapUtil {
    private static Map<String, String> map = new ConcurrentHashMap<>();
    private static String PRE = "WORK_STATUS";

    public static void put(String pileNo, TradeType tradeType, String elecAmount) {
        String key = PRE + pileNo + "_" + tradeType.getCode();
        if (!map.containsKey( key )||!elecAmount.equalsIgnoreCase( map.get( key ) )) {
            map.put( key, elecAmount );
        }
    }

    public static String get(String pileNo, int tradeType) {
        String key = PRE + pileNo + "_" + tradeType;
        return map.get(key);
    }
}
