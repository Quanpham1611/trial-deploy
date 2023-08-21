package quan.dodomilktea.controller.shipper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import quan.dodomilktea.dto.customer.MessageDto;
import quan.dodomilktea.globalenum.EOrderState;
import quan.dodomilktea.model.Order;
import quan.dodomilktea.service.OrderService;

@RestController
@RequestMapping("/shipper/order")
@CrossOrigin(origins ="http://localhost:3000")
public class ShipperOrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/all-order")
    public ResponseEntity<Object> getAllOrderShipper() {
        List<Order> orderList = orderService.getOrderPassedToShipper();

        if (orderList == null) {
            throw new NullPointerException();
        }
        return ResponseEntity.ok(orderList);
    }

    @GetMapping("order-detail")
    public ResponseEntity<Object> getOrderDetailShipper(@RequestParam String orderId) {
        Order order = orderService.getOrderDetailShipper(orderId);
        if(order == null) { throw new NullPointerException(); }

        if(order.getOrder_state().equals(EOrderState.PassedToShipper.toString())) {
            order = orderService.changeStateOrder(order, EOrderState.Shipping.toString());
            return ResponseEntity.ok(order);
        }
        return ResponseEntity.ok(new MessageDto("Get order unsuccessfully, order was received by another shipper"));
    }

    @PutMapping("manage-order/{orderId}")
    public ResponseEntity<Object> manageOrder(@PathVariable String orderId, @RequestParam boolean isSuccessful) {
        Order order = orderService.manageOrder(orderId, isSuccessful);
        return ResponseEntity.ok(order);
    }
}
