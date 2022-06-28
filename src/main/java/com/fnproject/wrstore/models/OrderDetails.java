package com.fnproject.wrstore.models;


import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

@NoArgsConstructor
//@AllArgsConstructor
//@RequiredArgsConstructor
@Getter
@Setter
@Slf4j
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "order_details")


@Entity
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    @Column(name = "quantity")
    int quantity;

    @NotNull
    @Column(name = "price")
    double price;

    @Column(name = "date")
    Date date;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    Order order;

    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    Product product;

    public OrderDetails(Order order, @NotNull Product product, @NotNull int quantity, @NotNull double price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        this.order = order;
        this.date = new Date();
    }
}