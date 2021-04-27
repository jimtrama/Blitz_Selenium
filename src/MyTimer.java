import java.util.Timer;
import java.util.TimerTask;

public class MyTimer {
    private Timer timer;
    private int interval;
    private boolean counting;
    private String lineStarted;
    private String lineStopped;

    public MyTimer(){
        interval=0;
        counting=false;
        lineStopped="";
        lineStarted="";
        timer=new Timer();
    }


    public void start() throws Error{
        System.out.println(counting);
        if(counting){
            throw new Error("timer already started");
        }else{
            interval=0;
            counting=true;
        }
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                interval+=500;
            }
        }, 250, 500);
    }
    public void start(String line) throws Error{
        lineStarted=line;

        if(counting){
            throw new Error("timer already started");
        }else{
            interval=0;
            counting=true;
        }
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                interval+=500;
            }
        }, 250, 500);
    }
    public void stop() throws Error{
        if(!counting){
            throw new Error("Can't stop With out starting");
        }else{
            counting=false;
        }
        timer.cancel();
    }
    public void stop(String line) throws Error{
        lineStopped=line;
        if(!counting){
            throw new Error("Can't stop With out starting");
        }else{
            counting=false;
        }
        timer.cancel();
    }
    public String getTimePassed() {

        String base =""+interval/100;
        String toreturn = "";
        for (int i = 0; i < base.split("").length; i++) {
            if (i == base.split("").length - 1) {
                toreturn += ".";
            }
            toreturn+=base.split("")[i];

        }
        if(lineStarted.length()!=0&&lineStopped.length()!=0){
            return toreturn+" from:"+lineStarted+"  until:"+lineStopped;
        }

        else{
            return toreturn;
        }


    }

}
