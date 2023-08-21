package quan.dodomilktea.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import quan.dodomilktea.model.CustomMilkTea;

@Repository
public interface CustomMilkTeaRepository extends JpaRepository<CustomMilkTea, String> {
    @Query(value = "SELECT * FROM custom_milk_tea cmt WHERE cmt.order_id = ?1 and cmt.enabled=true", nativeQuery = true)
    List<CustomMilkTea> findByCartId(String cartId);

    @Query(value = "SELECT * FROM custom_milk_tea cmt WHERE cmt.custom_milk_tea_id = ?1 and cmt.enabled=true", nativeQuery = true)
    Optional<CustomMilkTea> findById(String id);

    @Query(value = "SELECT * FROM custom_milk_tea cmt WHERE cmt.enabled=true", nativeQuery = true)
    List<CustomMilkTea> findAll();

    @Query(value = "SELECT * FROM custom_milk_tea cmt WHERE cmt.order_id = ?1 and cmt.enabled=true", nativeQuery = true)
    List<CustomMilkTea> findByOrderId(String id);
}