package com.alee.mold.service;

import com.alee.mold.entity.payment;
import com.alee.mold.mapper.paymentMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class paymentServiceImpl extends ServiceImpl<paymentMapper, payment> implements paymentService {
}
