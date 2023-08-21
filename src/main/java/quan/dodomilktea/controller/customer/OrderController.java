package quan.dodomilktea.controller.customer;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import quan.dodomilktea.dto.customer.MessageDto;
import quan.dodomilktea.model.AddOn;
import quan.dodomilktea.model.CustomMilkTea;
import quan.dodomilktea.model.Order;
import quan.dodomilktea.service.AddOnService;
import quan.dodomilktea.service.CustomMilkTeaService;
import quan.dodomilktea.service.MilkTeaService;
import quan.dodomilktea.service.OrderService;

@RestController
@RequestMapping("/customer")
@CrossOrigin(origins ="http://localhost:3000")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    MilkTeaService milkTeaService;
    @Autowired
    AddOnService addOnService;
    @Autowired
    CustomMilkTeaService customMilkTeaService;

    //save custom milk tea to cart
    @PostMapping(value = {"/save-to-cart/{mteaid}/{addOnIds}","/save-to-cart/{mteaid}"})
    public ResponseEntity<?> saveToCart(@RequestBody @Valid CustomMilkTea customMilkTea,
                                        @PathVariable String mteaid, @PathVariable @Nullable String addOnIds, HttpServletRequest request) {

        //Check ordering in cart exist or not
        Order orderIncart = orderService.getOrderIncart(request);
        if (orderIncart == null) orderIncart=orderService.createNewOrderIncart(request);

        //If have addons in url
        if (addOnIds != null) {
            customMilkTea = saveAddOnIntoCMT(addOnIds, customMilkTea);
        }

        customMilkTea.setMilkTea(milkTeaService.getMilkTeaById(mteaid));

        //Check custom milk tea existed or not, if not create a new one
        customMilkTea.setTotal_price(getTotalPrice(customMilkTea));
        customMilkTea.setTotal_cost(getTotalCost(customMilkTea));
        customMilkTea.setOrder(orderIncart);
        CustomMilkTea savedCustomMilkTea = customMilkTeaService.saveCustomMilkTea(customMilkTea);

        //update order total price
        orderService.updateTotalPrice(savedCustomMilkTea.getOrder().getOrder_id(), savedCustomMilkTea.getTotal_price());

        return ResponseEntity.ok(new MessageDto("You added " + savedCustomMilkTea.getMilkTea().getName() +
                " size " + savedCustomMilkTea.getSize() + " to cart"));
    }

    //manage cart
    @GetMapping("/my-cart")
    public ResponseEntity<?> manageCart(HttpServletRequest request) {
        //Check ordering in cart exist or not
        Order orderIncart = orderService.getOrderIncart(request);
        if (orderIncart == null) orderIncart=orderService.createNewOrderIncart(request);

        return ResponseEntity.ok(customMilkTeaService.getCustomMilkTeaInCart(orderIncart.getOrder_id()));
    }

    //delete item in cart
    @DeleteMapping("/my-cart/delete/{cmtId}")
    public ResponseEntity<?> deleteCustomMilkTea(@PathVariable String cmtId) {
        return ResponseEntity.ok(new MessageDto(customMilkTeaService.deleteCustomMilkTea(cmtId)));
    }

    //update item in cart
    @PutMapping("/my-cart/update/{cmtId}")
    public ResponseEntity<?> updateCsutomMilkTea(@RequestBody @Valid CustomMilkTea customMilkTea, @PathVariable String cmtId) {
        return ResponseEntity.ok(new MessageDto(customMilkTeaService.updateCustomMilkTea(customMilkTea, cmtId)));
    }

    //getOrderInCart
    @GetMapping("my-cart/order")
    public ResponseEntity<?> getOrder(HttpServletRequest request) {
        return ResponseEntity.ok(orderService.getOrderIncart(request));
    }

    //order with COD
    @PutMapping("my-cart/order/COD")
    public ResponseEntity<?> updateOrderCOD(HttpServletRequest request, @RequestBody @Valid Order order) {
        return ResponseEntity.ok(new MessageDto(orderService.changeStateToPassToStaff(request, order)));
    }

    //order with Paypal in PaypalController.java

    //Customer order state
    @GetMapping("/all-orders-ordered")
    public ResponseEntity<?> ordersState(HttpServletRequest request) {
        List<Order> orders = orderService.getOrdersOrdered(request);
        if (orders == null) {
            return ResponseEntity.ok(new MessageDto("Your orders are emtpy!!!"));
        }
        return ResponseEntity.ok(orders);
    }

    //Cancel order by customer
    @PutMapping("/all-orders-ordered/cancel-order/{orderId}")
    public ResponseEntity<?> cancelOrder(@PathVariable String orderId, HttpServletRequest request) {
        return ResponseEntity.ok(new MessageDto(orderService.cancelOrderByCustomer(orderId, request)));
    }

    //Save add on into custom milk tea
    private @Valid CustomMilkTea saveAddOnIntoCMT(String addOnIds, @Valid CustomMilkTea customMilkTea) {
        List<AddOn> addOns = new ArrayList<>();
        String[] addOnsIdArr;

        if (addOnIds.contains("+")) addOnsIdArr = addOnIds.split("\\+");
        else addOnsIdArr = new String[]{addOnIds};

        if (addOnsIdArr.length > 0) {
            for (String addOnId : addOnsIdArr) {
                addOns.add(addOnService.getAddOnById(addOnId));
            }
        }
        customMilkTea.setAddOn(addOns);
        return customMilkTea;
    }

    //calculate totalPrice of custom milk tea
    private int getTotalPrice(@Valid CustomMilkTea customMilkTea) {
        int totalPrice = 0;
        if (customMilkTea.getAddOn() != null) {
            for (AddOn addOn : customMilkTea.getAddOn()) {
                totalPrice += addOn.getPrice();
            }
        }
        totalPrice += customMilkTea.getMilkTea().getPrice();
        totalPrice *= customMilkTea.getQuantity();
        return totalPrice;
    }

    //calculate totalCost of custom milktea
    private int getTotalCost(@Valid CustomMilkTea customMilkTea) {
        int totalCost = 0;
        if (customMilkTea.getAddOn() != null) {
            for (AddOn addOn : customMilkTea.getAddOn()) {
                totalCost += addOn.getCost();
            }
        }
        totalCost += customMilkTea.getMilkTea().getCost();
        totalCost *= customMilkTea.getQuantity();
        return totalCost;
    }
}
