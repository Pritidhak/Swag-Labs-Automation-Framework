package VE3Project.VE3;

import org.openqa.selenium.*;
import org.testng.Assert;
import org.testng.annotations.*;
import VE3Project.VE3.ExcelUtils;

import java.time.Duration;
import java.util.*;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SwagLabsTest extends BaseTest {

    @DataProvider(name = "loginData")
    public Object[][] getLoginData() throws Exception {
        ExcelUtils excel = new ExcelUtils("testdata/LoginData.xlsx", "Sheet1");
        int rows = excel.getRowCount();
        Object[][] data = new Object[rows][2];
        for (int i = 0; i < rows; i++) {
            data[i][0] = excel.getCellData(i, 0); 
            data[i][1] = excel.getCellData(i, 1); 
        }
        return data;
    }

    @Test(priority = 1, dataProvider = "loginData")
    public void homepageLoadVerification(String username, String password) {
        test = extent.createTest("Homepage Load Verification with user: " + username);

        driver.findElement(By.id("user-name")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);
        driver.findElement(By.id("login-button")).click();

        boolean homepageDisplayed = driver.findElement(By.className("inventory_list")).isDisplayed();
        Assert.assertTrue(homepageDisplayed, "Homepage is not displayed after login");
    }


    @Test(priority = 2)
    public void productFilteringLowToHigh() {
    	test = extent.createTest("productFilteringLowToHigh");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        WebElement sortDropdown = driver.findElement(By.className("product_sort_container"));
        sortDropdown.click();
        sortDropdown.findElement(By.cssSelector("option[value='lohi']")).click();

        List<WebElement> products = driver.findElements(By.className("inventory_item_name"));
        System.out.println("Products list after sorting Low to High:");
        for (WebElement product : products) {
            System.out.println(product.getText());
        }
    }

    @Test(priority = 3)
    public void cartOperations() {
    	test = extent.createTest("cartOperations");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        driver.findElement(By.className("product_sort_container")).click();
        driver.findElement(By.cssSelector("option[value='lohi']")).click();

        String firstProduct = driver.findElements(By.className("inventory_item_name")).get(0).getText();
        driver.findElements(By.cssSelector(".btn_inventory")).get(0).click();

        driver.findElement(By.className("shopping_cart_link")).click();
        String cartProduct = driver.findElement(By.className("inventory_item_name")).getText();

        Assert.assertEquals(cartProduct, firstProduct, "Product in cart is not match");
    }

    @Test(priority = 4)
    public void checkoutProcess() {
        test = extent.createTest("Checkout Process");

        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        driver.findElements(By.cssSelector(".btn_inventory")).get(0).click();
        driver.findElement(By.className("shopping_cart_link")).click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement checkoutBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("checkout")));
        checkoutBtn.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("first-name"))).sendKeys("Test");
        driver.findElement(By.id("last-name")).sendKeys("User");
        driver.findElement(By.id("postal-code")).sendKeys("12345");
        driver.findElement(By.id("continue")).click();

        wait.until(ExpectedConditions.elementToBeClickable(By.id("finish"))).click();

        Assert.assertTrue(driver.findElement(By.className("complete-header")).isDisplayed(),
                "Order confirmation is not displayed");
    }

}

