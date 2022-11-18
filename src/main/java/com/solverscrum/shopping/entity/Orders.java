package com.solverscrum.shopping.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Entity @Table(name = "Orders_10709423")
public class Orders {
    @Id @Column @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderId;
    @Column @Temporal(TemporalType.DATE)
    private Date orderDate;
    @ManyToOne @JoinColumn(name = "customerId")
    private Customers customer;
    @ManyToOne @JoinColumn(name = "shipperId")
    private Shippers shipper;

}