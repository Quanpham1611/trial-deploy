package quan.dodomilktea.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quan.dodomilktea.dto.admin.AccountRequestDto;
import quan.dodomilktea.dto.customer.MessageDto;
import quan.dodomilktea.model.Account;
import quan.dodomilktea.service.AccountService;


import java.util.List;

@RestController
@RequestMapping("/admin/account")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminAccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAllAccount(){
        List<Account> accounts = accountService.getAllAccount();
        if(accounts.isEmpty()){
            throw new NullPointerException();
        }
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/insert")
    public ResponseEntity<Object> insertAccount(@RequestBody AccountRequestDto accountRequestDto){
        Account account = accountService.insertAccount(accountRequestDto);
        if(account == null){
            return ResponseEntity.ok(new MessageDto("Insert unsuccessfull"));
        }
        return ResponseEntity.ok(account);
    }

    @PutMapping("/update/{accountId}")
    public ResponseEntity<Object> updateMilktea(@RequestBody AccountRequestDto accountRequestDto, @PathVariable String accountId){
        Account account = accountService.updateAccount(accountRequestDto, accountId);
        if(account == null){
            return ResponseEntity.ok(new MessageDto("Update unsuccessful"));
        }
        return ResponseEntity.ok(account);
    }

    @PutMapping("/delete/{accountId}")
    public ResponseEntity<Object> deleteAccount(@PathVariable String accountId){
        boolean isDeleted = accountService.deleteAccount(accountId);
        if(isDeleted){
            return ResponseEntity.ok(new MessageDto("Delete successfull"));
        }
        return ResponseEntity.ok(new MessageDto("Delete unsuccessfull"));
    }
}
