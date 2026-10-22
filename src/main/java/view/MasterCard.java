package main.java.view;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Карточка мастера. Заполняется SQL-запросом, не хранится в БД.
 */
public class MasterCard {

    private Long masterId;
    private String fullName;
    private String role;

    private int completedOrders;   // заказы, где он masterId
    private int activeOrders;      // заказы в работе, где он masterId
    private int completedWorks;    // работы из OrderWork
    private BigDecimal totalRevenue;
    private BigDecimal avgRevenuePerOrder;
    private Instant lastCompletedAt;

    private String topWork;        // самая частая работа

    // геттеры/сеттеры
}