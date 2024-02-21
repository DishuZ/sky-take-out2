package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时任务类，定时处理订单状态
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单的方法
     */
//    @Scheduled(cron = "0 * * * * ?")  // 每分钟触发一次
    @Scheduled(cron = "* 0/10 * * * ?")  // 每10分钟触发一次
    public void processTimeoutOrder() {
        log.info("定时处理超时订单：{}", LocalDateTime.now());

        // 查询超时订单(当前时间 - 15 分钟)
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));
        // 修改超时订单状态
        if (ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("订单超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    /**
     * 处理一直处于派送中状态的订单
     */
    @Scheduled(cron = "0 0 1 * * ?")  // 每天凌晨 1 点触发一次
    public void processDeliveryOrder() {
        log.info("定时处理直处于派送中状态的订单：{}", LocalDateTime.now());

        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusMinutes(-60));
        if (ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orderMapper.update(orders);
            }
        }
    }
}
