import java.io.*;
import java.util.*;

public class Stonks {
    public static Map<String, Integer> ownedStocks = new HashMap<>();
    public static File saveFolder = new File("saves");
    public static File key = new File(saveFolder + File.separator + "key.txt");
    public static File save;
    public static void main(String[] args) throws InterruptedException, IOException {
        boolean buy = true;
        double cash = 2000;
        boolean load = false;
        int cCashUpdate = 0;
        List<StockConstructor> stonkList = new ArrayList<>();
        List<AIConsumer> consumers = new ArrayList<>();
        Random rand = new Random();
        int consumerAmount = 500;

        if (!saveFolder.exists()){
            saveFolder.mkdir();
        }

        Scanner scan = new Scanner(System.in);
        ArrayList<String> savePaths = new ArrayList<>();
        if (key.exists()) {
            StringBuilder last = new StringBuilder();
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(key.getAbsolutePath()))) {
                String line = bufferedReader.readLine();
                while (line != null) {
                    last.append(line);
                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                // Exception handling
            }
            String str = last.substring(0);
            String path;

            while (true) {
                if (str.substring(6).contains("saves/")){
                    path = str.substring(0,str.substring(6).indexOf("saves/") + 6);
                } else {
                    path = str;
                    savePaths.add(path);
                    System.out.println(path);
                    break;
                }
                savePaths.add(path);
                System.out.println(path);
                str = str.substring(path.length());
                }
            savePaths.remove("");
            boolean initialized = false;
            while (true) {
                for (int i = 0; i < savePaths.size(); i++){
                    System.out.println("[" + i + "] " + savePaths.get(i).substring(6, savePaths.get(i).length() - 4));
                }

                System.out.println("Which save should be loaded? (\"N\" to create a new save)");
                String ans = scan.next();
                if (ans.equalsIgnoreCase("n")){
                    System.out.println("What do you want to name this save?");
                    ans = scan.next();
                    save = new File(saveFolder + File.separator + ans + ".txt");
                    System.out.println("Created new Save!");
                    StringBuilder last2 = new StringBuilder();
                    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(key.getAbsolutePath()))) {
                        String line = bufferedReader.readLine();
                        while (line != null) {
                            last2.append(line);
                            line = bufferedReader.readLine();
                        }
                    } catch (IOException e) {
                        // Exception handling
                    }
                    try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(key.getAbsolutePath()))) {
                        bufferedWriter.write(last2.toString());
                        bufferedWriter.write(save.getPath());
                    } catch (IOException e) {
                        // Exception handling
                    }
                    StockConstructor cars = new StockConstructor("Car", 1, 100, 10, 50);
                    StockConstructor phone = new StockConstructor("Phone", 2, 50, 50, 100);
                    StockConstructor pencils = new StockConstructor("Pencil", 1, 500, 25, 75);
                    StockConstructor clowns = new StockConstructor("Clown", 1, 100, 100, 50);
                    StockConstructor plane = new StockConstructor("Plane", 0.25, 50, 100, 100);
                    stonkList.add(cars);
                    stonkList.add(phone);
                    stonkList.add(pencils);
                    stonkList.add(clowns);
                    stonkList.add(plane);
                    for (StockConstructor stonk : stonkList){
                        ownedStocks.put(stonk.getName(), 0);
                    }
                    for (int y = 0; y < consumerAmount; y++) {
                        consumers.add(new AIConsumer(rand.nextInt(10), rand.nextInt(100) + 1, rand.nextInt(100) + 1));
                    }
                    try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(save.getAbsolutePath()))) {
                        for (StockConstructor stock : stonkList){
                            bufferedWriter.write("&s{" + stock.getName() + "}{" + stock.getMultiplier() + "}{" + stock.getAvailability() + "}{" + stock.getChange() + "}{" + stock.getValue() + "}");
                        }
                        for (AIConsumer consumer : consumers){
                            bufferedWriter.write("&c{" + consumer.getWage() + "}{" + consumer.getBuyPercent() + "}{" + consumer.getSellPercent() + "}{" + consumer.getCash() + "}");
                        }
                    } catch (IOException e) {
                        // Exception handling
                    }
                    runAmount(100, stonkList, consumers);

                    // save stuff to the file

                    break;
                } else {
                    int num = Integer.parseInt(ans);
                    if (num < savePaths.size()){
                        save = new File(savePaths.get(num));
                        System.out.println("Loading...");
                        load = true;
                        buy = false;
                        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(save.getAbsolutePath()))) {
                            String line2 = bufferedReader.readLine();
                            String cut;
                            while(line2 != null) {
                                //System.out.println(line2.substring(1).indexOf(' '));
                                cut = line2.substring(1);
                                String line = cut.substring(0, cut.indexOf('&'));

                                while (true) {
                                    if (line.length() > 0) {
                                        if (line.charAt(0) == 's') {
                                            //stock "s{" + stock.getName() + "}{" + stock.getMultiplier() + "}{" + stock.getAvailability() + "}{" + stock.getChange() + "}"
                                            String name = line.substring(2, line.indexOf('}'));
                                            String multiplier = line.substring(4 + name.length(), line.substring(4 + name.length()).indexOf('}') + 4 + name.length());
                                            String availability = line.substring(6 + name.length() + multiplier.length(), line.substring(6 + name.length() + multiplier.length()).indexOf('}') + 6 + name.length() + multiplier.length());
                                            String change = line.substring(8 + name.length() + multiplier.length() + availability.length(), line.substring(8 + name.length() + multiplier.length() + availability.length()).indexOf('}') + 8 + name.length() + multiplier.length() + availability.length());
                                            String startValue = line.substring(10 + name.length() + multiplier.length() + availability.length() + change.length(), line.substring(10 + name.length() + multiplier.length() + availability.length() + change.length()).indexOf('}') + 10 + name.length() + multiplier.length() + availability.length() + change.length());
                                            stonkList.add(new StockConstructor(name, Double.parseDouble(multiplier), Integer.parseInt(availability), Integer.parseInt(change), Double.parseDouble(startValue)));
                                            ownedStocks.put(name, 0);
                                        } else if (line.charAt(0) == 'c') {
                                            //consumer "c{" + consumer.getWage() + "}{" + consumer.getBuyPercent() + "}{" + consumer.getSellPercent() + "}{" + consumer.getCash() + "}"
                                            String wage = line.substring(2, line.indexOf('}'));
                                            String buyPer = line.substring(4 + wage.length(), line.substring(4 + wage.length()).indexOf('}') + 4 + wage.length());
                                            String sellPer = line.substring(6 + wage.length() + buyPer.length(), line.substring(6 + wage.length() + buyPer.length()).indexOf('}') + 6 + wage.length() + buyPer.length());
                                            String cCash = line.substring(8 + wage.length() + buyPer.length() + sellPer.length(), line.substring(8 + wage.length() + buyPer.length() + sellPer.length()).indexOf('}') + 8 + wage.length() + buyPer.length() + sellPer.length());
                                            AIConsumer consumer = new AIConsumer(Double.parseDouble(wage), Integer.parseInt(buyPer), Integer.parseInt(sellPer));
                                            consumer.setCash(Double.parseDouble(cCash));
                                            // add thing here for keeping stocks
                                            consumers.add(consumer);
                                        } else if (line.charAt(0) == 'u') {
                                            //update "u{" + stock.getName() + "}{" + stock.getValue() + "}{" + stock.getAvailability() + "}"
                                            String name = line.substring(2, line.indexOf('}'));
                                            String value = line.substring(4 + name.length(), line.substring(4 + name.length()).indexOf('}') + 4 + name.length());
                                            String availability = line.substring(6 + name.length() + value.length(), line.substring(6 + name.length() + value.length()).indexOf('}') + 6 + name.length() + value.length());
                                            for (StockConstructor stonk : stonkList) {
                                                if (stonk.getName().equalsIgnoreCase(name)) {
                                                    stonk.addRead(Double.parseDouble(value), Integer.parseInt(availability));
                                                    break;
                                                }
                                            }

                                        } else if (line.charAt(0) == 'i') {
                                            if (consumers.size() > 0) {
                                                // " i{" + consumer.getCash() + "}"
                                                String cashAmount = line.substring(2, line.indexOf('}'));
                                                if (cCashUpdate >= consumers.size()) {
                                                    cCashUpdate = 0;
                                                }
                                                consumers.get(cCashUpdate).setCash(Double.parseDouble(cashAmount));
                                                cCashUpdate++;
                                            }
                                        } else if (line.charAt(0) == 'b'){
                                            // "&b{" + stonk.getName() + "}{" + buyAmount + "}{" + cash + "}"
                                            String name = line.substring(2, line.indexOf('}'));
                                            String amount = line.substring(4 + name.length(), line.substring(4 + name.length()).indexOf('}') + 4 + name.length());
                                            String cashAmount = line.substring(6 + name.length() + amount.length(), line.substring(6 + name.length() + amount.length()).indexOf('}') + 6 + name.length() + amount.length());
                                            for (StockConstructor stonk : stonkList) {
                                                if (stonk.getName().equalsIgnoreCase(name)) {
                                                    for (int i = 0; i < Integer.parseInt(amount); i++){
                                                        ownedStocks.replace(stonk.getName(), ownedStocks.get(stonk.getName())+1);
                                                    }
                                                    break;
                                                }
                                            }
                                            cash = Double.parseDouble(cashAmount);

                                        } else if (line.charAt(0) == 'n'){
                                            // "&n{" + stonk.getName() + "}{" + buyAmount + "}{" + cash + "}"
                                            String name = line.substring(2, line.indexOf('}'));
                                            String amount = line.substring(4 + name.length(), line.substring(4 + name.length()).indexOf('}') + 4 + name.length());
                                            String cashAmount = line.substring(6 + name.length() + amount.length(), line.substring(6 + name.length() + amount.length()).indexOf('}') + 6 + name.length() + amount.length());
                                            for (StockConstructor stonk : stonkList) {
                                                if (stonk.getName().equalsIgnoreCase(name)) {
                                                    for (int i = 0; i < Integer.parseInt(amount); i++){
                                                        ownedStocks.replace(stonk.getName(), ownedStocks.get(stonk.getName())-1);
                                                    }
                                                    break;
                                                }
                                            }
                                            cash = Double.parseDouble(cashAmount);

                                        }
                                        else {
                                            System.out.println("EMPTY");
                                            break;
                                        }
                                    } else {
                                        System.out.println("empty string");
                                    }
                                    cut = cut.substring(line.length() + 1);
                                    if (!cut.contains("&")){
                                        System.out.println("Finished Loading");
                                        initialized = true;
                                        break;
                                    } else {
                                        line = cut.substring(0, cut.indexOf("&"));
                                    }

                                }
                                line2 = bufferedReader.readLine();
                            }
                        } catch (IOException e) {
                            // Exception handling
                        }
                    }
                }
                if (initialized){
                    break;
                }
            }
        } else {
            key.createNewFile();
            System.out.println("What do you want to name this save?");
            String ans;
            ans = scan.next();
            save = new File(saveFolder + File.separator + ans + ".txt");
            System.out.println("Created new Save!");
            StringBuilder last = new StringBuilder();
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(key.getAbsolutePath()))) {
                String line = bufferedReader.readLine();
                while (line != null) {
                    last.append(line);
                    line = bufferedReader.readLine();
                }
            } catch (IOException e) {
                // Exception handling
            }
            try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(key.getAbsolutePath()))) {
                bufferedWriter.write(last.toString());
                bufferedWriter.write(save.getPath());
            } catch (IOException e) {
                // Exception handling
            }
            StockConstructor cars = new StockConstructor("Car", 1, 100, 10, 50);
            StockConstructor phone = new StockConstructor("Phone", 2, 50, 50, 100);
            StockConstructor pencils = new StockConstructor("Pencil", 1, 500, 25, 75);
            StockConstructor clowns = new StockConstructor("Clown", 1, 100, 100, 50);
            StockConstructor plane = new StockConstructor("Plane", 0.25, 50, 100, 100);
            stonkList.add(cars);
            stonkList.add(phone);
            stonkList.add(pencils);
            stonkList.add(clowns);
            stonkList.add(plane);
            for (StockConstructor stonk : stonkList){
                ownedStocks.put(stonk.getName(), 0);
            }
            for (int y = 0; y < consumerAmount; y++) {
                consumers.add(new AIConsumer(rand.nextInt(10), rand.nextInt(100) + 1, rand.nextInt(100) + 1));
            }
            try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(save.getAbsolutePath()))) {
                for (StockConstructor stock : stonkList){
                    bufferedWriter.write("&s{" + stock.getName() + "}{" + stock.getMultiplier() + "}{" + stock.getAvailability() + "}{" + stock.getChange() + "}{" + stock.getValue() + "}");
                }
                for (AIConsumer consumer : consumers){
                    bufferedWriter.write("&c{" + consumer.getWage() + "}{" + consumer.getBuyPercent() + "}{" + consumer.getSellPercent() + "}{" + consumer.getCash() + "}");
                }
            } catch (IOException e) {
                // Exception handling
            }
            runAmount(100, stonkList, consumers);
        }



        String lastStonkBought = "";



        for (StockConstructor stonk : stonkList) {
            stonk.displayConnectedGraph();
        }
        while (true) {
            String answer = "";
            String stonkName = "";
            if (buy) {
                while (true) {
                    String cashval = cash + "";
                    if (cashval.substring(cashval.indexOf(".")).length() > 4)
                        cashval = cashval.substring(0, cashval.indexOf(".") + 3);
                    System.out.println("Cash: $" + cashval);
                    System.out.println("Type a stock name to buy or \"N\" to not buy any stocks");
                    answer = scan.next();
                    if (answer.equalsIgnoreCase("N")) {
                        System.out.println("You chose not to buy a stock.");
                        break;
                    } else {
                        for (StockConstructor stonk : stonkList) {
                            if (stonk.getName().equalsIgnoreCase(answer)) {
                                System.out.println("You Chose " + stonk.getName() + "!");
                                lastStonkBought = stonk.getName();
                                stonkName = stonk.getName();
                                break;
                            }
                        }
                        if (stonkName.equals("")) {
                            System.out.println("Did not find that stock.");
                        } else {
                            break;
                        }
                    }
                }
            } else {
                answer = "N";
            }

            String ans = "";
            int buyAmount = 0;
            if (buy) {
                while (true) {
                    if (answer.equalsIgnoreCase("N")) {
                        break;
                    } else {
                        System.out.println("How many shares do you want to buy?");
                        ans = scan.next();
                        int num = Integer.parseInt(ans);
                        if (num > 0) {
                            for (StockConstructor stonk : stonkList) {
                                if (stonk.getName().equalsIgnoreCase(answer)) {
                                    if (stonk.getValue() * num <= cash) {
                                        System.out.println("You Bought " + num + " stocks of " + stonk.getName());
                                        buyAmount = num;
                                        cash -= stonk.getValue() * num;
                                        stonk.buyStock(buyAmount);
                                        lastStonkBought = stonk.getName();
                                        for (int i = 0; i < num; i++) {
                                            ownedStocks.replace(stonk.getName(), ownedStocks.get(stonk.getName()) + 1);
                                        }

                                        StringBuilder last = new StringBuilder();
                                        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(save.getAbsolutePath()))) {
                                            String line = bufferedReader.readLine();
                                            while (line != null) {
                                                last.append(line);
                                                line = bufferedReader.readLine();
                                            }
                                        } catch (IOException e) {
                                            // Exception handling
                                        }
                                        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(save.getAbsolutePath()))) {
                                            bufferedWriter.write(last.toString());
                                            bufferedWriter.write("&b{" + stonk.getName() + "}{" + buyAmount + "}{" + cash + "}");
                                        } catch (IOException e) {
                                            // Exception handling
                                        }
                                    } else {
                                        System.out.println("You do not have enough cash!");
                                    }
                                    break;
                                }
                            }
                            if (buyAmount > 0) {
                                break;
                            }
                        } else {
                            System.out.println("You have to buy at least 1!");
                        }

                    }
                }
            }
            buy = false;
            if (!load) {
                while (true) {
                    System.out.println("how many days do you want to wait?");
                    ans = scan.next();
                    System.out.println("------------------------------------------------------------------");
                    System.out.println("                                                                  ");
                    System.out.println("                                                                  ");
                    System.out.println("                                                                  ");
                    System.out.println("                                                                  ");
                    System.out.println("                                                                  ");
                    System.out.println("                                                                  ");
                    System.out.println("                                                                  ");
                    System.out.println("------------------------------------------------------------------");
                    int num = Integer.parseInt(ans);
                    if (num > 0) {
                        for (int i = 0; i < 15; i++) {
                            long start = System.currentTimeMillis();
                            if (i > 0) {
                                if (num < 15) {
                                    runAmount(num, stonkList, consumers);
                                    i = 15;
                                } else {
                                    runAmount(num / 15, stonkList, consumers);
                                }
                            } else {
                                runAmount(num % 15, stonkList, consumers);
                            }

                            //if (!(answer.equalsIgnoreCase("N"))) {
                            for (StockConstructor stonk : stonkList) {
                                if (lastStonkBought.equalsIgnoreCase(stonk.getName())) {
                                    for (int y = 0; y < 25; y++)
                                        System.out.println("");
                                    stonk.displayConnectedGraph();
                                    break;
                                }
                            }
                            long end = System.currentTimeMillis() - start;
                            if (end < 1000) {
                                Thread.sleep(1000 - end);
                            }
                            //}
                        }
                        System.out.println("------------------------------------------------------------------");
                        System.out.println("                                                                  ");
                        System.out.println("                                                                  ");
                        System.out.println("                                                                  ");
                        System.out.println("                                                                  ");
                        System.out.println("                                                                  ");
                        System.out.println("                                                                  ");
                        System.out.println("                                                                  ");
                        System.out.println("------------------------------------------------------------------");
                        for (StockConstructor stonk : stonkList) {
                            stonk.displayConnectedGraph();
                        }
                        break;
                    } else {
                        System.out.println("You have to wait at least 1 day!");
                    }
                }
            } else {
                for (StockConstructor stonk : stonkList) {
                    stonk.displayConnectedGraph();
                }
                load = false;
            }
            while (true) {
                String cashval = cash + "";
                if (cashval.substring(cashval.indexOf(".")).length() > 4)
                    cashval = cashval.substring(0, cashval.indexOf(".") + 3);
                System.out.println("Cash: $" + cashval);
                for (StockConstructor stonk : stonkList){
                    String stockCost = stonk.getValue() + "";
                    stockCost = stockCost.substring(0, stockCost.indexOf(".") + 3);
                    System.out.println(stonk.getName() + " $" + stockCost + " : " + queryOwnedStock(stonk.getName()));
                }
                System.out.println("What do you want to do with your stocks?");
                System.out.println("Buy \"B\", Sell \"S\", View Full Stock \"V\", or Nothing \"N\"");
                ans = scan.next();

                if (ans.equalsIgnoreCase("b")) {
                    buy = true;
                    break;
                } else if (ans.equalsIgnoreCase("s")) {

                    while (true) {
                        System.out.println("Type a stock name to Sell or \"N\" to not Sell any stocks");
                        answer = scan.next();
                        if (answer.equalsIgnoreCase("N")) {
                            System.out.println("You chose not to buy a stock.");
                            break;
                        } else {
                            for (StockConstructor stonk : stonkList) {
                                if (stonk.getName().equalsIgnoreCase(answer)) {
                                    System.out.println("You Chose " + stonk.getName() + "!");
                                    stonkName = stonk.getName();
                                    break;
                                }
                            }
                            if (stonkName.equals("")) {
                                System.out.println("Did not find that stock.");
                            } else {
                                break;
                            }
                        }
                    }
                    while (true) {
                        int sellAmount = 0;
                        if (answer.equalsIgnoreCase("N")) {
                            break;
                        } else {
                            System.out.println("How many shares do you want to Sell?");
                            ans = scan.next();
                            int num = Integer.parseInt(ans);
                            if (num > 0) {

                                for (StockConstructor stonk : stonkList) {
                                    if (stonk.getName().equalsIgnoreCase(answer)) {
                                        if (queryOwnedStock(stonk.getName()) >= num) {
                                            System.out.println("You Sold " + num + " stocks of " + stonk.getName());
                                            sellAmount = num;
                                            cash += stonk.getValue() * num;
                                            stonk.sellStock(sellAmount);

                                                for (int i = 0; i < num; i++){
                                                    ownedStocks.replace(stonk.getName(), ownedStocks.get(stonk.getName())-1);
                                                }

                                            StringBuilder last = new StringBuilder();
                                            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(save.getAbsolutePath()))) {
                                                String line = bufferedReader.readLine();
                                                while (line != null) {
                                                    last.append(line);
                                                    line = bufferedReader.readLine();
                                                }
                                            } catch (IOException e) {
                                                // Exception handling
                                            }
                                            try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(save.getAbsolutePath()))) {
                                                bufferedWriter.write(last.toString());
                                                bufferedWriter.write("&n{" + stonk.getName() + "}{" + sellAmount + "}{" + cash + "}");
                                            } catch (IOException e) {
                                                // Exception handling
                                            }

                                        } else {
                                            System.out.println("You do not have enough shares!");
                                        }
                                        break;
                                    }
                                }
                                if (sellAmount > 0) {
                                    break;
                                }
                            } else {
                                System.out.println("You are not going to sell any shares :(");
                                break;
                            }

                        }
                    }


                } else if (ans.equalsIgnoreCase("N")) {
                    break;
                } else if (ans.equalsIgnoreCase("V")){
                    System.out.println("------------------------------------------------------------------");
                    System.out.println("                                                                  ");
                    System.out.println("                                                                  ");
                    System.out.println("                                                                  ");
                    System.out.println("                                                                  ");
                    System.out.println("                                                                  ");
                    System.out.println("                                                                  ");
                    System.out.println("                                                                  ");
                    System.out.println("------------------------------------------------------------------");
                    for (StockConstructor stock : stonkList){
                        stock.displayFullConnectedGraph();
                    }
                }
            }
            for (StockConstructor stonk : stonkList){
                stonk.setChange(rand.nextInt(100) + 1);
            }
        }
    }


    public static void runAmount(int amount, List<StockConstructor> stonkList, List<AIConsumer> consumers){
        Random rand = new Random();
        ArrayList<String> update = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            for (StockConstructor testStonk : stonkList) {
                testStonk.update();
            }
            for (AIConsumer consumer : consumers) {
                consumer.update();
                for (StockConstructor testStonk : stonkList) {
                    if (testStonk.getValue() < 1){
                        testStonk.randomGood();
                    }
                    if (rand.nextInt(4) + 1 == 1) {

                        if (rand.nextInt((int) testStonk.getValue() + 1) + 1 <= 1 + consumer.stockAmount(testStonk.getName()) && rand.nextInt(100) + 1 <= consumer.getBuyPercent()) {
                            int buyAmount = rand.nextInt(10) + 1;

                            while (true) {
                                if (consumer.getCash() < buyAmount * testStonk.getValue()) {
                                    buyAmount--;
                                    if (buyAmount == 0) {
                                        break;
                                    }
                                } else {
                                    break;
                                }
                            }
                            if (testStonk.getAvailability() < buyAmount) {
                                buyAmount = testStonk.getAvailability();
                                testStonk.addProductionNeed();
                            }
                            if (buyAmount > 0 && testStonk.getAvailability() > 0) {
                                //System.out.println("bought stock: " + buyAmount);
                                consumer.setCash(consumer.getCash() - testStonk.purchase(buyAmount));
                                consumer.addStock(testStonk.getName());
                                testStonk.buyStock(buyAmount);
                            }
                        }
                        if (rand.nextInt(Math.abs((int) ((testStonk.getMax()-testStonk.getMin()) - (testStonk.getValue()-testStonk.getMin())) ) + 1) + 1 <= consumer.stockAmount(testStonk.getName()) && consumer.getCash() < (testStonk.getValue() * consumer.stockAmount(testStonk.getName())) / 3 && rand.nextInt(100) + 1 <= consumer.getSellPercent()) {
                            if (consumer.contains(testStonk.getName())) {
                                int sellAmount = rand.nextInt(consumer.stockAmount(testStonk.getName()) + 1);
                                //System.out.println("sell stock: " + sellAmount);
                                consumer.setCash(consumer.getCash() + testStonk.sell(sellAmount));
                                consumer.removeStock(testStonk.getName(), sellAmount);
                                testStonk.sellStock(sellAmount);
                            }
                        }
                    }
                }
                //Thread.sleep(1000);
                //System.out.println((float) testStonk.getValue());
                //
            }
            for (StockConstructor stock : stonkList){
                update.add("&u{" + stock.getName() + "}{" + stock.getValue() + "}{" + stock.getAvailability() + "}");
            }

        }
        StringBuilder last = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(save.getAbsolutePath()))) {
            String line = bufferedReader.readLine();
            while (line != null) {
                last.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            // Exception handling
        }
        List<Double> updates = new ArrayList<>();
        if (last.toString().contains("&i")) {
            String cut = last.substring(last.indexOf("&i"));
            String num;
            int index = last.indexOf("&i");
            while (true) {
                num = cut.substring(3, cut.indexOf("}"));
                updates.add(Double.parseDouble(num));
                last.replace(index, index + num.length() + 4, "");
                if (last.toString().contains("&i")) {
                    cut = last.substring(last.indexOf("&i"));
                    index = last.indexOf("&i");
                } else {
                    break;
                }
            }
            while (updates.size() > 1000) {
                updates.remove(1);
            }
            for (Double d : updates) {
                last.append("&i{").append(d).append("}");
            }
        }
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(save.getAbsolutePath()))) {
            bufferedWriter.write(last.toString());
            for (String str : update){
                bufferedWriter.write(str);
            }
            for (AIConsumer consumer : consumers){
                bufferedWriter.write("&i{" + consumer.getCash() + "}");
            }
        } catch (IOException e) {
            // Exception handling
        }
    }

    public static int queryOwnedStock(String stonkName){
        return ownedStocks.get(stonkName);
    }
}
