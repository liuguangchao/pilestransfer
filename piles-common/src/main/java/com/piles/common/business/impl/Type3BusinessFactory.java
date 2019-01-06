package com.piles.common.business.impl;

import com.piles.common.business.IBusiness;
import com.piles.common.business.IBusinessFactory;
import com.piles.common.util.BytesUtil;
import com.piles.common.util.SpringContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * 获取循道解析报文业务对象
 */
@Slf4j
@Component("type3BusinessFactory")
public class Type3BusinessFactory implements IBusinessFactory {

    @Override
    public IBusiness getByMsg(byte[] msg) {

        //如果是变长根据协议类型判断
        //取出第七个字节，变长报文该位置的类型标识
        int typeCode = BytesUtil.bytesToIntLittle(BytesUtil.copyBytes(msg, 6, 2));
        switch (typeCode) {
            //登陆
            case 106:
                return SpringContextUtil.getBean("type3LoginBusiness");
            //时钟同步
            case 4:
                return SpringContextUtil.getBean("type3LockSyncBusiness");
            //心跳
            case 102:
                return SpringContextUtil.getBean("type3ChainTestBusiness");
            case 104:
                return SpringContextUtil.getBean("type3UploadChargeMonitorBusiness");
            //充电
            case 8:
                return SpringContextUtil.getBean("type3StartBusiness");
            //停止充电
            case 6:
                return SpringContextUtil.getBean("type3StopBusiness");
            //上传充电记录
            case 202:
                return SpringContextUtil.getBean("type3UploadRecordBusiness");
            //设置电价
            case 1104:
                return SpringContextUtil.getBean("type3SetChargePlotBusiness");
            //设置电价
            case 114:
                return SpringContextUtil.getBean("type3ChargeMonitorBusiness");


            default:
                log.error("接收到报文{}，未匹配到合适的命令码：{}", msg, typeCode);
                return null;
        }

    }

}
