import java.util.*;
import java.util.concurrent.*;

/**
 * Created by guzy on 16/6/27.
 */
public class QuoteTask implements Callable<TravelQuote> {

    ExecutorService exec= Executors.newFixedThreadPool(3);


    @Override
    public TravelQuote call() throws Exception {
        return null;
    }

    public List<TravelQuote> getRankedTravelQuotes(TravelInfo traveInfo,Set<TravelCompany> companies,Comparator<TravelQuote>ranking,long time,TimeUnit unit) throws InterruptedException{
        List<QuoteTask> tasks=new ArrayList<QuoteTask>();
        for(TravelCompany company:companies){
            tasks.add(new QuoteTask(company,traveInfo));
        }

        List<Future<TravelQuote>> futures = exec.invokeAll(tasks,time,unit);

        List<TravelQuote> quotes=new ArrayList<TravelQuote>(tasks.size());
        Iterator<QuoteTask> itr=tasks.iterator();
        for(Future<TravelQuote> f:futures){
            QuoteTask task=itr.next();
            try {
                quotes.add(f.get());
            } catch (ExecutionException e) {
                e.printStackTrace();
                quotes.add(task.getFailureQuote(e.getCause()));
            }catch(CancellationException e){
                e.printStackTrace();
                quotes.add(task.getTimeoutQuote(e));
            }
        }
        return quotes;
    }

    private TravelQuote getFailureQuote(Throwable cause) {
        return new TravelQuote();
    }

    private TravelQuote getTimeoutQuote(Throwable cause) {
        return new TravelQuote();
    }

    TravelCompany company;
    TravelInfo travelInfo;

    public QuoteTask(TravelCompany company, TravelInfo travelInfo) {
        this.company = company;
        this.travelInfo = travelInfo;
    }

    public TravelCompany getCompany() {
        return company;
    }

    public void setCompany(TravelCompany company) {
        this.company = company;
    }

    public TravelInfo getTravelInfo() {
        return travelInfo;
    }

    public void setTravelInfo(TravelInfo travelInfo) {
        this.travelInfo = travelInfo;
    }
}
class TravelQuote{

}


class TravelInfo{

}

class TravelCompany{

}
