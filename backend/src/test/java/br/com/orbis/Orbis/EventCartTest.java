package br.com.orbis.Orbis;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledIfSystemProperty(named = "chrome.tests.enabled", matches = "true")
class EventCartTest extends BaseTest {

    @Test
    void testAddAndRemoveEventFromCart() {
        driver.get("http://localhost:3000/");


        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));


        By verDetalhesXPath = By.xpath(
                "//div[contains(@class,'ant-card') and .//div[contains(@class,'ant-card-meta-title') and text()='Tech Conference 2025']]//button[.//span[text()='Ver detalhes']]"
        );


        WebElement verDetalhesBtn = wait.until(ExpectedConditions.presenceOfElementLocated(verDetalhesXPath));


        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center', behavior: 'smooth'});", verDetalhesBtn);


        wait.until(ExpectedConditions.elementToBeClickable(verDetalhesBtn)).click();


        wait.until(ExpectedConditions.urlToBe("http://localhost:3000/eventos/1"));


        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[contains(text(),'Adicionar ao carrinho')]")
        ));
        addToCartBtn.click();


        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            System.out.println("Texto do alerta: " + alert.getText());
            alert.accept();
        } catch (Exception e) {
            WebElement popup = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(), 'Evento adicionado ao carrinho!')]")
            ));
            assertTrue(popup.isDisplayed());
            System.out.println("Modal de confirmação visível");


            WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[contains(., 'OK')]")
            ));
            okButton.click();

            wait.until(ExpectedConditions.invisibilityOf(popup));
        }

        driver.get("http://localhost:3000/carrinho");

        WebElement eventInCart = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//p/strong[text()='Tech Conference 2025']")
        ));
        assertTrue(eventInCart.isDisplayed());

        WebElement removeButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[text()='Remover']")
        ));
        removeButton.click();
        WebElement emptyCartMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(text(), 'Seu carrinho está vazio.')]")
        ));
        assertTrue(emptyCartMsg.isDisplayed());
    }
}
