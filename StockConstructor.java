
/**
 *
 * @author Not_Jaden
 */
import java.util.ArrayList;
import java.util.Random;
public class StockConstructor {
    Random rand = new Random();
    private final String name;
    // need for more items
    private int productionNeed = 0;
    // 1 or greater
    private double multiplier;
    // how much are available
    private int availability;
    // how much there were total
    private int itemTotal;
    private int originalTotal;
    // how often the stock will change: 1-100% chance it will change every update;
    private int change;
    // how many updates have happened
    private int updates = 0;
    // value of stock
    private double value;
    private ArrayList<Double> pastReads = new ArrayList<>();
    public StockConstructor(String name, double multiplier, int availibility, int change, double startValue){
        this.name = name;
        this.multiplier = multiplier;
        this.availability = availibility;
        this.itemTotal = availibility;
        this.change = change;
        this.value = startValue;
        this.originalTotal = itemTotal;
    }
    public void buyStock(int amount){
        value += ((amount * value) / itemTotal) * multiplier;
    }
    public void sellStock(int amount){
        value -= ((amount * value) / itemTotal) * multiplier;
    }
    public void update(){
        // loss in stock, could be manufacturing mistake or disaster to a store
        if (rand.nextInt(50) + 1 == 1){
            value -= Math.round(Math.random() * multiplier * 2 * 100.0) / 100.0;
            if (value < 0) {
                value = 0;
            }
        }
        // decide if an update will happen
        pastReads.add(value);
        if (rand.nextInt(100) + 1 <= change - (100 / ((getMax() - getMin()) - (getMax()-value)))) {
            //decide if it will go up or down
            // checking the availibility of the item

            if (availability > 0) {
                if (rand.nextInt(100) + 1 <= (itemTotal / (availability)) * 100) {
                    // goes down
                    value -= Math.round(Math.random() * multiplier * 100.0) / 100.0;
                    if (value < 0) {
                        value = 0;
                    }
                } else {
                    // goes up
                    value += Math.round(Math.random() * multiplier * 100.0) / 100.0;
                }
            } else {
                addProductionNeed();
                value -= Math.round(Math.random() * multiplier * 100.0) / 100.0;
                if (value < 0) {
                    value = 0;
                }
            }
        }
        /*if (name.equals("car")) {
            System.out.println("v: " + value);
            System.out.println("a: " + availability);
        }*/
        updates++;
        if (rand.nextInt(10) + 1 <= productionNeed){
            availability += originalTotal;
            itemTotal += originalTotal / 1.5;
            productionNeed = 0;
            //System.out.println("restocked");
        }
    }

    public double getValue() {
        return value;
    }

    public void displayGraph(){
        double max = 0;
        double min = value;
        for (double d : pastReads){
            if (d > max){
                max = d;
            }
            if (min > d){
                min = d;
            }
        }
        StringBuilder maxString = new StringBuilder(max + "");
        while (maxString.length() < 5){
            maxString.append("0");
        }
        String maxS = maxString.toString();
        if (maxS.length() > 5){
            maxS = maxS.substring(0, 5);
        }
        StringBuilder minString = new StringBuilder(min + "");
        while (minString.length() < 5){
            minString.append("0");
        }
        String minS = minString.toString();
        if (minS.length() > 5){
            minS = minS.substring(0, 5);
        }
        double mid = ((max-min) / 2) + min;
        StringBuilder midString = new StringBuilder(mid + "");
        while (midString.length() < 5){
            midString.append("0");
        }
        String midS = midString.toString();
        if (midS.length() > 5){
            midS = midS.substring(0, 5);
        }
        System.out.println(name + ":");
        System.out.println(maxS + " | " + row(1,max,min));
        System.out.println("      | " + row(2,max,min));
        System.out.println("      | " + row(3,max,min));
        System.out.println("      | " + row(4,max,min));
        System.out.println("      | " + row(5,max,min));
        System.out.println(midS + " | " + row(6,max,min));
        System.out.println("      | " + row(7,max,min));
        System.out.println("      | " + row(8,max,min));
        System.out.println("      | " + row(9,max,min));
        System.out.println("      | " + row(10,max,min));
        System.out.println(minS + " | " + row(11,max,min));
        System.out.println(updateCounter(max,min));


    }
    public String updateCounter(double max, double min){
        StringBuilder builder = new StringBuilder();
        builder.append("  Days: ");
        int i = 1;
        while (i < pastReads.size()){
            builder.append((int) i);
            String si = i + "";
            i+= ((double) pastReads.size()/100) * si.length();
            //i += si.length();
            i+= ((double) pastReads.size()/100) * 3;
            builder.append("   ");
        }


        return builder.toString();
    }
    public String digit(double value, double max, double min, int row){
        double range = max - min;
        double buffer = range / 11;

        if (value >= max-(buffer * row) && value <= max-(buffer * (row - 1))){
            return "█";
        }

        return " ";
    }

    public String row(int row, double max, double min){
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < pastReads.size()){
            result.append(digit(pastReads.get(i), max, min, row));
            i += pastReads.size() / 100;
        }
        return result.toString();
    }

    public int getAvailability(){
        return availability;
    }

    public void addProductionNeed(){
        productionNeed++;
    }

    public double purchase(int amount){
        availability -= amount;
        return value * amount;
    }

    public double sell(int amount){
        availability += amount;
        productionNeed--;
        return value * amount;
    }

    public String getName(){
        return name;
    }

    public double getMin(){
        double max = 0;
        double min = value;
        for (double d : pastReads){
            if (d > max){
                max = d;
            }
            if (min > d){
                min = d;
            }
        }
        return min;
    }

    public double getMax(){
        double max = 0;
        double min = value;
        for (double d : pastReads){
            if (d > max){
                max = d;
            }
            if (min > d){
                min = d;
            }
        }
        return max;
    }

    public void setChange(int amount){
        change = amount;
    }
}
