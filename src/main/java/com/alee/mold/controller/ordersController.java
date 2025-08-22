package com.alee.mold.controller;

import com.alee.mold.dto.OrderSearchDTO;
import com.alee.mold.entity.client;
import com.alee.mold.entity.orders;
import com.alee.mold.service.clientService;
import com.alee.mold.service.ordersService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.alee.mold.common.Range;

@RestController
@CrossOrigin
@RequestMapping("/orders")
public class ordersController extends  BaseController{
    @Autowired
    private ordersService ordersservice;

    @GetMapping("/list")
    public HashMap<String,Object> orderslist()
    {
        return ok(ordersservice.list());
    }
    @PostMapping("/add")
    public HashMap<String,Object> add(@RequestBody orders order){
        HashMap<String,Object> map = new HashMap<>();
        boolean save =  ordersservice.save(order);
        if(save) {
            map.put("code",200);
            map.put("msg","success");
            map.put("data",order);
        }else {
            map.put("code",500);
            map.put("msg","error");
            map.put("data",null);
        }
        return map;
    }
    @PostMapping("/update")
    public HashMap<String,Object> update(@RequestBody orders order){
        return ordersservice.updateById(order) ? ok() : error("修改失败",null);
    }
    @GetMapping("/get")
    public HashMap<String,Object> get(String orderId){
        return ok(ordersservice.getById(orderId));
    }
    @GetMapping("/remove")
    public HashMap<String,Object> remove(@RequestParam String orderId){
        boolean save =  ordersservice.removeById(orderId);
        if(save) {
            return ok();
        }
        else return error();
    }
    @PostMapping("/search")
    public HashMap<String,Object> search(@RequestBody OrderSearchDTO dto){
        if (dto.getOrderId() != null && !dto.getOrderId().isEmpty()) {
            return ok(ordersservice.getById(dto.getOrderId()));
        }
        QueryWrapper<orders> qw = new QueryWrapper<>();

        if (dto.getClientName() != null && !dto.getClientName().isEmpty()) {
            qw.eq("clientName", dto.getClientName());
        }
        // 过滤日期
        if (dto.getDateRange() != null && !dto.getDateRange().isEmpty()) {
            qw.between("createTime", dto.getDateRange().get(0), dto.getDateRange().get(1));
        }
        // 判断单价的范围
        if (dto.getUnitPrice() != null) {
            if(dto.getUnitPrice().getSt() != null && dto.getUnitPrice().getEd() != null){
                qw.between("unitPrice", dto.getUnitPrice().getSt(), dto.getUnitPrice().getEd());
            }else if(dto.getUnitPrice().getEd() != null){
                qw.le("unitPrice", dto.getUnitPrice().getEd());
            }else if(dto.getUnitPrice().getSt() != null){
                qw.ge("unitPrice", dto.getUnitPrice().getSt());
            }
        }
        // 判断工件数量的范围
        if (dto.getWorkPieceNum() != null) {
            if(dto.getWorkPieceNum().getSt() != null && dto.getWorkPieceNum().getEd() != null){
                qw.between("workPieceNum", dto.getWorkPieceNum().getSt(), dto.getWorkPieceNum().getEd());
            }else if(dto.getWorkPieceNum().getEd() != null){
                qw.le("workPieceNum", dto.getWorkPieceNum().getEd());
            }else if(dto.getWorkPieceNum().getSt() != null){
                qw.ge("workPieceNum", dto.getWorkPieceNum().getSt());
            }
        }

        if (dto.getPaymentStatus() != null && !dto.getPaymentStatus().isEmpty()) {
            qw.eq("paymentStatus", dto.getPaymentStatus());
        }

        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            qw.eq("status", dto.getStatus());
        }

        if (dto.getPayWay() != null && !dto.getPayWay().isEmpty()) {
            qw.eq("payWay", dto.getPayWay());
        }

        // ✅ 分页查询
        Page<orders> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        Page<orders> result = ordersservice.page(page, qw);


        HashMap<String,Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "success");
        map.put("data", result.getRecords());
        map.put("total", result.getTotal()); // 总记录数
        map.put("pageNum", result.getCurrent());
        map.put("pageSize", result.getSize());
        return map;
    }

}

