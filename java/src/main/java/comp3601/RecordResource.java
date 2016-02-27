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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
    public String getRecord(@QueryParam("name") String name) throws Exception {
        Record r = findRecord(name);
        if ( r == null ) {
            return "{\"error\":\"no record found for name='"+name+"'\"}";
        } 
        return r.toString();
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
    public String postRecord(MultivaluedMap<String, String> params) throws Exception {
        String name = params.getFirst("name");
        String value = params.getFirst("value");
        Record r = findRecord(name);
        if ( r == null ) { 
            return "{\"error\":\"record not found with name='"+name+"'\"}";
        }
        r.setValue(value);
        return updateRecord(r);
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
    public String putRecord(@QueryParam("name") String name, @QueryParam("value") String value) throws Exception {
        Record r = findRecord(name);
        if ( r != null ) { 
            return "{\"error\":\"record already exists with name='"+name+"'\"}";
        }   
        return createRecord(name, value);
    }

    /**
     * DELETE /record 
     * delete a record with the given name
     * @param name
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteRecord(@QueryParam("name") String name) throws Exception {
        Record r = findRecord(name);
        if ( r == null ) {
            return "{\"error\":\"record not found for name='"+name+"'\"}";
        } 
        return deleteRecord(r.getId(), r.getRev());
    }

    /**
     * Look up a record by name in Cloudant 
     */
    public Record findRecord(String name) throws MalformedURLException, IOException, ParseException {
        String url = db + "/records/_find";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getCredentialsProvider().setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), 
                new UsernamePasswordCredentials(dbUser, dbPass));
        HttpPost postRequest = new HttpPost(url);

        JSONObject obj = new JSONObject();
        JSONObject selector = new JSONObject();
        JSONObject id = new JSONObject();
        id.put("$gt", 0);
        selector.put("_id", id);
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
        String output = "", line = "";
        while ((line = br.readLine()) != null) {
            output += line;
        }
        httpClient.getConnectionManager().shutdown();

        JSONParser parser = new JSONParser();
        JSONObject js = (JSONObject)(parser.parse(output));
        JSONArray arr = (JSONArray)js.get("docs");
        if ( arr.isEmpty() ) {
            return null;
        }  
        JSONObject o = (JSONObject)arr.get(0);
        Record r = Record.parse(o);
        return r;
    }
    
    /**
     *  Update a record in Cloudant 
     */
    public String updateRecord(Record record) throws MalformedURLException, IOException, ParseException {
        String url = db + "/records";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getCredentialsProvider().setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), 
                new UsernamePasswordCredentials(dbUser, dbPass));
        HttpPost postRequest = new HttpPost(url);

        StringEntity input = new StringEntity(record.toString());
        input.setContentType("application/json");
        postRequest.setEntity(input);
        HttpResponse response = httpClient.execute(postRequest);
        if (response.getStatusLine().getStatusCode() != 201) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }
        BufferedReader br = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));
        String output = "", line = "";
        while ((line = br.readLine()) != null) {
            output += line;
        }
        httpClient.getConnectionManager().shutdown();

        return output;
    }
    
    /**
     *  Write a new record in Cloudant 
     */
    public String createRecord(String name, String value) throws MalformedURLException, IOException, ParseException {
        String url = db + "/records";
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getCredentialsProvider().setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), 
                new UsernamePasswordCredentials(dbUser, dbPass));
        HttpPost postRequest = new HttpPost(url);

        Record r = new Record(name, value);
        StringEntity input = new StringEntity(r.toString());
        input.setContentType("application/json");
        postRequest.setEntity(input);
        HttpResponse response = httpClient.execute(postRequest);
        if (response.getStatusLine().getStatusCode() != 201) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }
        BufferedReader br = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));
        String output = "", line = "";
        while ((line = br.readLine()) != null) {
            output += line;
        }
        httpClient.getConnectionManager().shutdown();

        return output;
    }
    
    /**
     *  delete a record by _id and _rev in Cloudant 
     */
    public String deleteRecord(String id, String rev) throws MalformedURLException, IOException, ParseException {
        String url = db + "/records/"+id+"?rev="+rev;
        DefaultHttpClient httpClient = new DefaultHttpClient();
        httpClient.getCredentialsProvider().setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), 
                new UsernamePasswordCredentials(dbUser, dbPass));
        HttpDelete delRequest = new HttpDelete(url);

        HttpResponse response = httpClient.execute(delRequest);
        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }
        BufferedReader br = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));
        String output = "", line = "";
        while ((line = br.readLine()) != null) {
            output += line;
        }
        httpClient.getConnectionManager().shutdown();

        return output;
    }
}
