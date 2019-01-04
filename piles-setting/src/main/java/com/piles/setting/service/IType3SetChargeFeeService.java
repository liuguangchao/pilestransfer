package com.piles.setting.service;

import com.piles.common.entity.BasePushCallBackResponse;
import com.piles.setting.entity.SetChargePlotRequest;
import com.piles.setting.entity.Type3SetChargeFeePushRequest;

/**
 * Created by zhanglizhi on 2018/4/10.
 */
public interface IType3SetChargeFeeService {

    /**
     * 下发设置电价接口
     *
     * @param xunDaoModifyIPPushRequest
     * @return 请求信息
     */
    BasePushCallBackResponse<SetChargePlotRequest> doPush(Type3SetChargeFeePushRequest xunDaoModifyIPPushRequest);


}
