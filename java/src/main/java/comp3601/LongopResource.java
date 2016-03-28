package comp3601;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.Consumes;
import javax.ws.rs.container.CompletionCallback;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;


@Path("long-operation")
public class LongopResource 
{
    static final String FILE_PATH = "/home/brandon/Dropbox/School/HonoursProject/values.txt";
    static double[] values = null;
    private static int numberOfSuccessResponses = 0;
    private static int numberOfFailures = 0;
    private static Throwable lastException = null;
              
     
    @GET
    public String get(@Suspended final AsyncResponse asyncResponse) {
   		asyncResponse.register(new CompletionCallback() {
            @Override
            public void onComplete(Throwable throwable) {
                if (throwable == null) {
                    // no throwable - the processing ended successfully
                    // (response already written to the client)
                    numberOfSuccessResponses++;
                } else {
                    numberOfFailures++;
                    lastException = throwable;
                }
            }
        }); 
        
        new Thread(new Runnable() {
            @Override
            public void run() { 
                if ( values == null )
                    initValues();
                bubbleSort(values);
                asyncResponse.resume(values);
            }
        }).start();
        return "done";
    }

    public double[] bubbleSort ( double[] values ) {
        for ( int i=0; i<values.length; i++ ) {
            for ( int j=1; j<values.length-i; j++ ) { 
                if ( values[j-1] < values[j] ) { 
                    double temp = values[j-1];
                    values[j-1] = values[j];
                    values[j] = temp;
                }
            }
        }
        return values;
    }

	private void initValues() {
		try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
		    String line;
		    ArrayList<String> lines = new ArrayList<String>();
		    while ((line = br.readLine()) != null) {
		       lines.add(line);
		    }
		    this.values = new double[lines.size()];
		    for ( int i=0; i<lines.size(); i++ )
		    	values[i] = Double.parseDouble(lines.get(i));
            System.out.println(values + " - " + values.length);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
