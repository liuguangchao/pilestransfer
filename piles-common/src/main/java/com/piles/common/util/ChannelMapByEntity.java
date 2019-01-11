package com.piles.common.util;


import com.piles.common.entity.ChannelEntity;
import com.piles.common.entity.type.TradeType;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Slf4j
public class ChannelMapByEntity {

    private ChannelMapByEntity() {
    }

    /**
     * key:桩编号,value:channel
     */
    private static Map<String, Channel> channelMap2 = new HashedMap(  );

    /**
     * key:channel,value:桩编号
     */
    private static Map<Channel, String> channelMap3 =  new HashedMap(  );

    /**
     *
     * add channelInfo
     *
     * @param channel
     * @return void
     * @exception
     */
    public static void addChannel(ChannelEntity channelEntity, Channel channel) {
        String key=channelEntity.getTradeType().getCode()+"_"+channelEntity.getPileNo();
        if (!channelMap2.containsKey(key  )||
                channel.equals( channelMap2.containsKey(key  ) ) ) {
            channelMap2.put( key, channel );
        }
    }

    public static void addChannel(Channel channel,ChannelEntity channelEntity) {
        String key1=channelEntity.getTradeType().getCode()+"_"+channelEntity.getPileNo();
        if (!channelMap3.containsKey(channel  )||
                key1.equalsIgnoreCase( channelMap3.get( channel ) ) ) {
            channelMap3.put( channel, key1);
        }
    }


    /**
     * 根据桩编号移出channel
     */
    public static void removeChannel(ChannelEntity channelEntity) {
        Channel channel = channelMap2.get(channelEntity.getTradeType().getCode()+"_"+channelEntity.getPileNo());
        if (channel != null) {
            channelMap3.remove(channel);
        }
        channelMap2.remove(channelEntity.getTradeType().getCode()+"_"+channelEntity.getPileNo());
    }
    /**
     * 根据桩编号移出channel
     */
    public static void removeChannel(int tradeTypeCode,String pileNo) {
        Channel channel = channelMap2.get(tradeTypeCode+"_"+pileNo);
        if (channel != null) {
            channelMap3.remove(channel);
        }
        channelMap2.remove(tradeTypeCode+"_"+pileNo);
    }

    public static Channel getChannel(ChannelEntity channelEntity) {
        return channelMap2.get(channelEntity.getTradeType().getCode()+"_"+channelEntity.getPileNo());
    }
    public static Channel getChannel(int tradeTypeCode,String pileNo) {
        pileNo = pileNo.trim();

        return channelMap2.get(tradeTypeCode+"_"+pileNo);
    }
    public static ChannelEntity getChannel(Channel channel) {
        String temp= channelMap3.get(channel);
        if (StringUtils.isNotEmpty(temp)){
            String[] strings=temp.split("_");
            ChannelEntity channelEntity=new ChannelEntity(strings[1], TradeType.fromCode(Integer.parseInt(strings[0])));
            return channelEntity;
        }
        return null;
    }


    public static void removeChannel(Channel channel) {
        String pileNo = channelMap3.get(channel);
        if (pileNo != null) {
            channelMap2.remove(pileNo);
        }

        channelMap3.remove(channel);
    }

    /**
     * 充电桩类型
     */
    private static Map<String, Integer> pileTypeMap = new HashedMap();

    /**
     * 添加充电桩类型
     *
     * @param pileNo
     * @param pileType
     */
    public static void addPileType(String pileNo, Integer pileType) {
        if (!pileTypeMap.containsKey(pileNo)) {
            if (pileType == 2) {
                pileType = 3;
            }
            pileTypeMap.put(pileNo, pileType);
        }
    }

    /**
     * 获取充电桩类型
     *
     * @param pileNo
     * @return
     */
    public static Integer getPileType(String pileNo) {
        if (pileTypeMap.containsKey(pileNo)) {
            return pileTypeMap.get(pileNo);
        }
        return null;
    }

    /**
     * 充电桩类型
     */
    private static Map<String, byte[]> pileTypeMap2 = new HashedMap();

    /**
     * 添加充电桩类型
     *
     * @param pileNo
     * @param pileType
     */
    public static void addPileType(String pileNo, byte[] pileType) {
        if (!pileTypeMap2.containsKey(pileNo)) {
            if (pileType[0] == (byte) 0x02) {
                pileType[0] = (byte) 0x03;
            }
            pileTypeMap2.put(pileNo, pileType);
        }
    }

    /**
     * 获取充电桩类型
     *
     * @param pileNo
     * @return
     */
    public static byte[] getPileTypeArr(String pileNo) {
        if (pileTypeMap2.containsKey(pileNo)) {
            return pileTypeMap2.get(pileNo);
        }
        return null;
    }
}
