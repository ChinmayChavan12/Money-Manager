package com.Chinmay.MoneyManager.Service;


import com.Chinmay.MoneyManager.IO.IncomeDTO;
import com.Chinmay.MoneyManager.Model.CategoryEntity;
import com.Chinmay.MoneyManager.Model.IncomeEntity;
import com.Chinmay.MoneyManager.Model.ProfileEntity;

public interface IncomeService {

    IncomeEntity toEntity(IncomeDTO incomeDTO, ProfileEntity profileEntity, CategoryEntity categoryEntity);
    IncomeDTO toDTO(IncomeEntity incomeEntity);
    IncomeDTO addIncome(IncomeDTO incomeDTO);
}
