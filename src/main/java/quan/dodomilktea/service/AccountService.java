package quan.dodomilktea.service;

import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import quan.dodomilktea.dto.admin.AccountRequestDto;
import quan.dodomilktea.globalenum.ERoles;
import quan.dodomilktea.jwt.JwtTokenUlti;
import quan.dodomilktea.model.Account;
import quan.dodomilktea.repo.AccountRepository;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
@Service
@Transactional
public class AccountService {
    @Autowired
    AccountRepository accountRepo;
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenUlti jwtTokenUtil;
    Logger logger = LoggerFactory.getLogger(AccountService.class);

    public void generateOneTimePassword(Account accounts) throws UnsupportedEncodingException, MessagingException {
        String OTP = RandomString.make(8);
        String encodedOTP = passwordEncoder.encode(OTP);

        accounts.setOneTimePassword(encodedOTP);
        accounts.setOtpRequestedTime(new Date());

        accountRepo.save(accounts);
        sendOTPEmail(accounts, OTP);
    }

    public void sendOTPEmail(Account accounts, String OTP) throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("contact@gmail.com","MilkTea Support");
        helper.setTo(accounts.getEmail());

        String subject = "Here's your One Time Password (OTP) - Expire in 5 minutes!";

        String content = "<p>Hello " + accounts.getName() + "</p>"
                + "<p>For security reason, you're required to use the following "
                + "One Time Password to login:</p>"
                + "<p><b>" + OTP + "</b></p>"
                + "<br>"
                + "<p>Note: this OTP is set to expire in 5 minutes.</p>";

        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    public Account clearOTP(Account accounts) {
        accounts.setOneTimePassword(null);
        accounts.setOtpRequestedTime(null);
        return accountRepo.save(accounts);
    }

    public void createAccount(Account account) {
        account.setRole(ERoles.CUSTOMER.toString());
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        account.setCreated_at(new Date());
        account.setEnabled(true);
        Account savedAcc =  accountRepo.save(account);
        System.out.println(savedAcc);
        accountRepo.save(account);
    }

    public boolean checkAccountExisted(String email) {
        if (accountRepo.findByEmail(email) != null) {
            return true;
        }
        return false;
    }

    public Account getAccById(String id) {
        return accountRepo.findById(id).orElseThrow(() -> new NullPointerException());
    }

    public Account getAccByRequest(HttpServletRequest request) {
        //Check token with acc_id
        String token = jwtTokenUtil.getAccessToken(request);
        String[] subjectArray = jwtTokenUtil.getSubject(token).split(",");
        Account account = getAccById(subjectArray[0]);
        return account;
    }

    public Account updateAccount(Account account) {
        return accountRepo.save(account);
    }

    public Account getAccountByEmail(String email) {
        return accountRepo.findByEmail(email);
    }

    public List<Account> getAllAccount() {
        return accountRepo.findAll();
    }

    public Account insertAccount(AccountRequestDto accountInsert) {
        // Check email is exist or not ?
        List<Account> accounts = getAllAccount();
        for (Account account : accounts) {
            if(account.getEmail().equalsIgnoreCase(accountInsert.getEmail())) {
                return null;
            }
        }
        Account account = new Account();
        account.setEmail(accountInsert.getEmail());
        account.setPassword(accountInsert.getPassword());
        account.setRole(accountInsert.getRole());
        account.setName(accountInsert.getName());
        account.setPhone(accountInsert.getPhone());
        account.setAddress(accountInsert.getAddress());
        account.setCreated_at(new Date());
        account.setEnabled(true);

        try {
            account = accountRepo.save(account);
        } catch(Exception e) {
            logger.error(e.getMessage());
            account = null;
        }
        return account;
    }

    public Account updateAccount(AccountRequestDto accountUpdate, String id) {
        Account accountResponse = accountRepo.findById(id).orElse(null);
        if(accountResponse == null || accountResponse.isEnabled() == false) { return null; }

        // Check name is exist or not ?
        List<Account> accounts = getAllAccount();
        for (Account account : accounts) {
            if(account.getEmail().equalsIgnoreCase(accountUpdate.getEmail())) {
                if(account.getEmail().equalsIgnoreCase(accountResponse.getEmail()))
                    continue;
                return null;
            }
        }

        accountResponse.setEmail(accountUpdate.getEmail());
        accountResponse.setPassword(accountUpdate.getPassword());
        accountResponse.setRole(accountUpdate.getRole());
        accountResponse.setName(accountUpdate.getName());
        accountResponse.setPhone(accountUpdate.getPhone());
        accountResponse.setAddress(accountUpdate.getAddress());

        try {
            accountResponse = accountRepo.save(accountResponse);
        } catch(Exception e) {
            logger.error(e.getMessage());
            accountResponse = null;
        }
        return accountResponse;
    }

    public boolean deleteAccount(String id) {
        Account accountDelete = accountRepo.findById(id).orElse(null);
        if(accountDelete == null || accountDelete.isEnabled() == false) { return false; }

        accountDelete.setEnabled(false);
        try {
            accountRepo.save(accountDelete);
        } catch(Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }
}
