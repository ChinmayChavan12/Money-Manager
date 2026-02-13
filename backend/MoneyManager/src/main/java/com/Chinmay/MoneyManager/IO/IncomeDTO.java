package com.Chinmay.MoneyManager.IO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IncomeDTO {
    private Long id;
    private String name;
    private String iconUrl;
    private String categoryName;
    private Long categoryId;
    private BigDecimal amount;
    private LocalDate date;


}
