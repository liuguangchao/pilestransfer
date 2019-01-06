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
        return buildReponse(msg, flag);
    }

    //封装返回报文
    private byte[] buildReponse(byte[] msg, boolean result) {

        byte[] head = new byte[]{(byte) 0xAA, (byte) 0xF5};
        byte[] length = BytesUtil.intToBytesLittle(20, 1);
        byte[] contrl = BytesUtil.copyBytes(msg, 2, 4);

        byte[] type = BytesUtil.intToBytesLittle(XunDaoTypeCode.SEND_DATA_CODE.getCode(), 1);
        byte[] beiyong = new byte[]{0x00};
        byte[] reason = new byte[]{0x03, 0x00};
        byte[] recordType = BytesUtil.intToBytesLittle(this.recordType, 1);
        byte[] data = BytesUtil.copyBytes(msg, 13, 8);
        byte[] resultByte = result == true ? new byte[]{0x00} : BytesUtil.intToBytesLittle(3, 1);
        data = Bytes.concat(data, resultByte);
        byte[] crc = CRC16Util.getXunDaoCRC(data);

        return Bytes.concat(head, length, contrl, type, beiyong, reason, crc, recordType, data);


    }

    private UploadRecord buildServiceEntity(Type3UploadRecordRequest uploadRecordRequest, int gunNo) {
        UploadRecord uploadRecord = new UploadRecord();
        uploadRecord.setTradeTypeCode(TradeType.HONG_JIALI.getCode());
        uploadRecord.setOrderNo(uploadRecordRequest.getOrderNo());
        uploadRecord.setPileNo(uploadRecordRequest.getPileNo());
        uploadRecord.setEndReason(uploadRecordRequest.getStopChargeReason());
        uploadRecord.setTotalAmmeterDegree(uploadRecordRequest.getTotalAmmeterDegree());
        uploadRecord.setSerial(Integer.parseInt(uploadRecordRequest.getSerial()));
        uploadRecord.setPileType(0);
        uploadRecord.setGunNo(gunNo);
        return uploadRecord;
    }
}
