package entity.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Утилита для работы с деньгами. Везде используется BigDecimal со scale 2 (копейки).
 * Хранить деньги в long нельзя — ошибки округления всплывут на первом же заказе с копейками.
 */
public final class Money {

    public static final int SCALE = 2;

    private Money() {}

    public static BigDecimal of(BigDecimal value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " обязательна");
        }
        if (value.signum() < 0) {
            throw new IllegalArgumentException(fieldName + " не может быть отрицательной");
        }
        return value.setScale(SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal zero() {
        return BigDecimal.ZERO.setScale(SCALE);
    }
}