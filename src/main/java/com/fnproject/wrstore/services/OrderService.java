package com.fnproject.wrstore.services;

import com.fnproject.wrstore.DTO.CartDto;
import com.fnproject.wrstore.DTO.CartItemDto;
import com.fnproject.wrstore.data.EmployeeRepository;
import com.fnproject.wrstore.data.OrderDetailsRepository;
import com.fnproject.wrstore.data.OrderRepository;
import com.fnproject.wrstore.models.Employee;
import com.fnproject.wrstore.models.Order;
import com.fnproject.wrstore.models.OrderDetails;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Slf4j
//@NoArgsConstructor
//@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Transactional(rollbackOn = {DataAccessException.class})
public class OrderService {

    //OrderRepository orderRepository;
    //EmployeeRepository employeeRepository;

    private CartService cartService;

    OrderRepository orderRepository;

    OrderDetailsRepository orderItemsRepository;

    @Autowired
    public OrderService(CartService cartService, OrderRepository orderRepository, OrderDetailsRepository orderItemsRepository) {
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.orderItemsRepository = orderItemsRepository;
    }

//    @Value("${BASE_URL}")
//    private String baseURL;
//
//    @Value("${STRIPE_SECRET_KEY}")
//    private String apiKey;

    // create total price
//    SessionCreateParams.LineItem.PriceData createPriceData(CheckoutItemDto checkoutItemDto) {
//        return SessionCreateParams.LineItem.PriceData.builder()
//                .setCurrency("usd")
//                .setUnitAmount((long)(checkoutItemDto.getPrice()*100))
//                .setProductData(
//                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
//                                .setName(checkoutItemDto.getProductName())
//                                .build())
//                .build();
//    }
//
//    // build each product in the stripe checkout page
//    SessionCreateParams.LineItem createSessionLineItem(CheckoutItemDto checkoutItemDto) {
//        return SessionCreateParams.LineItem.builder()
//                // set price for each product
//                .setPriceData(createPriceData(checkoutItemDto))
//                // set quantity for each product
//                .setQuantity(Long.parseLong(String.valueOf(checkoutItemDto.getQuantity())))
//                .build();
//    }
//
//    // create session from list of checkout items
//    public Session createSession(List<CheckoutItemDto> checkoutItemDtoList) throws StripeException {
//
//        // supply success and failure url for stripe
//        String successURL = baseURL + "payment/success";
//        String failedURL = baseURL + "payment/failed";

        // set the private key
        //Stripe.apiKey = apiKey;

        //List<SessionCreateParams.LineItem> sessionItemsList = new ArrayList<>();

        // for each product compute SessionCreateParams.LineItem
//        for (CheckoutItemDto checkoutItemDto : checkoutItemDtoList) {
//            sessionItemsList.add(createSessionLineItem(checkoutItemDto));
//        }

//        // build the session param
//        SessionCreateParams params = SessionCreateParams.builder()
//                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
//                .setMode(SessionCreateParams.Mode.PAYMENT)
//                .setCancelUrl(failedURL)
//                .addAllLineItem(sessionItemsList)
//                .setSuccessUrl(successURL)
//                .build();
//        return Session.create(params);
//    }

    public void placeOrder (Employee employee /*String sessionId*/) {
        // first let get cart items for the user
        CartDto cartDto = cartService.listCartItems(employee);

        List<CartItemDto> cartItemDtoList = cartDto.getCartItems();

        // create the order and save it
        Order newOrder = new Order();
        newOrder.setDate(new Date());
        //newOrder.setSessionId(sessionId);
        newOrder.setEmployee(employee);
        newOrder.setTotalPrice(cartDto.getTotalCost());
        orderRepository.save(newOrder);

        for (CartItemDto cartItemDto : cartItemDtoList) {
            // create orderItem and save each one
            OrderDetails orderItem = new OrderDetails();
            orderItem.setDate(new Date());
            orderItem.setPrice(cartItemDto.getProduct().getPrice());
            orderItem.setProduct(cartItemDto.getProduct());
            orderItem.setQuantity(cartItemDto.getQuantity());
            orderItem.setOrder(newOrder);
            // add to order item list
            orderItemsRepository.save(orderItem);
        }
        //
        cartService.deleteEmployeeCartItems(employee);
    }

    public List<Order> listOrders(Employee employee) {
        return orderRepository.findAllByEmployeeOrderByDateDesc(employee);
    }


    public Order getOrder(Integer orderId) throws NoSuchElementException {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get();
        }
       throw new NoSuchElementException("Order not found");
    }



    // My old stuff


//
//    public List<Order> findAll(){
//        return orderRepository.findAll();
//    }
//    @Transactional(rollbackOn = {NoSuchElementException.class})
//    public Order findById(int id) throws NoSuchElementException{
//        return orderRepository.findById(id).orElseThrow();
//    }
//
//    public void saveOrUpdate(Order order, int id){
//        order.setEmployee(employeeRepository.findById(id).get());
//        orderRepository.save(order);
//        log.info(String.format("Order ID created: %d Order Employee Name: %s",order.getOrderId(),order.getEmployee()));
//    }
//
//    public void delete(Order order){
//        orderRepository.delete(order);
//    }

}
