package com.Chinmay.MoneyManager.ServiceImpl;

import com.Chinmay.MoneyManager.IO.IncomeDTO;
import com.Chinmay.MoneyManager.Model.CategoryEntity;
import com.Chinmay.MoneyManager.Model.IncomeEntity;
import com.Chinmay.MoneyManager.Model.ProfileEntity;
import com.Chinmay.MoneyManager.Repository.CategoryEntityRepository;
import com.Chinmay.MoneyManager.Repository.IncomeRepository;
import com.Chinmay.MoneyManager.Service.IncomeService;
import com.Chinmay.MoneyManager.Service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {

    private final CategoryEntityRepository categoryEntityRepository;
    private final IncomeRepository incomeRepository;
    private final ProfileService profileService;

    @Override
    public IncomeEntity toEntity(IncomeDTO incomeDTO, ProfileEntity profileEntity, CategoryEntity categoryEntity) {
        return IncomeEntity.builder()
                .name(incomeDTO.getName())
                .amount(incomeDTO.getAmount())
                .date(incomeDTO.getDate())
                .iconUrl(incomeDTO.getIconUrl())
                .category(categoryEntity)
                .profile(profileEntity)
                .build();
    }

    @Override
    public IncomeDTO toDTO(IncomeEntity incomeEntity) {
        return IncomeDTO.builder()
                .id(incomeEntity.getId())
                .name(incomeEntity.getName())
                .iconUrl(incomeEntity.getIconUrl())
                .categoryName(incomeEntity.getCategory()!=null?incomeEntity.getCategory().getCategoryName():null)
                .categoryId(incomeEntity.getCategory()!=null?incomeEntity.getCategory().getId():null)
                .amount(incomeEntity.getAmount())
                .date(incomeEntity.getDate())
                .build();
    }


    @Override
    public IncomeDTO addIncome(IncomeDTO incomeDTO) {
        ProfileEntity profile= profileService.getCurrentProfile();
        CategoryEntity category= categoryEntityRepository.findById(incomeDTO.getCategoryId()).orElseThrow(()->new RuntimeException("Category Not found"));
        IncomeEntity newIncome= toEntity(incomeDTO,profile,category);
        newIncome= incomeRepository.save(newIncome);
        return toDTO(newIncome);
    }

    public List<IncomeDTO> getCurrentMonthIncomesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        LocalDate now = LocalDate.now();
        LocalDate startDate = now.withDayOfMonth(1);
        LocalDate endDate = now.withDayOfMonth(now.lengthOfMonth());
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetween(profile.getId(), startDate, endDate);
        return list.stream().map(this::toDTO).toList();
    }

    public void deleteIncome(Long incomeId) {
        ProfileEntity profile = profileService.getCurrentProfile();
        IncomeEntity entity = incomeRepository.findById(incomeId)
                .orElseThrow(() -> new RuntimeException("Income not found"));
        if (!entity.getProfile().getId().equals(profile.getId())) {
            throw new RuntimeException("Unauthorized to delete this income");
        }
        incomeRepository.delete(entity);
    }

    // Get latest 5 incomes for current user
    public List<IncomeDTO> getLatest5IncomesForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDTO).toList();
    }

    // Get total incomes for current user
    public BigDecimal getTotalIncomeForCurrentUser() {
        ProfileEntity profile = profileService.getCurrentProfile();
        BigDecimal total = incomeRepository.findTotalExpenseByProfileId(profile.getId());
        return total != null ? total: BigDecimal.ZERO;
    }

    //filter incomes
    public List<IncomeDTO> filterIncomes(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(profile.getId(), startDate, endDate, keyword, sort);
        return list.stream().map(this::toDTO).toList();
    }

}
