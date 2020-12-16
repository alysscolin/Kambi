import org.apache.commons.math3.util.Precision;
import org.json.simple.parser.ParseException;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Kambi extends TimerTask {

    final static String webLink = "https://eu-offering.kambicdn.org/offering/v2018/ubse/event/live/open.json";

    long id;

    public Kambi(long id) {
        this.id = id;
    }

    //only parse event which is/has type of "MATCH"
    public static ArrayList<OutputResult> parseOdds(ArrayList<LiveEvent> liveEvents) {
        if (liveEvents == null || liveEvents.isEmpty())
            return null;
        ArrayList<OutputResult> results = new ArrayList<>();
        for (int i = 0; i < liveEvents.size(); i++) {
            OutputResult outputResult = new OutputResult();
            Event event = liveEvents.get(i).getEvent();
            String[] tags = event.getTags();
            boolean isMatch = false;

            for (int j = 0; j < tags.length; j++) {
                if (tags[j].equalsIgnoreCase("MATCH")) {
                    isMatch = true;
                }
            }
            
            if (isMatch) {
                MainBetOffer mainBetOffer = new MainBetOffer();
                mainBetOffer = liveEvents.get(i).getMainBetOffer();
                OutCome[] outComes = mainBetOffer.getOutComes();
                int outComesLength = outComes.length;
                if (outComesLength == 3) {
                    outputResult.setPlayerOne(outComes[0].getLabel());
                    outputResult.setPlayerOneOdds(Precision.round(outComes[0].getOdds()/1000,2));
                    outputResult.setDraw(outComes[1].getLabel());
                    outputResult.setDrawOdds(Precision.round(outComes[1].getOdds()/1000, 2));
                    outputResult.setPlayerTwo(outComes[2].getLabel());
                    outputResult.setPlayerTwoOdds(Precision.round(outComes[2].getOdds()/1000, 2));
                }
                else if (outComesLength == 2) {
                    outputResult.setPlayerOne(outComes[0].getLabel());
                    outputResult.setPlayerOneOdds(Precision.round(outComes[0].getOdds()/1000, 2));
                    outputResult.setPlayerTwo(outComes[1].getLabel());
                    outputResult.setPlayerTwoOdds(Precision.round(outComes[1].getOdds()/1000, 2));
                }
            }
            results.add(outputResult);
        }
        return results;
    }

    public static void outputOdds(ArrayList<OutputResult> outputResults) {
        if (outputResults == null || outputResults.isEmpty())
            return;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("[yyyy-MM-dd HH:mm:ss]");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now) + " | ");
        for (int i = 0; i < outputResults.size(); i++) {
            if (outputResults.get(i).getDraw() != null) {
                System.out.print(outputResults.get(i).getPlayerOne() + ":  " + outputResults.get(i).getPlayerOneOdds());
                System.out.print(" | " + outputResults.get(i).getDraw() + ": " + outputResults.get(i).getDrawOdds());
                System.out.print(" | " + outputResults.get(i).getPlayerTwo() + ": " + outputResults.get(i).getPlayerTwoOdds());
                System.out.print(" | ");
                System.out.println("");
            }
            else {
                System.out.print(outputResults.get(i).getPlayerOne() + ":  " + outputResults.get(i).getPlayerOneOdds());
                System.out.print(" | " + outputResults.get(i).getPlayerTwo() + ": " + outputResults.get(i).getPlayerTwoOdds());
                System.out.print(" | ");
                System.out.println("");
            }
        }
    }

    public static ArrayList<LiveEvent> getEventWithEventId(long id, ArrayList<LiveEvent> liveEvents) {
        if (liveEvents == null || liveEvents.isEmpty())
            return null;
        ArrayList<LiveEvent> liveEvents_result = new ArrayList<>();
        for (int i = 0; i < liveEvents.size(); i++) {
            if (liveEvents.get(i).getEvent().getId() == id) {
                liveEvents_result.add(liveEvents.get(i));
            }
        }
        return liveEvents_result;
    }

    public void run() {
        AccessWeb accessWeb = null;

        try {
            accessWeb = new AccessWeb(webLink);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ArrayList<LiveEvent> liveEvents = null;

        String jsonStr = accessWeb.readOddsStrFromWeb();
        try {
            liveEvents = accessWeb.getJsonObjs(jsonStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayList<LiveEvent> eventsRelatedToId = getEventWithEventId(id, liveEvents);

        ArrayList<OutputResult> outputResults = parseOdds(eventsRelatedToId);

        if (outputResults != null) {
            outputOdds(outputResults);
        } else {
            System.out.println("No such record found for this event id.");
        }
    }

    public static void main(String[] args) {
        long id = Long.parseLong(args[0]);

        Kambi kambi = new Kambi(id);

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(kambi, 0, 10, TimeUnit.SECONDS);
    }
}
