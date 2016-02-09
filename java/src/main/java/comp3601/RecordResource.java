package comp3601;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONObject;
import org.apache.http.HttpResponse;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("record")
public class RecordResource 
{
    // set cloudant database URL and credentials 
    String db = "https://0a6f8059-22b3-4136-9e7c-9fbcb7b4579d-bluemix.cloudant.com";
    String dbUser = "0a6f8059-22b3-4136-9e7c-9fbcb7b4579d-bluemix";
    String dbPass = "7bc75c7e0ee2f57c22b8a985979885e6ecb78c64f5f201bcf61d8ab73b72642e";

    /**
     * GET /record
     * retrieve an existing record by name
     * @param name
     */
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Record getRecord() {
        //return findRecord("test");
        return new Record("test", "value");
    }

    /**
     * POST /record
     * update a record by name to the new value
     * @param name
     * @param value 
     */
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Record postRecord() {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://");
            StringEntity input = new StringEntity("{\"qty\":100,\"name\":\"iPad 4\"}");
            input.setContentType("application/json");
            postRequest.setEntity(input);
            HttpResponse response = httpClient.execute(postRequest);
            if (response.getStatusLine().getStatusCode() != 201) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));
            String output = "";
            while ((output += br.readLine()) != null) 
                httpClient.getConnectionManager().shutdown();
            System.out.println(output);
            return new Record("null", "null");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * PUT /record
     * add a new record with unique name and new value 
     * @param name
     * @param value
     */
    @PUT
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Record putRecord() {
        return null;
    }

    /**
     * DELETE /record 
     * delete a record with the given name
     * @param name
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Record deleteRecord() {
        return null;
    }

    /**
     * Look up a record by name in Cloudant 
     */
    public Record findRecord(String name) {
        String url = db + "/records/_find";
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            httpClient.getCredentialsProvider().setCredentials(
                    new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), 
                    new UsernamePasswordCredentials(dbUser, dbPass));
            HttpPost postRequest = new HttpPost(url);
            JSONObject obj = new JSONObject();
            JSONObject selector = new JSONObject();
            selector.put("_id", "{$gt:0}");
            selector.put("name", name);
            obj.put("selector", selector);
            StringEntity input = new StringEntity(obj.toString());
            input.setContentType("application/json");
            postRequest.setEntity(input);
            HttpResponse response = httpClient.execute(postRequest);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));
            String output = "";
            while ((output += br.readLine()) != null);
            httpClient.getConnectionManager().shutdown();
            System.out.println(output);
            return new Record("name", output);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Record("error", "error");
    }
}
