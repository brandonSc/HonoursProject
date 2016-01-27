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

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("record")
public class RecordResource 
{
    /**
     * GET /record
     * retrieve an existing record by name
     * @param name
     */
    @GET
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Record getRecord() {
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
}
