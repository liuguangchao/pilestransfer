package com.piles.record.business.impl;


import com.alibaba.fastjson.JSON;
import com.google.common.primitives.Bytes;
import com.piles.common.business.IBusiness;
import com.piles.common.entity.ChannelEntity;
import com.piles.common.entity.type.TradeType;
import com.piles.common.util.*;
import com.piles.record.domain.UploadChargeMonitor;
import com.piles.record.domain.UploadRecord;
import com.piles.record.entity.Type3UploadChargeMonitorRequest;
import com.piles.record.entity.XunDaoUploadChargeMonitorRequest;
import com.piles.record.service.IUploadChargeMonitorService;
import com.piles.record.service.IUploadRecordService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 上传充电过程监测数据 接口实现
 */
@Slf4j
@Service("type3UploadChargeMonitorBusiness")
public class Type3UploadChargeMonitorBusinessImpl implements IBusiness {

    @Resource
    private IUploadChargeMonitorService uploadChargeMonitorService;
    @Resource
    private IUploadRecordService uploadRecordService;


    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        log.info("接收到【type3】充电桩上传充电过程监测数据报文");
        byte[] dataBytes = BytesUtil.copyBytes(msg, 12, (msg.length - 12));

        //依照报文体规则解析报文
        Type3UploadChargeMonitorRequest uploadChargeMonitorRequest = Type3UploadChargeMonitorRequest.packEntity(dataBytes);
        log.info("接收到【type3】充电桩上传充电过程监测数据报文:{}", uploadChargeMonitorRequest.toString());

        int workStatus = uploadChargeMonitorRequest.getWorkStatus();
        GunStatusMapUtil.put(uploadChargeMonitorRequest.getPileNo(), TradeType.HONG_JIALI, uploadChargeMonitorRequest.getGunNo(), workStatus);

        UploadChargeMonitor uploadChargeMonitor = buildServiceEntity(uploadChargeMonitorRequest);
        //调用底层接口
        uploadChargeMonitorService.uploadChargeMonitor(uploadChargeMonitor);

        //组装返回报文体
        return packageInfo(msg);
    }

    private UploadChargeMonitor buildServiceEntity(Type3UploadChargeMonitorRequest uploadChargeMonitorRequest) {
        UploadChargeMonitor updateStatusReport = new UploadChargeMonitor();
        updateStatusReport.setTradeTypeCode(TradeType.HONG_JIALI.getCode());
        updateStatusReport.setPileNo(uploadChargeMonitorRequest.getPileNo());
        return updateStatusReport;
    }


    private byte[] packageInfo(byte[] msg) {
        //依照报文体规则解析报文

        byte[] head = BytesUtil.copyBytes(msg, 0, 6);
        byte[] cmd = BytesUtil.intToBytesLittle(103);
//        byte[] type = BytesUtil.intToBytesLittle(1, 1);
        byte[] dataTemp = BytesUtil.copyBytes(msg, 8, 5);

        byte[] crc = new byte[]{CRC16Util.getType3CRC(Bytes.concat(cmd, dataTemp))};
        int length = head.length + cmd.length + dataTemp.length + crc.length;
        byte[] lengths = BytesUtil.intToBytes(length);
        head[2] = lengths[0];
        head[3] = lengths[1];
        return Bytes.concat(head, cmd, dataTemp, crc);
    }


}
