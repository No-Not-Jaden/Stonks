import java.util.ArrayList;

public class AIConsumer {
    private ArrayList<String> stocks = new ArrayList<>();
    private double cash = 0;
    private final double wage;
    private final int buyPercent;
    private final int sellPercent;
    public AIConsumer(double wage, int buyPercent, int sellPercent){
        this.wage = wage;
        this.buyPercent = buyPercent;
        this.sellPercent = sellPercent;
    }

    public double getWage() {
        return wage;
    }

    public void update(){
        cash += wage;
    }
    public void addStock(String stockName){
        stocks.add(stockName);
    }
    public void removeStock(String stockName, int amount){
        for (int i = 0; i < stocks.size(); i++){
            if (stocks.get(i).equals(stockName)){
                stocks.remove(i);
                amount--;
                if (amount == 0)
                break;
            }
        }
    }
    public boolean contains(String stockName){
        return stocks.contains(stockName);
    }

    public double getCash() {
        return cash;
    }
    public void setCash(double amount){
        cash = amount;
    }
    public int stockAmount(String stockName){
        int amount = 0;
        for (String stock: stocks){
            if (stock.equals(stockName))
                amount++;
        }
        return amount;
    }

    public int getSellPercent() {
        return sellPercent;
    }

    public int getBuyPercent() {
        return buyPercent;
    }
}
