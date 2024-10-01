package org.example.parsingcommitsstatistic;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField inputUrl;

    @FXML
    protected void onHelloButtonClick() {

        String url = inputUrl.getText();

        WebDriver driver = new ChromeDriver();
        driver.get(url);

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("td.ContributionCalendar-day")));
            List<WebElement> contributionDays = driver.findElements(By.cssSelector("td.ContributionCalendar-day"));

            List<Quote> quotes = new ArrayList<>();

            for (WebElement day : contributionDays) {
                Quote quote = new Quote();
                String date = day.getAttribute("data-date");
                String level = day.getAttribute("data-level");

                if (!level.equals("0") && !level.isEmpty()){
                    quote.setDate(date);
                    quote.setLevel(level);
                    quotes.add(quote);
                }
            }

            String[] splitName = url.split("/");

            File csvFile = new File("output_" + splitName[splitName.length-1] + ".csv");

            try (PrintWriter pw = new PrintWriter(csvFile)) {
                for (var quote : quotes) {
                    List<String> row = new ArrayList<>();
                    row.add(quote.getData());
                    row.add(quote.getLevel());
                    pw.write(String.join(",", row) + "\n");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        welcomeText.setText("Данные были записаны");
    }
}