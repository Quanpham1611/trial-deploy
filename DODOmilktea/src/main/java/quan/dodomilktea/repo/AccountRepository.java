package quan.dodomilktea.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import quan.dodomilktea.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    @Query(value = "SELECT * FROM account acc WHERE acc.email = ?1 and acc.enabled = true" , nativeQuery = true)
    Account findByEmail(String email);

    @Query(value = "SELECT * FROM account acc WHERE acc.acc_id = ?1 and acc.enabled = true" , nativeQuery = true)
    Optional<Account> findById(String id);

    @Query(value = "SELECT * FROM account acc WHERE acc.enabled = true" , nativeQuery = true)
    List<Account> findAll();
}
