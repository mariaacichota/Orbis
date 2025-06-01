package br.com.orbis.Orbis;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EventCartTest extends BaseTest {

    @Test
    public void testAddAndRemoveEventFromCart() throws InterruptedException {
        driver.get("http://localhost:3000/");
        Thread.sleep(2000);

        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
        Thread.sleep(1000);

        By verDetalhesXPath = By.xpath(
                "//div[contains(@class,'ant-card') and .//div[contains(@class,'ant-card-meta-title') and text()='Tech Conference 2025']]//button[.//span[text()='Ver detalhes']]"
        );


        WebElement verDetalhesBtn = wait.until(ExpectedConditions.presenceOfElementLocated(verDetalhesXPath));
        Thread.sleep(1000);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", verDetalhesBtn);
        Thread.sleep(2000);

        wait.until(ExpectedConditions.elementToBeClickable(verDetalhesBtn)).click();
        Thread.sleep(2000);

        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/eventos/1"));
        Thread.sleep(1000);

        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Adicionar ao carrinho')]")
        ));
        Thread.sleep(1000);
        addToCartBtn.click();
        Thread.sleep(3000);

        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            Thread.sleep(1000);
            System.out.println("Texto do alerta: " + alert.getText());
            alert.accept();
            Thread.sleep(1000);
        } catch (Exception e) {
            WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(), 'Evento adicionado ao carrinho!')]")
            ));
            Thread.sleep(1000);
            assertTrue(popup.isDisplayed());
            System.out.println("Modal de confirmação visível");


            WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(., 'OK')]")
            ));
            Thread.sleep(1000);
            okButton.click();
            Thread.sleep(1000);

            wait.until(ExpectedConditions.invisibilityOf(popup));
            Thread.sleep(1000);
        }

        driver.get("http://localhost:3000/carrinho");
        Thread.sleep(3000);

        WebElement eventInCart = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p/strong[text()='Tech Conference 2025']")
        ));
        Thread.sleep(1000);
        assertTrue(eventInCart.isDisplayed());

        WebElement removeButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Remover']")
        ));
        Thread.sleep(1000);
        removeButton.click();
        Thread.sleep(3000);
        WebElement emptyCartMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'Seu carrinho está vazio.')]")
        ));
        Thread.sleep(1000);
        assertTrue(emptyCartMsg.isDisplayed());
    }


}
