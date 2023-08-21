package quan.dodomilktea.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quan.dodomilktea.dto.customer.MessageDto;
import quan.dodomilktea.model.Order;
import quan.dodomilktea.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/admin/order")
@CrossOrigin(origins ="http://localhost:3000")
public class AdminOrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/get-all")
    public ResponseEntity<Object> getAllOrders() {
        List<Order> orders = orderService.getAllOrderOrdered();
        if (orders == null) {
            throw new NullPointerException();
        }
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getorder")
    public ResponseEntity<Object> getOrderDetail(@RequestParam String orderId) {
        Order order = orderService.getOrderDetailShipper(orderId);
        if (order == null) {
            throw new NullPointerException();
        }
        return ResponseEntity.ok(order);
    }

    @PutMapping("/delete/{orderId}")
    public ResponseEntity<Object> deleteReport(@PathVariable String orderId) {
        boolean isDeleted = orderService.deleteOrder(orderId);
        if (isDeleted) {
            return ResponseEntity.ok(new MessageDto("Delete Order Successfully"));
        }
        return ResponseEntity.ok(new MessageDto("Delete Order Unsuccessfully, please check again !"));
    }

}