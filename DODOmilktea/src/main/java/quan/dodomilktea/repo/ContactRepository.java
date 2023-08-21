package quan.dodomilktea.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import quan.dodomilktea.model.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
    @Query(value = "SELECT * FROM contact c WHERE c.contact_id=?1 and c.enabled = true" , nativeQuery = true)
    Optional<Contact> findById(String id);

    @Query(value = "SELECT * FROM contact c WHERE c.enabled = true" , nativeQuery = true)
    List<Contact> findAll();
}
