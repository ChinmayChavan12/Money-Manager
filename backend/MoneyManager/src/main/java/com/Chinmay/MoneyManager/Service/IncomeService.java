package com.Chinmay.MoneyManager.Service;


import com.Chinmay.MoneyManager.IO.IncomeDTO;
import com.Chinmay.MoneyManager.Model.CategoryEntity;
import com.Chinmay.MoneyManager.Model.IncomeEntity;
import com.Chinmay.MoneyManager.Model.ProfileEntity;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeService {

    IncomeEntity toEntity(IncomeDTO incomeDTO, ProfileEntity profileEntity, CategoryEntity categoryEntity);
    IncomeDTO toDTO(IncomeEntity incomeEntity);
    IncomeDTO addIncome(IncomeDTO incomeDTO);
    List<IncomeDTO> getCurrentMonthIncomesForCurrentUser();
    void deleteIncome(Long incomeId);
    List<IncomeDTO> getLatest5IncomesForCurrentUser();
    BigDecimal getTotalIncomeForCurrentUser();
    List<IncomeDTO> filterIncomes(LocalDate startDate, LocalDate endDate, String keyword, Sort sort);
}
