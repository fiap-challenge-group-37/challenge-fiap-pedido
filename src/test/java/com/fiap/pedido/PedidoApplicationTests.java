package com.fiap.pedido;

import org.junit.jupiter.api. Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org. springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions. assertNotNull;

@SpringBootTest(properties = {
        "spring.cloud.aws.sqs.enabled=false",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@ActiveProfiles("test")
class PedidoApplicationTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {
        assertNotNull(context);
    }
}