package quan.dodomilktea.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import quan.dodomilktea.model.Account;
import quan.dodomilktea.repo.AccountRepository;
@Component
public class CustomAccountDetailService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account accountEmail = accountRepository.findByEmail(username);
        if(accountEmail == null){
            throw new UsernameNotFoundException("Account not found");
        }
        return new CustomAccountDetail(accountEmail);
    }
}
