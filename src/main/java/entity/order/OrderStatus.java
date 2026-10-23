package entity.order;

public enum OrderStatus {
    NEW,                 // Новый
    ACCEPTED,            // Принят
    DIAGNOSTICS,         // Диагностика   ← добавлено
    AWAITING_APPROVAL,   // Ждёт согласования с клиентом
    AWAITING_PARTS,      // Ждёт запчасти
    IN_REPAIR,           // В ремонте
    COMPLETED,           // Выполнен
    READY_FOR_PICKUP,    // Готов к выдаче
    COMPLETED_AND_PAID,  // Выполнен и оплачен
    CANCELLED,           // Отменён
    RETURNED             // Возврат по гарантии
}