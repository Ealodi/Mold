package com.alee.mold.service;

import com.alee.mold.entity.orders;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ordersService extends IService<orders> {
    // 按 clientID 删除该客户的所有订单（逻辑删除）
    //public boolean removeByClientId(Long clientID);
}
