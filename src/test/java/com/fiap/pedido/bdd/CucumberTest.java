package com.fiap.pedido.bdd;


import io.cucumber.junit. Cucumber;
import io.cucumber. junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.fiap.pedido.bdd",
        plugin = {
                "pretty",
                "html:target/cucumber-reports.html",
                "json:target/cucumber-reports/cucumber.json"
        }
)
public class CucumberTest {
}