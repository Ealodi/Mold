package com.alee.mold.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.alee.mold.entity.client;
import com.alee.mold.mapper.clientMapper;
import org.springframework.stereotype.Service;

@Service
public class clientServiceImpl extends ServiceImpl<clientMapper,client> implements clientService {
}
