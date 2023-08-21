package quan.dodomilktea.controller.customer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import quan.dodomilktea.config.paypal.PaypalUtils;
import quan.dodomilktea.dto.customer.MessageDto;
import quan.dodomilktea.globalenum.PaypalPaymentIntent;
import quan.dodomilktea.globalenum.PaypalPaymentMethod;
import quan.dodomilktea.model.Order;
import quan.dodomilktea.service.OrderService;
import quan.dodomilktea.service.PaypalService;

@RestController
@CrossOrigin(origins ="http://localhost:3000")
public class PaypalController {
    public static final String URL_PAYPAL_SUCCESS = "paypal/pay/success";
    public static final String URL_PAYPAL_CANCEL = "paypal/pay/cancel";
    private Logger log = LoggerFactory.getLogger(getClass());
    Order userOrder = new Order();

    @Autowired
    PaypalService paypalService;
    @Autowired
    OrderService orderService;

    @GetMapping(URL_PAYPAL_CANCEL)
    public ResponseEntity<?> cancelPay() {
        return ResponseEntity.ok(new MessageDto("Payment canceled"));
    }

    @GetMapping(URL_PAYPAL_SUCCESS)
    public ResponseEntity<?> successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId,
                                        HttpServletResponse response, HttpServletRequest request) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                return ResponseEntity.ok(new MessageDto(orderService.changeStateToPassToStaff(request, userOrder)));
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(new MessageDto("Unsuccessful payment"));
    }

    @PostMapping("customer/my-cart/order/paypal")
    public ResponseEntity<?> pay(HttpServletRequest request, @RequestBody @Valid Order order){
        String cancelUrl = PaypalUtils.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
        String successUrl = PaypalUtils.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
        Double money = Double.valueOf(order.getOrder_total_price())*0.000043;

        try {
            Payment payment = paypalService.createPayment(money, "USD", PaypalPaymentMethod.paypal
                    , PaypalPaymentIntent.sale, "payment description", cancelUrl, successUrl);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    userOrder = order;
                    return ResponseEntity.ok(new MessageDto(links.getHref()));
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.ok(new MessageDto("Unsuccessful payment"));
    }
}
