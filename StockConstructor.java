
/**
 *
 * @author Not_Jaden
 */
import java.util.ArrayList;
import java.util.Random;
public class StockConstructor {
    Random rand = new Random();
    private int rowLength;
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
    private double preValue;
    private ArrayList<Double> pastReads = new ArrayList<>();
    public StockConstructor(String name, double multiplier, int availability, int change, double startValue){
        this.name = name;
        this.multiplier = multiplier;
        this.availability = availability;
        this.itemTotal = availability;
        this.change = change;
        this.value = startValue;
        this.originalTotal = itemTotal;
        preValue = value;
    }

    public void addRead(double read, int availability){
        pastReads.add(read);
        this.availability = availability;
        value = read;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public int getChange() {
        return change;
    }


    public void randomGood(){
        value += 5;
        value += multiplier * rand.nextDouble() * 100;
    }
    public void buyStock(int amount){
        value += ((amount * value) / itemTotal) * multiplier;
        availability -= amount;
    }
    public void sellStock(int amount){
        value -= ((amount * value) / itemTotal) * multiplier;
        availability += amount;
    }
    public void update(){
        preValue = value;
        value += 5 - (Math.random() * 10);
        if (rand.nextInt(25) + 1 == 1){
            if (rand.nextInt(2) + 1 == 1){
                value += originalTotal * multiplier;
            } else {
                value -= originalTotal * rand.nextDouble() * multiplier;
                if (value < 0){
                    value = 0;
                    randomGood();
                }
            }
        }
        // loss in stock, could be manufacturing mistake or disaster to a store
        if (rand.nextInt(50) + 1 == 1){
            value -= Math.round(Math.random() * multiplier * 2 * 100.0) / 100.0;
            if (value < 0) {
                value = 0;
            }
        }
        // increase in stock, bought new company or released new product
        if (rand.nextInt(50) + 1 == 1){
            value += Math.round(Math.random() * multiplier * 2 * 100.0) / 100.0;
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
                        randomGood();
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

        if (value > preValue){
            value += (value-preValue) * Math.random() * 2 * multiplier;
        } else {
            value -= (preValue-value) * Math.random() * 2 * multiplier;
            if (value < 0) {
                value = 0;
                randomGood();
            }
        }
    }

    public double getValue() {
        return value;
    }


    public void displayConnectedGraph(){
        double max = 0;
        double min = value;
        for (int i = pastReads.size() - 100; i < pastReads.size(); i++) {
            double d = pastReads.get(i);
            if (d > max){
                max = d;
            }
            if (min > d){
                min = d;
            }
        }
        char[][] graph = new char[11][100];
        // making all rows into a 2d array
        for (int rowNum = 1; rowNum < 12; rowNum++) {
            String rowString = row(rowNum, max, min);
            for (int i = 0; i < 100; i++) {
                if (rowString.substring(i, i+1).equalsIgnoreCase("█")){
                    // point on graph
                    graph[rowNum-1][i] = '█';
                } else {
                    // blank on graph
                    graph[rowNum-1][i] = ' ';
                }
            }
        }
        for (int row = 0; row < graph[0].length - 1 ; row++){
            int firstLevel = -1;
            int secondLevel = -1;
            // find the levels
            for (int col = 0; col < graph.length; col++){
                if (graph[col][row] == '█'){
                    firstLevel = col;
                    break;
                }
            }
            for (int col = 0; col < graph.length; col++){
                if (graph[col][row + 1] == '█'){
                    secondLevel = col;
                    break;
                }
            }
            if (!(firstLevel == -1 || secondLevel == -1)) {


                // lower number means higher on the graph
                if (firstLevel < secondLevel) {
                    // need to go downwards
                    for (int level = firstLevel + 1; level < secondLevel; level++) {
                        graph[level][row] = '█';
                    }
                } else if (firstLevel > secondLevel) {
                    // need to go upwards
                    for (int level = firstLevel - 1; level > secondLevel; level--) {
                        graph[level][row] = '█';
                    }
                }
            }
        }

        String maxS = max + "";
        if (maxS.length() > 7){
            maxS = maxS.substring(0, 7);
        }
        if (maxS.contains(".")){
            maxS = maxS.substring(0,maxS.indexOf('.'));
        }
        StringBuilder maxString = new StringBuilder(maxS);
        while (maxString.length() < 7){
            maxString.append(" ");
        }
        maxS = maxString.toString();
        String minS = min + "";
        if (minS.length() > 7){
            minS = minS.substring(0, 7);
        }
        if (minS.contains(".")){
            minS = minS.substring(0,minS.indexOf('.'));
        }
        StringBuilder minString = new StringBuilder(minS);
        while (minString.length() < 7){
            minString.append(" ");
        }
        minS = minString.toString();
        double mid = ((max-min) / 2) + min;
        String midS = mid + "";
        if (midS.length() > 7){
            midS = midS.substring(0, 7);
        }
        if (midS.contains(".")){
            midS = midS.substring(0,midS.indexOf('.'));
        }
        StringBuilder midString = new StringBuilder(midS);
        while (midString.length() < 7){
            midString.append(" ");
        }
        midS = midString.toString();
        String stockCost = value + "";
        if (stockCost.substring(stockCost.indexOf(".")).length() > 4)
        stockCost = stockCost.substring(0, stockCost.indexOf(".") + 3);
        System.out.println(name + " $" + stockCost + " : ");
        System.out.print(maxS + " | "); for (int i = 0; i < graph[0].length; i++){ System.out.print(graph[0][i]); } System.out.println("");
        System.out.print("        | "); for (int i = 0; i < graph[1].length; i++){ System.out.print(graph[1][i]); } System.out.println("");
        System.out.print("        | "); for (int i = 0; i < graph[2].length; i++){ System.out.print(graph[2][i]); } System.out.println("");
        System.out.print("        | "); for (int i = 0; i < graph[3].length; i++){ System.out.print(graph[3][i]); } System.out.println("");
        System.out.print("        | "); for (int i = 0; i < graph[4].length; i++){ System.out.print(graph[4][i]); } System.out.println("");
        System.out.print(midS + " | "); for (int i = 0; i < graph[5].length; i++){ System.out.print(graph[5][i]); } System.out.println("");
        System.out.print("        | "); for (int i = 0; i < graph[6].length; i++){ System.out.print(graph[6][i]); } System.out.println("");
        System.out.print("        | "); for (int i = 0; i < graph[7].length; i++){ System.out.print(graph[7][i]); } System.out.println("");
        System.out.print("        | "); for (int i = 0; i < graph[8].length; i++){ System.out.print(graph[8][i]); } System.out.println("");
        System.out.print("        | "); for (int i = 0; i < graph[9].length; i++){ System.out.print(graph[9][i]); } System.out.println("");
        System.out.print(minS + " | "); for (int i = 0; i < graph[10].length; i++){ System.out.print(graph[10][i]); } System.out.println("");
        System.out.println(updateCounter(max,min));
    }
    public void displayFullConnectedGraph(){
        double max = 0;
        double min = value;
        for (double d : pastReads) {
            if (d > max) {
                max = d;
            }
            if (min > d) {
                min = d;
            }
        }
        System.out.println("Max: " + max + " Min: " + min);
        char[][] graph = new char[11][100];
        // making all rows into a 2d array
        for (int rowNum = 1; rowNum < 12; rowNum++) {
            String rowString = fullRow(rowNum, max, min);
            for (int i = 0; i < 100; i++) {
                if (rowString.substring(i, i+1).equalsIgnoreCase("█")){
                    // point on graph
                    graph[rowNum-1][i] = '█';
                } else {
                    // blank on graph
                    graph[rowNum-1][i] = ' ';
                }
            }
        }
        for (int row = 0; row < graph[0].length - 1 ; row++){
            int firstLevel = -1;
            int secondLevel = -1;
            // find the levels
            for (int col = 0; col < graph.length; col++){
                if (graph[col][row] == '█'){
                    firstLevel = col;
                    break;
                }
            }
            for (int col = 0; col < graph.length; col++){
                if (graph[col][row + 1] == '█'){
                    secondLevel = col;
                    break;
                }
            }
            if (!(firstLevel == -1 || secondLevel == -1)) {


                // lower number means higher on the graph
                if (firstLevel < secondLevel) {
                    // need to go downwards
                    for (int level = firstLevel + 1; level < secondLevel; level++) {
                        graph[level][row] = '█';
                    }
                } else if (firstLevel > secondLevel) {
                    // need to go upwards
                    for (int level = firstLevel - 1; level > secondLevel; level--) {
                        graph[level][row] = '█';
                    }
                }
            }
        }

        String maxS = max + "";
        if (maxS.length() > 7){
            maxS = maxS.substring(0, 7);
        }
        if (maxS.contains(".")){
            maxS = maxS.substring(0,maxS.indexOf('.'));
        }
        StringBuilder maxString = new StringBuilder(maxS);
        while (maxString.length() < 7){
            maxString.append(" ");
        }
        maxS = maxString.toString();
        String minS = min + "";
        if (minS.length() > 7){
            minS = minS.substring(0, 7);
        }
        if (minS.contains(".")){
            minS = minS.substring(0,minS.indexOf('.'));
        }
        StringBuilder minString = new StringBuilder(minS);
        while (minString.length() < 7){
            minString.append(" ");
        }
        minS = minString.toString();
        double mid = ((max-min) / 2) + min;
        String midS = mid + "";
        if (midS.length() > 7){
            midS = midS.substring(0, 7);
        }
        if (midS.contains(".")){
            midS = midS.substring(0,midS.indexOf('.'));
        }
        StringBuilder midString = new StringBuilder(midS);
        while (midString.length() < 7){
            midString.append(" ");
        }
        midS = midString.toString();
        String stockCost = value + "";
        stockCost = stockCost.substring(0, stockCost.indexOf(".") + 3);
        System.out.println(name + " $" + stockCost + " : ");
        System.out.print(maxS + " | "); for (int i = 0; i < graph[0].length; i++){ System.out.print(graph[0][i]); } System.out.println("");
        System.out.print("        | "); for (int i = 0; i < graph[1].length; i++){ System.out.print(graph[1][i]); } System.out.println("");
        System.out.print("        | "); for (int i = 0; i < graph[2].length; i++){ System.out.print(graph[2][i]); } System.out.println("");
        System.out.print("        | "); for (int i = 0; i < graph[3].length; i++){ System.out.print(graph[3][i]); } System.out.println("");
        System.out.print("        | "); for (int i = 0; i < graph[4].length; i++){ System.out.print(graph[4][i]); } System.out.println("");
        System.out.print(midS + " | "); for (int i = 0; i < graph[5].length; i++){ System.out.print(graph[5][i]); } System.out.println("");
        System.out.print("        | "); for (int i = 0; i < graph[6].length; i++){ System.out.print(graph[6][i]); } System.out.println("");
        System.out.print("        | "); for (int i = 0; i < graph[7].length; i++){ System.out.print(graph[7][i]); } System.out.println("");
        System.out.print("        | "); for (int i = 0; i < graph[8].length; i++){ System.out.print(graph[8][i]); } System.out.println("");
        System.out.print("        | "); for (int i = 0; i < graph[9].length; i++){ System.out.print(graph[9][i]); } System.out.println("");
        System.out.print(minS + " | "); for (int i = 0; i < graph[10].length; i++){ System.out.print(graph[10][i]); } System.out.println("");
        System.out.println(fullUpdateCounter(max,min));
    }
    public String updateCounter(double max, double min){
        StringBuilder builder = new StringBuilder();
        builder.append("  Days: ");
        double i = pastReads.size()-99;
        int numbersUsed = 15;
        int numLeft = 15;
        double numChange = ((double)100/numbersUsed);
        int charsLeft = rowLength;
        while (charsLeft >= 1){
            builder.append((int) i);
            String si = ((int) i) + "";
            i += numChange;

            /*i+= ((double) pastReads.size()/100) * si.length();
            //i += si.length();
            i+= ((double) pastReads.size()/100) * 3;*/
            int whiteSpace = ((int)charsLeft / numLeft) - si.length();
            for (int y = 0; y < whiteSpace; y++){
                builder.append(" ");
                charsLeft--;
                if (charsLeft == 0){
                    break;
                }
            }
            numLeft--;
            charsLeft-= si.length();
        }

        builder.append(pastReads.size());
        return builder.toString();
    }
    public String fullUpdateCounter(double max, double min){
        StringBuilder builder = new StringBuilder();
        builder.append("  Days: ");
        double i = 1;
        int numbersUsed = 15;
        int numLeft = 15;
        double numChange = ((double)pastReads.size()/numbersUsed);
        int charsLeft = 100;
        while (charsLeft >= 1){
            builder.append((int) i);
            String si = ((int) i) + "";
            i += numChange;

            /*i+= ((double) pastReads.size()/100) * si.length();
            //i += si.length();
            i+= ((double) pastReads.size()/100) * 3;*/
            int whiteSpace = ((int)charsLeft / numLeft) - si.length();
            for (int y = 0; y < whiteSpace; y++){
                builder.append(" ");
                charsLeft--;
                if (charsLeft == 0){
                    break;
                }
            }
            numLeft--;
            charsLeft-= si.length();
        }

        builder.append(pastReads.size());
        return builder.toString();
    }
    public String digit(double value, double max, double min, int row){
        double range = max - min;
        double buffer = range / 11;

        if (value >= max-(buffer * row) && value <= max-(buffer * (row - 1))){
            return "█";
        }
        if (row == 1){
            if (value >= max){
                return "█";
            }
        }
        if (row == 11){
            if (value <= min){
                return "█";
            }
        }

        return " ";
    }

    public String row(int row, double max, double min){
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < 100){
            result.append(digit(pastReads.get(i + (pastReads.size() - 100)), max, min, row));
            i += 1;
        }
        rowLength = result.toString().length();
        return result.toString();
    }
    public String fullRow(int row, double max, double min){
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < 100){
            result.append(digit(pastReads.get(i * (pastReads.size()/100)), max, min, row));
            i += 1;
        }
        rowLength = result.toString().length();
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
