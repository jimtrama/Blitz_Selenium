import org.openqa.selenium.chrome.*;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.lang.Thread;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Logic {
    public ChromeDriver driver;
    public String play_handle;
    public String solve_window;
    public WebDriver solvingDriverWindow;


    public Logic(String name,boolean rankedGame) {
        System.setProperty("webdriver.chrome.driver", "./src/chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        openSolveWindow();
        openFbWindowAndLogin();
        startGameWith(name,rankedGame);

    }

    public void openFbWindowAndLogin() {
        driver.executeScript("window.open('');");
        try {
            Thread.sleep(2000);
        } catch (Exception e) {

        }

        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        driver.get("https://facebook.com");
        play_handle = driver.getWindowHandle();
        try {
            driver.findElementByXPath("//*[@data-testid='cookie-policy-dialog-accept-button']").click();
            ExecuteLogin();
        } catch (Exception e) {
            ExecuteLogin();
        }
    }

    public void ExecuteLogin() {
        driver.findElementById("email").sendKeys("");
        driver.findElementById("pass").sendKeys("");
        driver.findElementByXPath("//*[@data-testid='royal_login_button']").click();
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }

        driver.get("https://www.facebook.com/instantgames/2211386328877300/");
    }

    public void openSolveWindow() {

        driver.get("https://www.wordblitz.gr/solver.html");
        solve_window = driver.getWindowHandle();
    }

    public ArrayList<String> getCorrectWords(ArrayList<String> letters) {
        HashMap<String, String> map = getHelpingMap();

        ArrayList<String> correctWords = new ArrayList<>();
        if (!driver.getWindowHandle().equals(solve_window)) {
            driver.switchTo().window(solve_window);
        }
        WebElement inputElement = driver.findElementByClassName("square");
        for (String letter : letters) {
            inputElement.sendKeys(map.get(letter));
        }
        driver.findElementByTagName("button").click();
        List<WebElement> words = driver.findElementsByClassName("word");
        for (WebElement word : words) {
            if (word.getText().length() > 1)
                correctWords.add(word.getText());
        }

        return correctWords;
    }

    public void startGameWith(String username,boolean rankedGame) {
        boolean flag = true;
        WebElement frame1 = null;
        while (flag) {
            try {
                frame1 = driver.findElementsByTagName("iframe").get(0);
                flag = false;
            } catch (Exception e) {
                System.out.println("tryoing1111");
            }

        }
        try {
            Thread.sleep(6000);
        } catch (Exception r) {
        }
        WebDriver driverOuter = driver.switchTo().frame(frame1);
        WebElement frame2 = null;
        flag = true;
        while (flag) {
            try {
                frame2 = driverOuter.findElements(By.tagName("iframe")).get(4);
                flag = false;
            } catch (Exception e) {
                try {
                    Thread.sleep(5000);
                } catch (Exception ed) {

                }
                System.out.println("tryoing2222");
            }

        }


        if (rankedGame) {
            WebDriver driverInner = driverOuter.switchTo().frame(frame2);
            driverInner.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            System.out.println("rank game");
            driverInner.findElements(By.xpath("//div[@class='blitz-challenge-card darkener']")).get(0).click();
            WebDriver startGameDriver = driverOuter.switchTo().parentFrame();
            try {
                Thread.sleep(1000);
                try {

                } catch (Exception ef) {
                }
                System.out.println("clicked");
                //

                WebElement nextiframe = startGameDriver.findElements(By.tagName("iframe")).get(4);
                WebDriver continueDriver = startGameDriver.switchTo().frame(nextiframe);
                continueDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                boolean gameFound = false;
                int counter = 0;
                System.out.println(continueDriver.getPageSource());
                while(!gameFound){
                    try {
                        Thread.sleep(2000);
                        List<WebElement> testing = continueDriver.findElements(By.xpath(".//div[@class='footer flex-column']"));
                        System.out.println(testing.size());
                        if (testing.size()!=0) {
                            gameFound = false;
                        } else {
                            gameFound = true;
                        }
                    }
                    catch (Exception d){
                        d.printStackTrace();
                        counter++;
                        if(counter==3)
                            gameFound=true;
                    }
                }
                try {

                    System.out.println("bika");
                    driver.switchTo().defaultContent();
                    WebDriver solv = driver.switchTo().frame(driver.findElementsByTagName("iframe").get(0));
                    WebDriver solvingDriver = solv.switchTo().frame(solv.findElements(By.tagName("iframe")).get(4));
                    solvingDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                    solvingDriverWindow = solvingDriver;
                    List<WebElement> letters = solvingDriver.findElements(By.className("letter"));
                    System.out.println("---------------------------");
                    for (WebElement f : letters) {
                        System.out.println(f.getText());
                    }
                    System.out.println("---------------------------");
                    ArrayList<String> lettersToSend = new ArrayList<>();
                    ArrayList<String> mapOFLetters = new ArrayList<>();
                    ArrayList<WebElement> mapOFLettersWeb = new ArrayList<>();
                    for (WebElement letter : letters) {
                        lettersToSend.add(letter.getText());
                        mapOFLetters.add(letter.getText());
                        mapOFLettersWeb.add(letter);
                    }
                    ArrayList<String> answers = getCorrectWords(lettersToSend);
                    if (!driver.getWindowHandle().equals(play_handle)) {
                        driver.switchTo().window(play_handle);
                        System.out.println("switced back");
                    } else {
                        System.out.println("edoooooooooooooooooooooooooooooooooooooooooooooo");
                    }

                    solvePuzzle(answers, mapOFLetters, mapOFLettersWeb, solvingDriver);

                } catch (Exception ed) {
                    ed.printStackTrace();
                    System.out.println("not found");

                }
            } catch (Exception re) {
                re.printStackTrace();
            }
        } else {
            System.out.println("Start");
            try{
                Thread.sleep(20000);
            }catch (Exception e){
                System.out.println("Error in Slepp");
            }
            System.out.println("Stop");
            WebDriver driverInner = driverOuter.switchTo().frame(frame2);
            WebElement app = driverInner.findElement(By.id("app"));
            List<WebElement> users = app.findElements(By.xpath(".//div[@class='cell-game clickable']"));
            System.out.println(users.size());
            for (WebElement possibleGame : users) {
                System.out.println(possibleGame.findElement(By.xpath(".//div[@class='cell-title']/span")).getText().trim());
                if (possibleGame.findElement(By.xpath(".//div[@class='cell-title']/span")).getText().trim().equals(username)) {
                    possibleGame.findElement(By.xpath(".//div[@class='button-round clickable btn-go']")).click();
                    WebDriver startGameDriver = driverOuter.switchTo().parentFrame();
                    try {
                        Thread.sleep(2000);
                        startGameDriver.findElement(By.xpath("//div[@role='button']")).click();
                        System.out.println("clicked");
                        //
                        Thread.sleep(4000);
                        WebElement nextiframe = startGameDriver.findElements(By.tagName("iframe")).get(4);
                        WebDriver continueDriver = startGameDriver.switchTo().frame(nextiframe);

                        try {
                            System.out.println("bika");
                            continueDriver.findElement(By.className("button-primary")).click();
                            WebDriver solv = continueDriver.switchTo().parentFrame();
                            WebDriver solvingDriver = solv.switchTo().frame(solv.findElements(By.tagName("iframe")).get(4));
                            solvingDriverWindow = solvingDriver;
                            List<WebElement> letters = solvingDriver.findElements(By.className("letter"));
                            System.out.println("---------------------------");
                            for (WebElement f : letters) {
                                System.out.println(f.getText());
                            }
                            System.out.println("---------------------------");
                            ArrayList<String> lettersToSend = new ArrayList<>();
                            ArrayList<String> mapOFLetters = new ArrayList<>();
                            ArrayList<WebElement> mapOFLettersWeb = new ArrayList<>();
                            for (WebElement letter : letters) {
                                lettersToSend.add(letter.getText());
                                mapOFLetters.add(letter.getText());
                                mapOFLettersWeb.add(letter);
                            }
                            ArrayList<String> answers = getCorrectWords(lettersToSend);
                            if (!driver.getWindowHandle().equals(play_handle)) {
                                driver.switchTo().window(play_handle);
                                System.out.println("switced back");
                            } else {
                                System.out.println("edoooooooooooooooooooooooooooooooooooooooooooooo");
                            }

                            solvePuzzle(answers, mapOFLetters, mapOFLettersWeb, solvingDriver);

                        } catch (Exception ed) {
                            System.out.println("not found");

                        }


                        break;
                    } catch (Exception e) {

                    }

                    break;
                }
            }

        }


    }

    private void solvePuzzle(ArrayList<String> answers, ArrayList<String> mapOFLetters, ArrayList<WebElement> mapOFLetterWeb, WebDriver solveDriver) {

        System.out.println("bika kai ekana DEN switch");
        driver.switchTo().defaultContent();
        driver.switchTo().window(play_handle);
        driver.switchTo().defaultContent();
        WebDriver something = driver.switchTo().frame(driver.findElementsByTagName("iframe").get(0));
        WebDriver finalDriver = something.switchTo().frame(something.findElements(By.tagName("iframe")).get(4));
        Actions action = new Actions(finalDriver);

        System.out.println("kati kati kati");
        for (String answer : answers) {
            System.out.println("Start Solving Word");
            ArrayList<String> letters = new ArrayList<>(Arrays.asList(answer.split("")));
            ArrayList<Integer> solutionToPerform = reqSolveWord(0, letters, mapOFLetters, new ArrayList<>());
            System.out.println("solution size :" + solutionToPerform.size() + "----------------------------");


            if (solutionToPerform.size() == 2) {
                System.out.println("2");
                action.clickAndHold(mapOFLetterWeb.get(solutionToPerform.get(0)))
                        .moveToElement((mapOFLetterWeb.get(solutionToPerform.get(1)))).release()
                        .build().perform();
            } else if (solutionToPerform.size() == 3) {
                System.out.println("3");
                action.clickAndHold(mapOFLetterWeb.get(solutionToPerform.get(0)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(1)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(2))).release()
                        .build().perform();
            } else if (solutionToPerform.size() == 4) {
                System.out.println("4");
                action.clickAndHold(mapOFLetterWeb.get(solutionToPerform.get(0)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(1)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(2)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(3))).release()
                        .build().perform();
            } else if (solutionToPerform.size() == 5) {

                action.clickAndHold(mapOFLetterWeb.get(solutionToPerform.get(0)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(1)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(2)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(3)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(4))).release()
                        .build().perform();
            } else if (solutionToPerform.size() == 6) {
                action.clickAndHold(mapOFLetterWeb.get(solutionToPerform.get(0)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(1)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(2)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(3)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(4)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(5))).release()
                        .build().perform();
            } else if (solutionToPerform.size() == 7) {
                System.out.println("7");
                action.clickAndHold(mapOFLetterWeb.get(solutionToPerform.get(0)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(1)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(2)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(3)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(4)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(5)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(6))).release()
                        .build().perform();
            } else if (solutionToPerform.size() == 8) {
                System.out.println("8");
                action.clickAndHold(mapOFLetterWeb.get(solutionToPerform.get(0)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(1)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(2)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(3)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(4)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(5)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(6)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(7))).release()
                        .build().perform();
            } else if (solutionToPerform.size() == 9) {
                System.out.println("9");
                action.clickAndHold(mapOFLetterWeb.get(solutionToPerform.get(0)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(1)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(2)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(3)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(4)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(5)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(6)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(7)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(8))).release()
                        .build().perform();
            } else if (solutionToPerform.size() == 10) {
                System.out.println("10");
                action.clickAndHold(mapOFLetterWeb.get(solutionToPerform.get(0)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(1)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(2)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(3)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(4)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(5)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(6)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(7)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(8)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(9))).release()
                        .build().perform();
            } else if (solutionToPerform.size() == 11) {
                action.clickAndHold(mapOFLetterWeb.get(solutionToPerform.get(0)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(1)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(2)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(3)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(4)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(5)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(6)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(7)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(8)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(9)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(10))).release()
                        .build().perform();
            } else if (solutionToPerform.size() == 12) {
                action.clickAndHold(mapOFLetterWeb.get(solutionToPerform.get(0)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(1)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(2)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(3)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(4)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(5)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(6)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(7)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(8)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(9)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(10)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(11))).release()
                        .build().perform();
            } else if (solutionToPerform.size() == 13) {
                action.clickAndHold(mapOFLetterWeb.get(solutionToPerform.get(0)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(1)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(2)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(3)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(4)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(5)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(6)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(7)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(8)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(9)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(10)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(11)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(12))).release()
                        .build().perform();
            } else if (solutionToPerform.size() == 14) {
                action.clickAndHold(mapOFLetterWeb.get(solutionToPerform.get(0)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(1)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(2)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(3)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(4)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(5)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(6)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(7)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(8)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(9)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(10)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(11)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(12)))
                        .moveToElement(mapOFLetterWeb.get(solutionToPerform.get(13))).release()
                        .build().perform();
            } else {
                System.out.println("Something Went Wrong Performing Action");

            }
            System.out.println("next Ansewr");
        }
    }

    public ArrayList<Integer> reqSolveWord(
            int letterIamIn, ArrayList<String> word, ArrayList<String> board, ArrayList<ArrayList<Integer>> paths) {
        for (ArrayList<Integer> path : paths) {
            if (path.size() == word.size()) {
                return path;

            }
        }
        if (letterIamIn == 0) {
            for (int i = 0; i < 16; i++) {
                if (board.get(i).equals(word.get(0))) {
                    paths.add(new ArrayList<>());
                    paths.get(paths.size() - 1).add(i);
                }
            }

            return reqSolveWord(1, word, board, paths);
        } else {
            ArrayList<ArrayList<Integer>> temp = new ArrayList<>();
            for (ArrayList<Integer> path : paths) {
                ArrayList<Integer> neigbours = getNeighbours(path.get(path.size() - 1));
                for (int neigbour : neigbours) {
                    if (word.get(letterIamIn).equals(board.get(neigbour)) && !path.contains(neigbour)) {
                        temp.add(new ArrayList<>());
                        for (int position : path) {
                            temp.get(temp.size() - 1).add(position);
                        }
                        temp.get(temp.size() - 1).add(neigbour);
                    }
                }
            }
            letterIamIn++;
            return reqSolveWord(letterIamIn, word, board, temp);
        }
    }

    public ArrayList<Integer> getNeighbours(int pos) {
        ArrayList<Integer> toReturn = new ArrayList<>();
        for (int i = pos / 4 - 1; i < pos / 4 + 2; i++) {
            for (int j = pos % 4 - 1; j < pos % 4 + 2; j++) {
                if (i >= 0 && i < 4 && j < 4 && j >= 0 && (i * 4 + j) != pos) {
                    toReturn.add(i * 4 + j);
                }
            }
        }
        return toReturn;
    }

    public HashMap<String, String> getHelpingMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put("Α", "a");
        map.put("Β", "b");
        map.put("Γ", "g");
        map.put("Δ", "d");
        map.put("Ε", "e");
        map.put("Ζ", "z");
        map.put("Η", "h");
        map.put("Θ", "u");
        map.put("Ι", "i");
        map.put("Κ", "k");
        map.put("Λ", "l");
        map.put("Μ", "m");
        map.put("Ν", "n");
        map.put("Ξ", "j");
        map.put("Ο", "o");
        map.put("Π", "p");
        map.put("Ρ", "r");
        map.put("Σ", "s");
        map.put("Τ", "t");
        map.put("Υ", "y");
        map.put("Φ", "f");
        map.put("Χ", "x");
        map.put("Ψ", "c");
        map.put("Ω", "v");
        return map;
    }

}
