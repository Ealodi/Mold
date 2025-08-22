package com.alee.mold.controller;

import com.alee.mold.entity.client;
import com.alee.mold.entity.payment;
import com.alee.mold.service.paymentService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@CrossOrigin
@RequestMapping("/payment")
public class paymentController extends BaseController {
    @Autowired
    private paymentService paymentservice;
    @GetMapping("/list")
    HashMap<String,Object> paymentList(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String orderId,
            @RequestParam(required = false) String clientName)
    {
        QueryWrapper<payment> qw = new QueryWrapper<payment>();
        if (clientName != null && !clientName.isEmpty()) {
            qw.eq("clientName", clientName);
        }
        if (orderId != null && !orderId.isEmpty()) {
            qw.eq("orderId", orderId);
        }
        qw.ne("deleted",1);
        // ✅ 分页查询
        Page<payment> page = new Page<>(pageNum, pageSize);
        Page<payment> result = paymentservice.page(page, qw);

        HashMap<String,Object> map = new HashMap<>();
        map.put("code", 200);
        map.put("msg", "success");
        map.put("data", result.getRecords()); // 当前页数据
        map.put("total", result.getTotal());  // 总条数
        map.put("pageNum", result.getCurrent());
        map.put("pageSize", result.getSize());
        return map;
    }
    @PostMapping("/add")
    HashMap<String,Object> paymentAdd(@RequestBody  payment pm){
        return paymentservice.save(pm) ? ok() : error();
    }
    @PostMapping("/update")
    HashMap<String,Object> paymentUpdate(@RequestBody  payment pm){
        return paymentservice.updateById(pm) ? ok() : error();
    }
    @GetMapping("/get")
    HashMap<String,Object> get(String id){
        return ok(paymentservice.getById(id));
    }
    @GetMapping("/remove")
    HashMap<String,Object> paymentRemove(String id){
        return paymentservice.removeById(id) ? ok() : error();
    }
}
