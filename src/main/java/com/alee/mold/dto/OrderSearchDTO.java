package com.alee.mold.dto;

import com.alee.mold.common.Range;
import java.util.Date;
import java.util.List;

public class OrderSearchDTO {
    private String orderId;
    private String clientName;
    private List<Date> dateRange;
    private Range<Double> unitPrice;
    private Range<Integer> workPieceNum;
    private String paymentStatus;
    private String status;
    private String payWay;
    private Integer pageNum = 1;   // 当前页，默认第1页
    private Integer pageSize = 10; // 每页条数，默认10
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<Date> getDateRange() {
        return dateRange;
    }

    public void setDateRange(List<Date> dateRange) {
        this.dateRange = dateRange;
    }

    public Range<Double> getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Range<Double> unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Range<Integer> getWorkPieceNum() {
        return workPieceNum;
    }

    public void setWorkPieceNum(Range<Integer> workPieceNum) {
        this.workPieceNum = workPieceNum;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
