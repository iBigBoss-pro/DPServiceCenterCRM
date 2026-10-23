package view;

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

        public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public boolean isImportant() { return important; }
    public void setImportant(boolean important) { this.important = important; }
    public String getImportanceNote() { return importanceNote; }
    public void setImportanceNote(String importanceNote) { this.importanceNote = importanceNote; }
    public BigDecimal getDiscountPercent() { return discountPercent; }
    public void setDiscountPercent(BigDecimal discountPercent) { this.discountPercent = discountPercent; }
    public int getTotalOrders() { return totalOrders; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }
    public int getActiveOrders() { return activeOrders; }
    public void setActiveOrders(int activeOrders) { this.activeOrders = activeOrders; }
    public BigDecimal getTotalSpent() { return totalSpent; }
    public void setTotalSpent(BigDecimal totalSpent) { this.totalSpent = totalSpent; }
    public BigDecimal getTotalProfit() { return totalProfit; }
    public void setTotalProfit(BigDecimal totalProfit) { this.totalProfit = totalProfit; }
    public BigDecimal getAvgProfitPerOrder() { return avgProfitPerOrder; }
    public void setAvgProfitPerOrder(BigDecimal avgProfitPerOrder) { this.avgProfitPerOrder = avgProfitPerOrder; }
    public Instant getFirstVisitAt() { return firstVisitAt; }
    public void setFirstVisitAt(Instant firstVisitAt) { this.firstVisitAt = firstVisitAt; }
    public Instant getLastVisitAt() { return lastVisitAt; }
    public void setLastVisitAt(Instant lastVisitAt) { this.lastVisitAt = lastVisitAt; }
    public String getLastDevice() { return lastDevice; }
    public void setLastDevice(String lastDevice) { this.lastDevice = lastDevice; }
    public String getLastMalfunction() { return lastMalfunction; }
    public void setLastMalfunction(String lastMalfunction) { this.lastMalfunction = lastMalfunction; }
}