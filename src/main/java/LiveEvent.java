
class OutCome {
    private long id;
    private String label;
    private long odds;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public long getOdds() {
        return odds;
    }

    public void setOdds(long odds) {
        this.odds = odds;
    }
}

class MainBetOffer {
    private long id;
    private OutCome[] outComes;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OutCome[] getOutComes() {
        return outComes;
    }

    public void setOutComes(OutCome[] outComes) {
        this.outComes = outComes;
    }
}

class Event {
    private long id;
    private String name;
    private String[] tags;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}

class OutputResult {
    private String playerOne;
    private double playerOneOdds;
    private String playerTwo;
    private double playerTwoOdds;
    private String draw;
    private double drawOdds;

    public String getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(String playerOne) {
        this.playerOne = playerOne;
    }

    public double getPlayerOneOdds() {
        return playerOneOdds;
    }

    public void setPlayerOneOdds(double playerOneOdds) {
        this.playerOneOdds = playerOneOdds;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(String playerTwo) {
        this.playerTwo = playerTwo;
    }

    public double getPlayerTwoOdds() {
        return playerTwoOdds;
    }

    public void setPlayerTwoOdds(double playerTwoOdds) {
        this.playerTwoOdds = playerTwoOdds;
    }

    public String getDraw() {
        return draw;
    }

    public void setDraw(String draw) {
        this.draw = draw;
    }

    public double getDrawOdds() {
        return drawOdds;
    }

    public void setDrawOdds(double drawOdds) {
        this.drawOdds = drawOdds;
    }
}

public class LiveEvent {
    private Event event;
    private MainBetOffer mainBetOffer;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public MainBetOffer getMainBetOffer() {
        return mainBetOffer;
    }

    public void setMainBetOffer(MainBetOffer mainBetOffer) {
        this.mainBetOffer = mainBetOffer;
    }
}
