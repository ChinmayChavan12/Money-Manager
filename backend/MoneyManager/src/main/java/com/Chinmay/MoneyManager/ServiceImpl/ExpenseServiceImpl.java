package com.Chinmay.MoneyManager.ServiceImpl;

import com.Chinmay.MoneyManager.IO.ExpenseDTO;
import com.Chinmay.MoneyManager.Model.CategoryEntity;
import com.Chinmay.MoneyManager.Model.ExpenseEntity;
import com.Chinmay.MoneyManager.Model.ProfileEntity;
import com.Chinmay.MoneyManager.Repository.CategoryEntityRepository;
import com.Chinmay.MoneyManager.Repository.ExpenseRepository;

import com.Chinmay.MoneyManager.Service.ExpenseService;
import com.Chinmay.MoneyManager.Service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final CategoryEntityRepository categoryEntityRepository;
    private final ProfileService profileService;

    @Override
    public ExpenseEntity toEntity(ExpenseDTO expenseDTO, ProfileEntity profileEntity, CategoryEntity categoryEntity) {
        return ExpenseEntity.builder()
                .name(expenseDTO.getName())
                .amount(expenseDTO.getAmount())
                .iconUrl(expenseDTO.getIconUrl())
                .date(expenseDTO.getDate())
                .category(categoryEntity)
                .profile(profileEntity)
                .build();
    }

    @Override
    public ExpenseDTO toDTO(ExpenseEntity expenseEntity) {
       return  ExpenseDTO.builder()
                .id(expenseEntity.getId())
                .name(expenseEntity.getName())
                .iconUrl(expenseEntity.getIconUrl())
                .categoryName(expenseEntity.getCategory()!=null?expenseEntity.getCategory().getCategoryName():null)
                .categoryId(expenseEntity.getCategory()!=null?expenseEntity.getCategory().getId():null)
                .amount(expenseEntity.getAmount())
                .date(expenseEntity.getDate())
                .build();
    }

    @Override
    public ExpenseDTO addExpense(ExpenseDTO expenseDTO) {
        ProfileEntity profile= profileService.getCurrentProfile();
        CategoryEntity category= categoryEntityRepository.findById(expenseDTO.getCategoryId()).orElseThrow(()->new RuntimeException("Category Not found"));
        ExpenseEntity newExpense= toEntity(expenseDTO,profile,category);
        newExpense= expenseRepository.save(newExpense);
        return toDTO(newExpense);
    }

    @Override
    public List<ExpenseDTO> getCurrentMonthExpensesForCurrentUser() {
        ProfileEntity profile= profileService.getCurrentProfile();
        LocalDate now= LocalDate.now();
        LocalDate startDate=  now.withDayOfMonth(1);
        LocalDate endDate= now.withDayOfMonth(now.lengthOfMonth());
        List<ExpenseEntity>  expenses= expenseRepository.findByProfileIdAndDateBetween(profile.getId(),startDate,endDate);
        return expenses.stream().map(this::toDTO).toList();
    }

    public void deleteExpense(Long expenseId) {
        ProfileEntity profile = profileService.getCurrentProfile();
        ExpenseEntity entity = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found"));
        if (!entity.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Unauthorized to delete this expense");
        }
        expenseRepository.delete(entity);
    }

    // Get latest 5 expenses for current user
    public List<ExpenseDTO> getLatest5ExpensesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> list = expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDTO).toList();
    }

    // Get total expenses for current user
    public BigDecimal getTotalExpenseForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = expenseRepository.findTotalExpenseByProfileId(profile.getId());
        return total != null ? total: BigDecimal.ZERO;
    }

    //filter expenses
    public List<ExpenseDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword, sort);
        return list.stream().map(this::toDTO).toList();
    }

    @Override
    public List<ExpenseDTO> getExpensesForUserOnDate(Long profileId, LocalDate date) {
        List<ExpenseEntity> list= expenseRepository.findByProfileIdAndDate(profileId, date);
       return  list.stream().map(this::toDTO).toList();
    }


}
