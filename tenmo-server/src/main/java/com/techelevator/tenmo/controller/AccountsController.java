package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.model.Account;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
@RestController
public class AccountsController {


    private final AccountDao dao;


    public AccountsController(AccountDao dao) {
        this.dao = dao;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/accounts", method = RequestMethod.POST)
    public Account createAccount(@RequestBody @Valid Account account) {
        return dao.createAccount(account);
    }

    @GetMapping(path = "/accounts/{id}")
    public Account getAccount(@PathVariable int id) {
        return dao.getAccount(id);
    }

    @GetMapping(path = "/accounts/user/{id}/")
    public long getUserAccountIdByUserId(@PathVariable long id) {
        return dao.findAccountIdByUserId(id);
    }

    @GetMapping(path = "/accounts/user/full/{id}")
    public Account getUserAccountUserId(@PathVariable long id) {
        return getAccount((int) dao.findAccountIdByUserId(id));
    }

    @PatchMapping(path = "/accounts/{id}")
    public void updateAccount(@RequestBody @Valid Account account, @PathVariable int id) {
        dao.updateAccount(account, id);
    }

    @GetMapping(path = "/accounts/{id}/balance")
    public BigDecimal getAccountBalance(@PathVariable int id) {
        return dao.getAccountBalance(id);
    }

    @PatchMapping(path = "/accounts/{id}/receiving")
    public BigDecimal increaseBalance(@PathVariable BigDecimal addedAmount, @PathVariable int id){
       return dao.increaseBalance(addedAmount, id);
    }

    @PatchMapping(path = "/accounts/{id}/sending")
    public BigDecimal decreaseBalance(@PathVariable BigDecimal subtractedAmount, @PathVariable int id){
        return dao.decreaseBalance(subtractedAmount, id);
    }


}




