package com.alee.mold.service;


import com.alee.mold.entity.orders;
import com.alee.mold.mapper.ordersMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ordersServiceImpl extends ServiceImpl<ordersMapper, orders> implements ordersService {
//    @Autowired
//    private ordersMapper ordersMapper_;

//    @Override
//    public boolean removeByClientId(Long clientID) {
//        return this.remove(new QueryWrapper<orders>().eq("clientID", clientID));
//    }

}
