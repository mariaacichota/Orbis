package br.com.orbis.Orbis;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledIfSystemProperty(named = "chrome.tests.enabled", matches = "true")
class PaymentTest extends BaseTest {

    @Test
    void testPayment() {
        driver.get("http://localhost:3000/carrinho");

        WebElement stripeIframe = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("iframe[src*='elements-inner-card']")
        ));
        driver.switchTo().frame(stripeIframe);

        WebElement numeroInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[name='cardnumber']")
        ));
        numeroInput.sendKeys("4242 4242 4242 4242");

        WebElement validadeInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[name='exp-date']")
        ));
        validadeInput.sendKeys("12 / 34");

        WebElement cvcInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.cssSelector("input[name='cvc']")
        ));
        cvcInput.sendKeys("123");

        driver.switchTo().defaultContent();

        WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("button[type='submit']")
        ));
        submitButton.click();

        WebElement mensagem = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(text(), 'Pagamento simulado com sucesso! (modo teste)')]")
        ));

        assertTrue(mensagem.isDisplayed(), "A mensagem de pagamento simulado não foi encontrada.");
    }
}
