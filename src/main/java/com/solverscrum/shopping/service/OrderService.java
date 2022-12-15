package com.solverscrum.shopping.service;

import com.solverscrum.shopping.entity.Customer;
import com.solverscrum.shopping.entity.OrderDetail;
import com.solverscrum.shopping.entity.Order;
import com.solverscrum.shopping.entity.Shipper;
import com.solverscrum.shopping.exception.CustomerException;
import com.solverscrum.shopping.exception.OrderException;
import com.solverscrum.shopping.exception.ProductException;
import com.solverscrum.shopping.exception.ShipperException;
import com.solverscrum.shopping.repository.CustomerRepository;
import com.solverscrum.shopping.repository.OrderRepository;
import com.solverscrum.shopping.repository.ProductRepository;
import com.solverscrum.shopping.repository.ShipperRepository;
import com.solverscrum.shopping.vo.OrderDetailsVo;
import com.solverscrum.shopping.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    ShipperRepository shipperRepository;
    @Autowired
    ProductRepository productRepository;

    private static ProductRepository staticProductRepository;

    @PostConstruct
    private void init(){
        staticProductRepository = productRepository;
    }

    public List<OrderVo> getOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderService::convertToOrderVo)
                .collect(Collectors.toList());
    }

    public OrderVo getOrderById(Integer id){
        Optional<Order> order = orderRepository.findById(id);
        if (order.isEmpty())
            throw new OrderException("order not found with id :"+id);
        return convertToOrderVo(order.get());
    }

    public String addOrder(OrderVo orderVo) throws ParseException {
        Optional<Customer> customer = customerRepository.findById(orderVo.getCustomerId());
        Optional<Shipper> shipper = shipperRepository.findById(orderVo.getShipperId());
        if (customer.isEmpty())
            throw new CustomerException("order not found with id :"+orderVo.getCustomerId());
        if (shipper.isEmpty())
            throw new ShipperException("shipper not found with id :"+orderVo.getShipperId());
        Order order = convertToOrders(orderVo);
        orderRepository.save(order);

        return "Added!!";
    }

    static Order convertToOrders(OrderVo orderVo) throws ParseException{
        Order order = new Order();
        order.setOrderDate(new SimpleDateFormat("yyyy-MM-dd").parse(orderVo.getOrderDate()));
        Customer customers = new Customer();
        customers.setCustomerId(orderVo.getCustomerId());
        order.setCustomer(customers);
        Shipper shippers = new Shipper();
        shippers.setShipperId(orderVo.getShipperId());
        order.setShipper(shippers);
        List<OrderDetail> orderDetails = new ArrayList<>();
        for(OrderDetailsVo orderDetailsVo : orderVo.getOrderDetailsVo()){
            if(staticProductRepository.findById(orderDetailsVo.getProductId()).isEmpty())
                throw new ProductException("Product not found with id :"+orderDetailsVo.getProductId());
            orderDetails.add(OrderDetailsService.convertToOrderDetail(orderDetailsVo));
        }
        order.setOrderDetails(orderDetails);

        return order;
    }

    static OrderVo convertToOrderVo(Order orders){
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderId(orders.getOrderId());
        orderVo.setOrderDate(orders.getOrderDate().toString());
        orderVo.setCustomer(CustomerService.convertToCustomerVo(orders.getCustomer()));
        orderVo.setShipper(ShipperService.convertToShipperVo(orders.getShipper()));
        List<OrderDetailsVo> orderDetailsVos = orders.getOrderDetails()
                .stream()
                .map(OrderDetailsService::convertToOrderDetailsVo)
                .collect(Collectors.toList());
        orderVo.setOrderDetailsVo(orderDetailsVos);
        return orderVo;

    }
}
