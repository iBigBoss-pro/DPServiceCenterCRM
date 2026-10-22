package main.java.view;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * Карточка клиента для приёмщика. Заполняется SQL-запросом, не хранится в БД.
 */
public class ClientCard {

    private Long clientId;
    private String fullName;
    private String phoneNumber;
    private String email;

    private String status;          // ClientStatus.name()
    private boolean important;
    private String importanceNote;
    private BigDecimal discountPercent;

    private int totalOrders;
    private int activeOrders;
    private BigDecimal totalSpent;
    private BigDecimal totalProfit;
    private BigDecimal avgProfitPerOrder;
    private Instant firstVisitAt;
    private Instant lastVisitAt;

    private String lastDevice;      // «iPhone 13»
    private String lastMalfunction; // «замена экрана»

    // геттеры/сеттеры — опущены для краткости
}