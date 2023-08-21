package quan.dodomilktea.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import quan.dodomilktea.dto.admin.StatisticizeDto;
import quan.dodomilktea.globalenum.EOrderState;
import quan.dodomilktea.globalenum.EPaymentMethod;
import quan.dodomilktea.jwt.JwtTokenUlti;
import quan.dodomilktea.model.Account;
import quan.dodomilktea.model.CustomMilkTea;
import quan.dodomilktea.model.Order;
import quan.dodomilktea.repo.AccountRepository;
import quan.dodomilktea.repo.OrderRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    JwtTokenUlti jwtTokenUtil;
    @Autowired
    AccountRepository accountRepo;
    @Autowired CustomMilkTeaService customMilkTeaService;
    Logger logger = LoggerFactory.getLogger(OrderService.class);

    public Order getOrderIncart(HttpServletRequest request) {
        //Check token with acc_id
        String token = jwtTokenUtil.getAccessToken(request);
        String[] subjectArray = jwtTokenUtil.getSubject(token).split(",");
        Account account = accountRepo.findById(subjectArray[0]).get();
        if (account != null) {
            return orderRepository.getOrderWithStateCustomer(EOrderState.InCart.toString(), account.getAcc_id());
        }
        else throw new NullPointerException();
    }

    public List<Order> getOrderPassedToStaff() {
        List<Order> orders = orderRepository.getOrderWithStateStaffManager(EOrderState.PassedToStaff.toString());
        if (orders == null) throw new NullPointerException();
        return orders;
    }

    public void updateTotalPrice(String orderId, int price) {
        Order updateOrder = orderRepository.findById(orderId).orElseThrow(() -> new NullPointerException());
        int updatePrice = updateOrder.getOrder_total_price() + price;
        updateOrder.setOrder_total_price(updatePrice);
        orderRepository.save(updateOrder);
    }

    public Order createNewOrderIncart(HttpServletRequest request) {
        //Check token with acc_id
        String token = jwtTokenUtil.getAccessToken(request);
        String[] subjectArray = jwtTokenUtil.getSubject(token).split(",");
        Account account = accountRepo.findById(subjectArray[0]).get();
        if (account != null) {
            Order order = new Order();
            order.setReceiver_name(account.getName());
            order.setAddress(account.getAddress());
            order.setPhone(account.getPhone());
            order.setPayment_method(EPaymentMethod.COD.toString());
            order.setCustomer_name(account.getName());
            order.setOrder_state(EOrderState.InCart.toString());
            order.setCustomer_account(account);
            order.setEnabled(true);
            return orderRepository.save(order);
        }
        else throw new NullPointerException();
    }

    public String changeStateToPassToStaff(HttpServletRequest request, Order order) {
        Order updateOrder = getOrderIncart(request);
        updateOrder.setAddress(order.getAddress());
        updateOrder.setReceiver_name(order.getReceiver_name());
        updateOrder.setPhone(order.getPhone());
        updateOrder.setPayment_method(order.getPayment_method());
        updateOrder.setOrder_date(new Date());
        updateOrder.setNote(order.getNote());
        updateOrder.setOrder_state(EOrderState.PassedToStaff.toString());
        orderRepository.save(updateOrder);
        createNewOrderIncart(request);
        return "Order successfully!";
    }

    public String passOrderToShipperOrCancel(String orderId, String state) {
        Order updateOrder = orderRepository.findById(orderId).orElseThrow(() -> new NullPointerException());
        if (!(updateOrder.getOrder_state().equals(EOrderState.PassedToStaff.toString()))) {
            return "Staff manager can't change this order's state";
        }
        updateOrder.setOrder_state(state);
        orderRepository.save(updateOrder);
        if (state.equals(EOrderState.CanceledByStaff.toString())) {
            return "Cancel order successfully!!!";
        }
        return "Order has been passed to shipper, quickly!!!";
    }


    public List<Order> getOrdersOrdered(HttpServletRequest request) {
        //Check token with acc_id
        String token = jwtTokenUtil.getAccessToken(request);
        String[] subjectArray = jwtTokenUtil.getSubject(token).split(",");
        Account account = accountRepo.findById(subjectArray[0]).get();
        if (account != null) {
            return orderRepository.getOrdersWithoutStateCustomer(EOrderState.InCart.toString(), account.getAcc_id());
        }
        else throw new NullPointerException();
    }

    public String cancelOrderByCustomer(String orderId, HttpServletRequest request) {
        String token = jwtTokenUtil.getAccessToken(request);
        String[] subjectArray = jwtTokenUtil.getSubject(token).split(",");
        Account account = accountRepo.findById(subjectArray[0]).get();
        if (account == null) {
            return "Account doesn't exist!!!";
        }

        Order updateOrder = orderRepository.findOrderByCustomerId(
                orderId, account.getAcc_id()).orElseThrow(() -> new NullPointerException());
        String orderState = updateOrder.getOrder_state();

        if (orderState.equals(EOrderState.InCart.toString())) return "Order doesn't exist or hasn't been paid for!!!";

        if (orderState.equals(EOrderState.CanceledByCustomer.toString())) return "Order has ben canceled by you!!!";

        if (!(orderState.equals(EOrderState.PassedToStaff.toString()))) return "The order has been proccessed, please call the phone number 0971723723 if you wish to cancel";

        updateOrder.setOrder_state(EOrderState.PassedToShipper.toString());
        orderRepository.save(updateOrder);
        return "Your order has been canceled successfully!!!";
    }

    public List<Order> getOrderPassedToShipper() {
        return orderRepository.findByState(EOrderState.PassedToShipper.toString());
    }

    public Order getOrderDetailShipper(String orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public Order changeStateOrder(Order order, String orderState) {
        order.setOrder_state(orderState);
        return orderRepository.save(order);
    }

    public Order manageOrder(String orderId, boolean isSuccessful) {
        Order order = orderRepository.findById(orderId).orElse(null);

        if(order == null) {
            throw new NullPointerException();
        }

        if(isSuccessful) {
            return changeStateOrder(order, EOrderState.Shipped.toString());
        }
        return changeStateOrder(order, EOrderState.CanceledByShipper.toString());
    }

    public List<Order> getAllOrderOrdered() {
        return orderRepository.findOrderOrdered(EOrderState.InCart.toString());
    }

    public boolean deleteOrder(String id) {
        Order order = orderRepository.findById(id).orElse(null);
        if(order == null || order.isEnabled() == false) { return false; }

        order.setEnabled(false);
        try {
            orderRepository.save(order);
        } catch(Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    public List<Order> getOrderByDate(String date) {
        return orderRepository.findByDate(date);
    }

    public StatisticizeDto statisticizeByDate(String date) {
        List<Order> orders = getOrderByDate(date);
        if (orders.isEmpty()) { return null; }

        StatisticizeDto result = new StatisticizeDto();
        int totalFunds = 0;
        int totalRevenue = 0;
        for(Order order : orders) {
            totalRevenue += order.getOrder_total_price();
            totalFunds += calculateTotalFundsOfOrder(order);
        }
        result.setTotalFunds(totalFunds);
        result.setTotalRevenue(totalRevenue);
        result.setTotalProfit(totalRevenue - totalFunds);

        return result;
    }

    private int calculateTotalFundsOfOrder(Order order) {
        List<CustomMilkTea> milkteas = customMilkTeaService.getCustomMilkteaByOrder(order.getOrder_id());
        int total = 0;
        for(CustomMilkTea milktea : milkteas) {
            total += milktea.getTotal_cost() * milktea.getQuantity();
        }
        return total;
    }
}
