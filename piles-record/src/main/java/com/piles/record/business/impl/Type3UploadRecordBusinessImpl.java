package com.piles.record.business.impl;


import com.google.common.primitives.Bytes;
import com.piles.common.business.IBusiness;
import com.piles.common.entity.type.TradeType;
import com.piles.common.entity.type.XunDaoTypeCode;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.CRC16Util;
import com.piles.common.util.ChannelMapByEntity;
import com.piles.common.util.MsgHelper;
import com.piles.record.domain.UploadRecord;
import com.piles.record.entity.Type3UploadChargeMonitorRequest;
import com.piles.record.entity.Type3UploadRecordRequest;
import com.piles.record.entity.XunDaoUploadRecordRequest;
import com.piles.record.service.IUploadRecordService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 上传充电记录接口实现
 */
@Slf4j
@Service("type3UploadRecordBusiness")
public class Type3UploadRecordBusinessImpl implements IBusiness {

    @Resource
    private IUploadRecordService uploadRecordService;


    //返回报文记录类型
    private int recordType = 15;

    @Override
    public byte[] process(byte[] msg, Channel incoming) {
        log.info("接收到【type3】充电桩上传充电记录报文");
        int gunNo = MsgHelper.getType3GunNo(msg);
        //依照报文体规则解析报文
        byte[] dataBytes = BytesUtil.copyBytes(msg, 12, (msg.length - 12));
        Type3UploadRecordRequest uploadRecordRequest = Type3UploadRecordRequest.packEntity(dataBytes);
        log.info("接收到【type3】充电桩上传充电记录报文:{}", uploadRecordRequest.toString());
        UploadRecord uploadRecord = buildServiceEntity(uploadRecordRequest, gunNo);
        //添加serial
        //调用底层接口
        boolean flag = uploadRecordService.uploadRecord(uploadRecord);
        if (flag) {
            return buildReponse(msg, flag);
        } else {
            return null;
        }
    }

    //封装返回报文
    private byte[] buildReponse(byte[] msg, boolean result) {

        byte[] head = BytesUtil.copyBytes(msg, 0, 6);
        byte[] cmd = BytesUtil.intToBytesLittle(201);
//        byte[] type = BytesUtil.intToBytesLittle(1, 1);
        byte[] dataTemp = Bytes.concat(new byte[]{0x00, 0x00, 0x00, 0x00}, BytesUtil.copyBytes(msg, 45, 1),
                BytesUtil.copyBytes(msg, 46, 32), BytesUtil.copyBytes(msg, 120, 4), new byte[]{0x00,
                        0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});

        byte[] crc = new byte[]{CRC16Util.getType3CRC(Bytes.concat(cmd, dataTemp))};
        int length = head.length + cmd.length + dataTemp.length + crc.length;
        byte[] lengths = BytesUtil.intToBytesLittle(length);
        head[2] = lengths[0];
        head[3] = lengths[1];
        return Bytes.concat(head, cmd, dataTemp, crc);

    }

    private UploadRecord buildServiceEntity(Type3UploadRecordRequest uploadRecordRequest, int gunNo) {
        UploadRecord uploadRecord = new UploadRecord();
        uploadRecord.setTradeTypeCode(TradeType.HONG_JIALI.getCode());
        uploadRecord.setOrderNo(uploadRecordRequest.getSerial());
        uploadRecord.setPileNo(uploadRecordRequest.getPileNo());
        uploadRecord.setEndReason(uploadRecordRequest.getStopChargeReason());
        uploadRecord.setTotalAmmeterDegree(uploadRecordRequest.getTotalAmmeterDegree());
        uploadRecord.setSerial(Integer.parseInt(uploadRecordRequest.getSerial()));
        uploadRecord.setPileType(0);
        uploadRecord.setGunNo(gunNo);
        return uploadRecord;
    }
}
