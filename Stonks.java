import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Stonks {
    public static void main(String[] args) throws InterruptedException {
        Random rand = new Random();
        Scanner scan = new Scanner(System.in);
        int cash = 200;
        StockConstructor cars = new StockConstructor("Car", 1, 100, 10, 50);
        StockConstructor phone = new StockConstructor("Phone", 2, 50, 50, 100);
        StockConstructor pencils = new StockConstructor("Pencil", 1, 500, 25, 75);
        StockConstructor clowns = new StockConstructor("Clown", 1, 100, 100, 50);
        StockConstructor plane = new StockConstructor("Plane", 0.25, 50, 100, 100);
        List<AIConsumer> consumers = new ArrayList<>();
        List<StockConstructor> stonkList = new ArrayList<>();
        stonkList.add(cars);
        stonkList.add(phone);
        stonkList.add(pencils);
        stonkList.add(clowns);
        stonkList.add(plane);
        int consumerAmount = 100;
        for (int y = 0; y < consumerAmount; y++) {
            consumers.add(new AIConsumer(rand.nextInt(10), rand.nextInt(100) + 1, rand.nextInt(100) + 1));
        }
        runAmount(100, stonkList, consumers);
        for (StockConstructor stonk : stonkList) {
            stonk.displayGraph();
        }
        boolean buy = true;
        while (true) {
            String answer = "";
            String stonkName = "";
            if (buy) {
                while (true) {

                    System.out.println("Type a stock name to buy or \"N\" to not buy any stocks");
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
            } else {
                answer = "N";
            }

            String ans = "";
            int buyAmount = 0;
            if (buy)
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
            buy = false;
            while (true) {
                System.out.println("how many days do you want to wait?");
                ans = scan.next();
                int num = Integer.parseInt(ans);
                if (num > 0) {
                    for (int i = 0; i < num; i++) {
                        runAmount(1, stonkList, consumers);
                        if (!(answer.equalsIgnoreCase("N"))) {
                            for (StockConstructor stonk : stonkList) {
                                if (answer.equalsIgnoreCase(stonk.getName())) {
                                    stonk.displayGraph();
                                    break;
                                }
                            }
                            Thread.sleep(1000);
                        }
                    }
                    for (StockConstructor stonk : stonkList) {
                        stonk.displayGraph();
                    }
                    break;
                } else {
                    System.out.println("You have to wait at least 1 day!");
                }
            }
            while (true) {
                System.out.println("What do you want to do with your stocks?");
                System.out.println("Buy \"B\", Sell \"S\", or Nothing \"N\"");
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
                                        if (stonk.getValue() * num <= cash) {
                                            System.out.println("You Sold " + num + " stocks of " + stonk.getName());
                                            sellAmount = num;
                                            cash -= stonk.getValue() * num;
                                            stonk.sellStock(sellAmount);
                                        } else {
                                            System.out.println("You do not have enough cash!");
                                        }
                                        break;
                                    }
                                }
                                if (sellAmount > 0) {
                                    break;
                                }
                            } else {
                                System.out.println("You have to sell at least 1!");
                            }

                        }
                    }
                    break;


                } else if (ans.equalsIgnoreCase("N")) {
                    break;
                }
            }
            for (StockConstructor stonk : stonkList){
                stonk.setChange(rand.nextInt(100) + 1);
            }
        }
    }


    public static void runAmount(int amount, List<StockConstructor> stonkList, List<AIConsumer> consumers){
        Random rand = new Random();
        for (int i = 0; i < amount; i++) {
            for (StockConstructor testStonk : stonkList) {
                testStonk.update();
            }
            for (AIConsumer consumer : consumers) {
                consumer.update();
                for (StockConstructor testStonk : stonkList) {
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
        }
    }
}
