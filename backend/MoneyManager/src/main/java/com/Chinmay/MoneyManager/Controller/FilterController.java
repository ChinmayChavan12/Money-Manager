package com.Chinmay.MoneyManager.Controller;


import com.Chinmay.MoneyManager.IO.ExpenseDTO;
import com.Chinmay.MoneyManager.IO.FilterDTO;
import com.Chinmay.MoneyManager.IO.IncomeDTO;
import com.Chinmay.MoneyManager.Service.ExpenseService;
import com.Chinmay.MoneyManager.Service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {

    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<?> filterTransaction(@RequestBody FilterDTO filterDTO) {
            LocalDate startDate=filterDTO.getStartDate()!=null?filterDTO.getStartDate():LocalDate.MIN;
            LocalDate endDate=filterDTO.getEndDate()!=null?filterDTO.getEndDate():LocalDate.now();
            String keyword = filterDTO.getKeyword()!=null?filterDTO.getKeyword():"";
            String sortField = filterDTO.getSortField()!=null?filterDTO.getSortField():"date";
            Sort.Direction direction="desc".equalsIgnoreCase(sortField)?Sort.Direction.DESC:Sort.Direction.ASC;
            Sort sort=Sort.by(direction,sortField);
            if("income".equals(filterDTO.getType())){
                List<IncomeDTO> incomes = incomeService.filterIncomes(startDate,endDate,keyword,sort);
                return ResponseEntity.ok(incomes);
            }else if("expense".equals(filterDTO.getType())){
                List<ExpenseDTO> expenses=expenseService.filterExpenses(startDate,endDate,keyword,sort);
                return ResponseEntity.ok(expenses);
            }else{
                return  ResponseEntity.badRequest().body("Invalid filter type.Must be Income or Expense");
            }
    }



}
