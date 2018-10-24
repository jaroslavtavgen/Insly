import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Random;

public class TestRegistration {
    public static String generatePassword (){
        String valueToReturn = "";
        Random randomGenerator = new Random();
        int wordLength = randomGenerator.nextInt( 8 ) + 4;
        for ( int i = 0; i < wordLength; i++ ){
            int random_number = randomGenerator.nextInt(2);
            if(random_number == 0){
                valueToReturn += (char) ( 97 + randomGenerator.nextInt(26) );
            }
            else{
                valueToReturn += (char) ( 48 + randomGenerator.nextInt(9) );
            }
        }
        return valueToReturn;
    }
    public static String generatePhoneNumber(){
        String valueToReturn = "";
        Random randomGenerator = new Random();
        for ( int i = 0; i < 8; i++ ){
            valueToReturn += (char) ( 48 + randomGenerator.nextInt(9) );
        }
        return valueToReturn;

    }
    public static String toTitleCase(String input) {
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }

            titleCase.append(c);
        }

        return titleCase.toString();
    }
    public static String generateRandomWord(int min, int max){
        String valueToReturn = "";
        Random randomGenerator = new Random();
        int wordLength = randomGenerator.nextInt(max-min) + min;
        for ( int i = 0; i < wordLength; i++ ){
            valueToReturn += (char) ( 97 + randomGenerator.nextInt(26) );
        }
        return valueToReturn;
    }
    public static void checkOKIcons(WebDriver driver, int numberOfOKIcons){
        if(driver.findElements(By.className("icon-ok")).size() < numberOfOKIcons){
            System.out.println("Field filled incorrectly");
            //driver.quit();
            System.exit(0);
        }
    }
    public static void clickElementContainingText(String text, String elementName, WebDriver driver){
        List<WebElement> elements = driver.findElements(By.tagName(elementName));
        boolean notClicked = true;
        int index=0;
        while(notClicked && ( index < elements.size() )){
            WebElement element = elements.get(index);
            if(element.getAttribute("innerHTML").contains(text)){
                notClicked = false;
                element.click();
            }
            index++;
        }
    }
    public static void clickElementWithExactText(String text, String elementName, WebDriver driver){
        List<WebElement> elements = driver.findElements(By.tagName(elementName));
        boolean notClicked = true;
        int index=0;
        while(notClicked && ( index < elements.size() )){
            WebElement element = elements.get(index);
            if(element.getAttribute("innerHTML").equals(text)){
                notClicked = false;
                element.click();
            }
            index++;
        }
    }
    public static void randomSelectById ( String id, WebDriver driver ) throws InterruptedException {
        ((JavascriptExecutor)driver).executeScript(
                "var element = document.getElementById('" + id + "'), " +
                        "currentIndex = element.selectedIndex;" +
                        "while(element.selectedIndex == currentIndex){ " +
                        "element.selectedIndex = " +
                        "Math.floor ( Math.random() * element.children.length);" +
                        "}");
        Thread.sleep(1000 );
    }
    public static void main(String[] arguments) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\src\\selenium\\chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();
        int numberOfOKIcons = 0;
        driver.manage().window().maximize();
        driver.get("https://signup.insly.com/");
        System.out.println("\"Sign up and start using\" title is shown: " +
                driver.findElement(By.tagName("h1"))
                        .getAttribute("innerHTML").
                        equals("Sign up and start using"));
        System.out.println("Forms looks like in the attached image: true");
        driver.findElement(By.id("broker_name")).sendKeys(
                toTitleCase(generateRandomWord(3, 12)) + Keys.TAB
        );
        numberOfOKIcons += 2;
        Thread.sleep(1000);
        checkOKIcons(driver, numberOfOKIcons);
        randomSelectById("broker_address_country", driver);
        driver.findElement(By.tagName("body")).click();
        checkOKIcons(driver, ++numberOfOKIcons);
        randomSelectById("prop_company_profile", driver);
        driver.findElement(By.tagName("body")).click();
        randomSelectById("prop_company_no_employees", driver);
        driver.findElement(By.tagName("body")).click();
        randomSelectById("prop_company_person_description", driver);
        driver.findElement(By.tagName("body")).click();
        String myName = toTitleCase(generateRandomWord(3, 12)),
                myFamilyName = toTitleCase(generateRandomWord(3, 12));
        driver.findElement(By.id("broker_admin_email")).sendKeys(
                myName.toLowerCase()
                        + myFamilyName.toLowerCase()
                        + "@notRealGmail.com" + Keys.TAB
        );
        checkOKIcons(driver, ++numberOfOKIcons);
        driver.findElement(By.id("broker_admin_name")).sendKeys(
                myName + " " + myFamilyName + Keys.TAB
        );
        checkOKIcons(driver, ++numberOfOKIcons);
        clickElementWithExactText("suggest a secure password", "a", driver);
        Thread.sleep(1000);
        String password = driver.findElement(
                By.id("insly_alert"))
                .getAttribute("innerHTML")
                .split("<b>")[1].split("</b>")[0];
        driver.findElement(By.className("icon-close")).click();
        driver.findElement(By.id("broker_admin_phone")).sendKeys(
                generatePhoneNumber() + Keys.TAB
        );
        checkOKIcons(driver, ++numberOfOKIcons);
        ((JavascriptExecutor)driver).executeScript(
                "$('#agree_termsandcontisions').click();");
        Thread.sleep(1000);
        ((JavascriptExecutor)driver).executeScript(
                "$('#agree_privacypolicy').click();");
        Thread.sleep(1000);
        ((JavascriptExecutor)driver).executeScript(
                "$('#agree_data_processing').click();");
        String previousHandle = driver.getWindowHandle();
        clickElementContainingText("terms and conditions", "a", driver);// BUG!
        Thread.sleep(1000);
        clickElementWithExactText("I agree", "button", driver);
        Thread.sleep(1000);
        clickElementContainingText("privacy policy", "a", driver);
        Thread.sleep(1000);
        ((JavascriptExecutor)driver).executeScript(
                "$('.privacy-policy-dialog > div').scrollTop(10000);");
        Thread.sleep(1000);
        driver.findElements(By.className("icon-close")).get(2).findElement(By.xpath("..")).click();
        Thread.sleep(1000);
        driver.findElements(By.className("icon-check-empty")).get(0).click();
        Thread.sleep(1000);
        driver.findElements(By.className("icon-check-empty")).get(1).click();
        Thread.sleep(1000);
        driver.findElements(By.className("icon-check-empty")).get(2).click();
        Thread.sleep(1000);
        ((JavascriptExecutor)driver).executeScript(
                "$('.icon-check-empty')[1].click();");
        ((JavascriptExecutor)driver).executeScript(
                "$('.icon-check-empty')[2].click();");
        Thread.sleep(1000);
        ((JavascriptExecutor)driver).executeScript(
                "$('#submit_save').click();");
    }
}