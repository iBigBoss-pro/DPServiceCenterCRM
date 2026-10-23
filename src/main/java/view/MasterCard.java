package view;

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

    public Long getMasterId() { return masterId; }
    public void setMasterId(Long masterId) { this.masterId = masterId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public int getCompletedOrders() { return completedOrders; }
    public void setCompletedOrders(int completedOrders) { this.completedOrders = completedOrders; }
    public int getActiveOrders() { return activeOrders; }
    public void setActiveOrders(int activeOrders) { this.activeOrders = activeOrders; }
    public int getCompletedWorks() { return completedWorks; }
    public void setCompletedWorks(int completedWorks) { this.completedWorks = completedWorks; }
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    public BigDecimal getAvgRevenuePerOrder() { return avgRevenuePerOrder; }
    public void setAvgRevenuePerOrder(BigDecimal avgRevenuePerOrder) { this.avgRevenuePerOrder = avgRevenuePerOrder; }
    public Instant getLastCompletedAt() { return lastCompletedAt; }
    public void setLastCompletedAt(Instant lastCompletedAt) { this.lastCompletedAt = lastCompletedAt; }
    public String getTopWork() { return topWork; }
    public void setTopWork(String topWork) { this.topWork = topWork; }
}
