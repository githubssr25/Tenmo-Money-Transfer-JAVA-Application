package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
@RestController
public class TransferController {

    private final TransferDao dao;


    public TransferController(TransferDao dao) {
        this.dao = dao;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfers", method = RequestMethod.POST)
    public Transfer createTransfer(@RequestBody @Valid Transfer transfer) {
        return dao.createTransfer(transfer);
    }

    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
    public Transfer getTransfer(@PathVariable int id) {
        return dao.getTransfer(id);
    }


    @RequestMapping(path = "/transfers/transfer_type/transfer_type_id", method = RequestMethod.GET)
    public List<Transfer> searchByTransferType( @PathVariable int id) {return dao.searchByTransferType(id);

    }

    @RequestMapping(path = "/transfers/users/{id}/{accountType}", method = RequestMethod.GET)
    public Transfer[] getTransfersByUserId(@PathVariable int id, @PathVariable int accountType) {
        return dao.getTransfersByUserId(id, accountType);
    }

    @RequestMapping(path = "/transfers/sending", method = RequestMethod.POST)
    public String sendTransferTo(@PathVariable int user_From, @PathVariable int user_To, @PathVariable BigDecimal amount){
        return dao.sendTransferTo(user_From, user_To, amount);
    }





}
