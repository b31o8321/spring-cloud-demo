package com.demo.common.service.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class GatewayApplicationTests {
	@Test
	void run() {
		String pathPatten = "^/api/users/{userId}/orders/{orderId}";
		String path = "/api/users/1/orders/1";

	}
}
