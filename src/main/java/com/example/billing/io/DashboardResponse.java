package com.example.billing.io;

import java.util.List;

public class DashboardResponse {
    private Double todaySales;
    private Long todayOrderCount;
    private List<OrderResponse> recentOrders;

    public Double getTodaySales() {
        return todaySales;
    }

    public void setTodaySales(Double todaySales) {
        this.todaySales = todaySales;
    }

    public Long getTodayOrderCount() {
        return todayOrderCount;
    }

    public void setTodayOrderCount(Long todayOrderCount) {
        this.todayOrderCount = todayOrderCount;
    }

    public List<OrderResponse> getRecentOrders() {
        return recentOrders;
    }

    public void setRecentOrders(List<OrderResponse> recentOrders) {
        this.recentOrders = recentOrders;
    }

    public DashboardResponse(Double todaySales, Long todayOrderCount, List<OrderResponse> recentOrders) {
        this.todaySales = todaySales;
        this.todayOrderCount = todayOrderCount;
        this.recentOrders = recentOrders;
    }
}
