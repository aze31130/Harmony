package trade;

import java.util.ArrayList;
import java.util.List;

import items.Item;
import users.User;

public class Trade {
    public User userA;
    public int userAMoney;
    public List<Item> userAItems;
    
    public User userB;
    public int userBMoney;
    public List<Item> userBItems;

    public Trade(User userA, User userB) {
        this.userA = userA;
        this.userB = userB;

        this.userAMoney = 0;
        this.userBMoney = 0;
        this.userAItems = new ArrayList<Item>();
        this.userBItems = new ArrayList<Item>();
    }
}
