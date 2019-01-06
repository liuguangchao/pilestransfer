package com.piles.setting.entity;

import com.piles.common.entity.BasePushResponse;
import com.piles.common.util.BytesUtil;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by lgc on 19/1/2.
 */
@Data
public class SetChargePlotRequest extends BasePushResponse implements Serializable {
    /**
     * 结果 1位 BIN    0: 启动成功 1: 枪被预约 2: 其他原因失败
     */
    private int result;

    /**
     * 解析报文并封装request体
     *
     * @param msg
     * @return
     */
    public static SetChargePlotRequest packEntity(byte[] msg) {
        SetChargePlotRequest request = new SetChargePlotRequest();
        request.setResult(BytesUtil.bytesToInt(BytesUtil.copyBytes(msg, 8, 1), 1));
        return request;
    }
}
