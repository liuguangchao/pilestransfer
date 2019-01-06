package com.piles.record.business.impl;


import com.piles.common.business.IBusiness;
import com.piles.common.entity.BasePushResponse;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.common.util.ChannelResponseCallBackMap;
import com.piles.record.entity.Type3UploadChargeMonitorRequest;
import com.piles.record.entity.XunDaoChargeMonitorRequest;
import com.piles.record.entity.XunDaoDCChargeMonitorRequest;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 上传充电过程监测数据 接口实现
 */
@Slf4j
@Service("type3ChargeMonitorBusiness")
public class Type3ChargeMonitorBusinessImpl implements IBusiness {

    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        log.info("接收到type3充电桩上传充电过程监测数据报文");
        byte[] dataBytes = BytesUtil.copyBytes(msg, 12, (msg.length - 12));

        //依照报文体规则解析报文
        Type3UploadChargeMonitorRequest uploadChargeMonitorRequest = Type3UploadChargeMonitorRequest.packEntity(dataBytes);
        String order = String.valueOf(BytesUtil.type3ControlByte2Int(BytesUtil.copyBytes(msg, 5, 1)));
        //依照报文体规则解析报文
        XunDaoChargeMonitorRequest remoteStartRequest = XunDaoChargeMonitorRequest.packEntityType3(uploadChargeMonitorRequest);
        log.info("接收到type3充电桩上传充电过程监测数据报文:{}", remoteStartRequest.toString());

        ChannelResponseCallBackMap.callBack(incoming, order, remoteStartRequest);
        //组装返回报文体
        return null;
    }

}
