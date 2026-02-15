package com.Chinmay.MoneyManager.Service;

import com.Chinmay.MoneyManager.IO.ExpenseDTO;
import com.Chinmay.MoneyManager.Model.CategoryEntity;
import com.Chinmay.MoneyManager.Model.ExpenseEntity;
import com.Chinmay.MoneyManager.Model.ProfileEntity;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseService {

    ExpenseEntity toEntity(ExpenseDTO expenseDTO, ProfileEntity profileEntity, CategoryEntity categoryEntity);
    ExpenseDTO toDTO(ExpenseEntity expenseEntity);
    ExpenseDTO addExpense(ExpenseDTO expenseDTO);
    List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser();
    void deleteExpense(Long expenseId);
    List<ExpenseDTO> getLatest5ExpensesForCurrentUser();
    BigDecimal getTotalExpenseForCurrentUser();
    List<ExpenseDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort);
    //Notifcation
    List<ExpenseDTO> getExpensesForUserOnDate(Long profileId, LocalDate date);
}
