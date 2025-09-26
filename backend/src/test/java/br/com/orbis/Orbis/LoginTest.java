package br.com.orbis.Orbis;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledIfSystemProperty(named = "chrome.tests.enabled", matches = "true")
public class LoginTest extends BaseTest {

    @Test
    void testUserRegistrationAndLogin() {
        driver.get("http://localhost:3000/cadastro");

        String email = "teste1" + System.currentTimeMillis() + "@email.com";
        String senha = "Senha123@";

        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("name"))).sendKeys("Usuário Selenium");
        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(senha);

        WebElement roleSelect = driver.findElement(By.name("role"));
        roleSelect.click();
        roleSelect.sendKeys(Keys.ARROW_DOWN);
        roleSelect.sendKeys(Keys.ENTER);

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        wait.until(ExpectedConditions.urlContains("/perfil"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.name("email")));

        driver.findElement(By.name("email")).sendKeys(email);
        driver.findElement(By.name("password")).sendKeys(senha);

        WebElement entrarButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.cssSelector("button.resultado[type='submit']"))
        );
        entrarButton.click();

        wait.until(ExpectedConditions.urlContains("/logado"));
        assertTrue(driver.getCurrentUrl().contains("/logado"));

        WebElement sairButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Sair']"))
        );
        sairButton.click();

        wait.until(ExpectedConditions.urlContains("/perfil"));
        assertTrue(driver.getCurrentUrl().contains("/perfil"));
    }}


