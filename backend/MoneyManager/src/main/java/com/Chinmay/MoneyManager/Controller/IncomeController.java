package com.Chinmay.MoneyManager.Controller;


import com.Chinmay.MoneyManager.IO.IncomeDTO;
import com.Chinmay.MoneyManager.Service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public ResponseEntity<IncomeDTO> addIncome(@RequestBody IncomeDTO incomeDTO) {
        IncomeDTO saved= incomeService.addIncome(incomeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<IncomeDTO>> getExpenses() {
        List<IncomeDTO> expenses = incomeService.getCurrentMonthIncomesForCurrentUser();
        return ResponseEntity.ok(expenses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }

}
