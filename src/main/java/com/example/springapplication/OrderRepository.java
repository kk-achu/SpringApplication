package com.example.springapplication;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {

	List<CustomerOrder> findAllByCustomerId(Long customerId);

	@Query(value = """
			SELECT c.id AS customerId,
				   c.firstName AS firstName,
				   c.lastName AS lastName,
				   c.email AS email,
				   c.phoneNumber AS phoneNumber,
				   c.addLine1 AS addLine1,
				   c.addLine2 AS addLine2,
				   c.state AS state,
				   c.country AS country,
				   o.orderid AS orderId,
				   o.customerid AS orderCustomerId,
				   o.orderPlacedAt AS orderPlacedAt
			FROM Customer c
			LEFT JOIN CustomerOrder o ON o.customerid = c.id
			WHERE c.id = :customerId
			ORDER BY o.orderPlacedAt, o.orderid
			""", nativeQuery = true)
	List<CustomerOrderQueryRow> findCustomerWithOrdersNative(@Param("customerId") Long customerId);
}