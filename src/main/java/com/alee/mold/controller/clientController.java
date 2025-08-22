package com.alee.mold.controller;

import com.alee.mold.entity.client;
import com.alee.mold.entity.orders;
import com.alee.mold.service.clientService;
import com.alee.mold.service.ordersService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.UpdateById;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/client")
public class clientController extends BaseController{
    @Autowired
    private clientService clientservice;

    @GetMapping("/list")
    public HashMap<String,Object> clientlist(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String clientName,
            @RequestParam(required = false) String phoneNumber){
        QueryWrapper<client> qw = new QueryWrapper<client>();
        if (clientName != null && !clientName.isEmpty()) {
            qw.like("clientName", clientName); // 模糊匹配客户名
        }
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            qw.like("phoneNumber", phoneNumber); // 模糊匹配电话
        }
        qw.ne("deleted",1);
        // ✅ 分页查询
        Page<client> page = new Page<>(pageNum, pageSize);
        Page<client> result = clientservice.page(page, qw);

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
    public HashMap<String,Object> add(@RequestBody client clt){
        HashMap<String,Object> map = new HashMap<>();
        boolean save =  clientservice.save(clt);
        if(save) {
            map.put("code",200);
            map.put("msg","success");
            map.put("data",clt);
        }else {
            map.put("code",500);
            map.put("msg","error");
            map.put("data",null);
        }
        return map;
    }
    @PostMapping("/update")
    public HashMap<String,Object> update(@RequestBody client clt){
        return clientservice.updateById(clt) ? ok() : error("修改失败",null);
    }
    @GetMapping("/get")
    public HashMap<String,Object> get(Long clientId){
        return ok(clientservice.getById(clientId));
    }
    @GetMapping("/remove")
    public HashMap<String,Object> remove(Long clientId){
        boolean save =  clientservice.removeById(clientId);
        if(save) {
            return ok();
        }
        else return error();
    }
}
