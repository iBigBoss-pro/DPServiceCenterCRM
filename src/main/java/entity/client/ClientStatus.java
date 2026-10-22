package main.java.entity.client;

/**
 * Статус клиента. NEW/REGULAR/VIP — авто по правилам (если не manualOverride).
 * PROBLEM/BLACKLIST — только вручную (ADMIN/DIRECTOR).
 */
public enum ClientStatus {
    NEW,        // Первый раз пришёл
    REGULAR,    // Постоянный (N заказов)
    VIP,        // Ценный по накопленной ПРИБЫЛИ
    PROBLEM,    // Проблемный (жалобы, споры)
    BLACKLIST   // Не обслуживаем
}