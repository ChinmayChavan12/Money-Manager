package com.Chinmay.MoneyManager.Service;

import com.Chinmay.MoneyManager.IO.ExpenseDTO;
import com.Chinmay.MoneyManager.Model.CategoryEntity;
import com.Chinmay.MoneyManager.Model.ExpenseEntity;
import com.Chinmay.MoneyManager.Model.ProfileEntity;

import java.util.List;

public interface ExpenseService {

    ExpenseEntity toEntity(ExpenseDTO expenseDTO, ProfileEntity profileEntity, CategoryEntity categoryEntity);
    ExpenseDTO toDTO(ExpenseEntity expenseEntity);
    ExpenseDTO addExpense(ExpenseDTO expenseDTO);
    List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser();
}
