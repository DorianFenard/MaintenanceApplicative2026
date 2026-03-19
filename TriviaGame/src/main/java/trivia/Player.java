package trivia;

public class Player {
    private final String name;
    private int place = 1;
    private int coins = 0;
    private boolean inPenaltyBox = false;

    public Player(String name) { this.name = name; }

    public void move(int roll) {
        place += roll;
        if (place > 12) place -= 12;
    }

    public void addCoin() { coins++; }
    public boolean hasWon() { return coins == 6; }
    public String getName() { return name; }
    public int getPlace() { return place; }
    public int getCoins() { return coins; }
    public boolean isInPenaltyBox() { return inPenaltyBox; }
    public void setInPenaltyBox(boolean inPenaltyBox) { this.inPenaltyBox = inPenaltyBox; }
}