package app.finplan.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionFilter (
        Long accountId,
        Long categoryId,
        String name,
        String type,
        BigDecimal amountFrom,
        BigDecimal amountTo,
        LocalDateTime createdAtFrom,
        LocalDateTime createdAtTo
) {
}
