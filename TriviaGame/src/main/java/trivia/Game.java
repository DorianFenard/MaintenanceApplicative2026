package trivia;

import java.util.*;

public class Game implements IGame {
    private final List<Player> players = new ArrayList<>();
    private final Map<String, LinkedList<String>> questionsByCategory = new HashMap<>();
    private int currentPlayerIndex = 0;
    private boolean isGettingOutOfPenaltyBox;

    public Game() {
        String[] categories = {"Pop", "Science", "Sports", "Rock"};
        for (String category : categories) {
            LinkedList<String> questions = new LinkedList<>();
            for (int i = 0; i < 50; i++) {
                questions.add(category + " Question " + i);
            }
            questionsByCategory.put(category, questions);
        }
    }

    public boolean add(String playerName) {
        players.add(new Player(playerName));
        System.out.println(playerName + " was added");
        System.out.println("They are player number " + players.size());
        return true;
    }

    public void roll(int roll) {
        Player currentPlayer = players.get(currentPlayerIndex);
        System.out.println(currentPlayer.getName() + " is the current player");
        System.out.println("They have rolled a " + roll);

        if (currentPlayer.isInPenaltyBox()) {
            if (roll % 2 != 0) {
                isGettingOutOfPenaltyBox = true;
                System.out.println(currentPlayer.getName() + " is getting out of the penalty box");
                executeMove(currentPlayer, roll);
            } else {
                System.out.println(currentPlayer.getName() + " is not getting out of the penalty box");
                isGettingOutOfPenaltyBox = false;
            }
        } else {
            executeMove(currentPlayer, roll);
        }
    }

    private void executeMove(Player player, int roll) {
        player.move(roll);
        System.out.println(player.getName() + "'s new location is " + player.getPlace());
        System.out.println("The category is " + currentCategory(player.getPlace()));
        askQuestion(player.getPlace());
    }

    private void askQuestion(int place) {
        String category = currentCategory(place);
        System.out.println(questionsByCategory.get(category).removeFirst());
    }

    String currentCategory(int place) {
        int categoryIndex = (place - 1) % 4;
        return switch (categoryIndex) {
            case 0 -> "Pop";
            case 1 -> "Science";
            case 2 -> "Sports";
            default -> "Rock";
        };
    }

    public boolean handleCorrectAnswer() {
        Player currentPlayer = players.get(currentPlayerIndex);

        if (currentPlayer.isInPenaltyBox() && !isGettingOutOfPenaltyBox) {
            advanceTurn();
            return true;
        }

        System.out.println("Answer was correct!!!!");
        currentPlayer.addCoin();
        System.out.println(currentPlayer.getName() + " now has " + currentPlayer.getCoins() + " Gold Coins.");

        boolean gameContinues = !currentPlayer.hasWon();
        advanceTurn();
        return gameContinues;
    }

    public boolean wrongAnswer() {
        Player currentPlayer = players.get(currentPlayerIndex);
        System.out.println("Question was incorrectly answered");
        System.out.println(currentPlayer.getName() + " was sent to the penalty box");
        currentPlayer.setInPenaltyBox(true);

        advanceTurn();
        return true;
    }

    private void advanceTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Map<String, LinkedList<String>> getQuestionsByCategory() {
        return questionsByCategory;
    }

    public boolean isGettingOutOfPenaltyBox() {
        return isGettingOutOfPenaltyBox;
    }

    public void setGettingOutOfPenaltyBox(boolean gettingOutOfPenaltyBox) {
        isGettingOutOfPenaltyBox = gettingOutOfPenaltyBox;
    }

}