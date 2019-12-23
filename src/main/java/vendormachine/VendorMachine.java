package vendormachine;

import java.util.HashMap;
import java.util.Map;

public class VendorMachine {
    HashMap<Item, Integer> stockCounter = new HashMap<Item, Integer>();
    HashMap<Item, Integer> priceItem = new HashMap<Item, Integer>();
    HashMap<Item, Integer> shoppingCart = new HashMap<Item, Integer>();
    private Integer clientMoney;
    private Integer defaultMachineBalance;

    public VendorMachine() {
        this(10);
    }

    public VendorMachine(Integer clientMoney) {
        this.clientMoney = clientMoney;
        this.defaultMachineBalance = 100;

        stockCounter.put(Item.PEPSI, 10);
        stockCounter.put(Item.COCA_COLA, 10);
        stockCounter.put(Item.NESTLE, 10);
        stockCounter.put(Item.NATURAL_WATER, 10);

        priceItem.put(Item.PEPSI, 100);
        priceItem.put(Item.COCA_COLA, 125);
        priceItem.put(Item.NESTLE, 75);
        priceItem.put(Item.NATURAL_WATER, 50);
    }


    public synchronized void buyItem(Item item, int quantity) throws Exception {
        Integer itemStock = stockCounter.get(item);

        if (itemStock - quantity < 0) {
            throw new Exception("Not item enough quantity");
        }
        int balance = getItemPrice(item) * quantity;
        if (clientMoney - balance < 0) {
            throw new Exception("Client hasnt enough balance in the machine");
        }
        clientMoney = clientMoney - balance;
        stockCounter.put(item, itemStock - quantity);
        addToShoppingCart(item, quantity);
        changeMachineBalance(balance, ProcessType.INCREASE);
    }

    public synchronized void completeProcess() throws Exception {
        int sumAllItemOfCart = sumAllItemOfCart();

        if (sumAllItemOfCart > clientMoney) {
            throw new Exception("Client hasnt enough balance in the machine");
        }
        changeMachineBalance(clientMoney - sumAllItemOfCart, ProcessType.REDUCE);
    }

    private Integer sumAllItemOfCart() {
        int sum = 0;
        for (Map.Entry<Item, Integer> entry : shoppingCart.entrySet()) {
            sum += entry.getValue() * getItemPrice(entry.getKey());
        }
        return sum;
    }


    private void addToShoppingCart(Item item, int quantity) {
        shoppingCart.compute(item, (key, value) -> (value == null) ? quantity : value + quantity);
    }

    private void changeMachineBalance(int amount, ProcessType processType) {
        if (processType.equals(ProcessType.INCREASE))
            this.defaultMachineBalance = defaultMachineBalance + amount;
        else
            this.defaultMachineBalance = defaultMachineBalance - amount;
    }

    private Integer getItemPrice(Item item) {
        return priceItem.get(item);
    }

}
