package br.com.orbis.Orbis;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.jupiter.api.Assertions.assertTrue;

@EnabledIfSystemProperty(named = "chrome.tests.enabled", matches = "true")
class FiltersTest extends BaseTest {

    @BeforeEach
    @Override
    void setup() {
        super.setup();
        driver.get("http://localhost:3000");
    }

    @Test
    @DisplayName("Deve aplicar os filtros de categoria e região")
    void shouldApplyFilters() {
        try {
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));

            WebElement categorySelect = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'ant-select')][.//span[contains(text(),'Categoria')]]")
            ));
            categorySelect.click();

            WebElement artOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'ant-select-item-option') and .='Arte']")
            ));
            artOption.click();

            WebElement regionSelect = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'ant-select')][.//span[contains(text(),'Região')]]")
            ));
            regionSelect.click();

            WebElement southeastOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[contains(@class,'ant-select-item-option') and .='Sudeste']")
            ));
            southeastOption.click();

            WebElement applyButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[contains(text(),'Aplicar Filtros')]]")
            ));
            applyButton.click();

            assertTrue(applyButton.isDisplayed());

            wait.until(ExpectedConditions.textToBePresentInElementLocated(
                    By.xpath("//h4[contains(@class,'ant-typography')]"),
                    "Todos os Eventos (1)"
            ));

        } catch (Exception e) {
            throw new AssertionError("Falha no teste: " + e.getMessage(), e);
        }
    }

    @Test
    @DisplayName("Deve buscar evento pelo nome e limpar filtros")
    void shouldSearchByName() {
        try {
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));

            WebElement searchInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[@placeholder='Buscar por nome do evento']")));
            searchInput.clear();
            searchInput.sendKeys("Web Dev Conference");
            searchInput.sendKeys(Keys.ENTER);

            wait.until(ExpectedConditions.textToBePresentInElementLocated(
                    By.xpath("//h4[contains(@class,'ant-typography')]"),
                    "Todos os Eventos (1)"
            ));

            WebElement clearButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[normalize-space()='Limpar']")));
            clearButton.click();

            assertTrue(clearButton.isDisplayed());

            wait.until(ExpectedConditions.textToBePresentInElementLocated(
                    By.xpath("//h4[contains(@class,'ant-typography')]"),
                    "Todos os Eventos (10)"
            ));

        } catch (Exception e) {
            throw new AssertionError("Falha no teste de busca por nome e limpeza de filtros: " + e.getMessage(), e);
        }
    }

    @Test
    @DisplayName("Deve filtrar eventos pela data")
    void shouldFilterByDate() {
        try {
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));

            WebElement dateRangeInput = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//input[@placeholder='Data início']")));
            dateRangeInput.click();


            WebElement datePickerDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div.ant-picker-dropdown")));

            WebElement startDate = datePickerDropdown.findElement(
                    By.xpath(".//div[contains(@class,'ant-picker-panels')]/div[1]//td[contains(@class,'ant-picker-cell') and not(contains(@class,'ant-picker-cell-disabled')) and normalize-space(.)='15']")
            );
            startDate.click();

            WebElement endDate = datePickerDropdown.findElement(
                    By.xpath(".//div[contains(@class,'ant-picker-panels')]/div[2]//td[contains(@class,'ant-picker-cell') and not(contains(@class,'ant-picker-cell-disabled')) and normalize-space(.)='15']")
            );
            endDate.click();

            WebElement applyButton = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//button[.//span[text()='Aplicar Filtros']]")));
            applyButton.click();

            assertTrue(applyButton.isDisplayed());

            wait.until(ExpectedConditions.textToBePresentInElementLocated(
                    By.xpath("//h4[contains(@class,'ant-typography')]"),
                    "Todos os Eventos (1)"
            ));

        } catch (Exception e) {
            throw new AssertionError("Falha no teste de filtro por data: " + e.getMessage(), e);
        }
    }
}
