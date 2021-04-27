import java.util.HashMap;
import java.util.List;

public class MyTimers {
    private HashMap<String,MyTimer> myTimersList;

    public MyTimers(List<String> descriptions){
        myTimersList = new HashMap<>();
        for (String dec:descriptions) {
            myTimersList.put(dec,new MyTimer());
        }
    }


    public MyTimer ref(String dec){
        if(!myTimersList.keySet().contains(dec))
            throw new Error("timer NOT Found");
        return myTimersList.get(dec);
    }
}
