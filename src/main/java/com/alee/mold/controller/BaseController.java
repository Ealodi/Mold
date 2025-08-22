package com.alee.mold.controller;
import java.util.HashMap;

// 抽象类基础controller
public abstract class BaseController {
    public HashMap<String,Object> ok(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "OK");
        result.put("data",null);
        return result;
    }
    public HashMap<String,Object> ok(Object data){
        HashMap<String,Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", "OK");
        result.put("data",data);
        return result;
    }
    public HashMap<String,Object> ok(String msg,Object data){
        HashMap<String,Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("msg", msg);
        result.put("data",data);
        return result;
    }
    public HashMap<String,Object> error(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("code", 500);
        result.put("msg", "未知错误，请稍后再试");
        result.put("data",null);
        return result;
    }
    public HashMap<String,Object> error(String msg,Object data){
        HashMap<String,Object> result = new HashMap<>();
        result.put("code", 500);
        result.put("msg", msg);
        result.put("data",data);
        return result;
    }
    public HashMap<String,Object> error(Object data){
        HashMap<String,Object> result = new HashMap<>();
        result.put("code", 500);
        result.put("msg", "未知错误，请稍后再试");
        result.put("data",data);
        return result;
    }
}
