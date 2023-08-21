package quan.dodomilktea.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import quan.dodomilktea.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    @Query(value = "SELECT * FROM ordering o WHERE o.order_state = ?1 and o.customer_id = ?2 and o.enabled = true", nativeQuery = true)
    Order getOrderWithStateCustomer(String state, String id);

    @Query(value = "SELECT * FROM ordering o WHERE o.order_state != ?1 and o.customer_id = ?2 and o.enabled = true", nativeQuery = true)
    List<Order> getOrdersWithoutStateCustomer(String state, String id);

    @Query(value = "SELECT * FROM ordering o WHERE o.order_state = ?1 and o.enabled = true", nativeQuery = true)
    List<Order> getOrderWithStateStaffManager(String state);

    @Query(value = "SELECT * FROM ordering o WHERE o.order_id = ?1 and o.enabled = true" , nativeQuery = true)
    Optional<Order> findById(String id);

    @Query(value = "SELECT * FROM ordering o WHERE o.enabled = true" , nativeQuery = true)
    List<Order> findAll();

    @Query(value = "SELECT * FROM ordering o WHERE o.order_id = ?1 and o.customer_id = ?2 and o.enabled = true" , nativeQuery = true)
    Optional<Order> findOrderByCustomerId(String orderId, String customerId);

    @Query(value = "select * from ordering o where o.order_state = ?1 and o.enabled = true", nativeQuery = true)
    List<Order> findByState(String state);

    @Query(value = "SELECT * FROM ordering o WHERE o.order_state != ?1 and o.enabled = true", nativeQuery = true)
    List<Order> findOrderOrdered(String state);

    @Query(value = "SELECT * FROM ordering o WHERE DATE(o.order_date) = STR_TO_DATE(?1,'%Y-%m-%d') and o.enabled = true", nativeQuery = true)
    List<Order> findByDate(String date);
}
