/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Person;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author jobe
 */
@Path("person")
public class PersonResource {

    @Context
    private UriInfo context;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Map<Integer, Person> personMap = new HashMap();

    
    /**
     * Creates a new instance of PersonResource
     */
    public PersonResource() {
        if (personMap.size() == 0){
            personMap.put(1, new Person(1, "Cercei Lannister"));
            personMap.put(2, new Person(2, "Aya Stark"));
            personMap.put(3, new Person(3, "Jon Snow"));
            personMap.put(4, new Person(4, "The mountain"));
        }
    }

    /**
     * Retrieves representation of an instance of rest.PersonResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        return "{\"msg\":\"Your Person API is up and running\"}";
    }
    
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPersonJson(@PathParam("id") int id) {
        return GSON.toJson(personMap.get(id));
    }
    
    @GET
    @Path("country/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCountryJson(@PathParam("id") String id) {
        String jsonStr = null;
        try {
            URL url = new URL("http://restcountries.eu/rest/v1/alpha?codes=" + id);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json;charset=UTF-8");
            Scanner scan = new Scanner(con.getInputStream());
            if (scan.hasNext()) {
                jsonStr = scan.nextLine();
            }
            scan.close();
        } catch (Error e) {
        } catch (MalformedURLException ex) {
            Logger.getLogger(PersonResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(PersonResource.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PersonResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonStr;
    }
    
    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllPersons() {
        return GSON.toJson(personMap.values());
    }
 
    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String deletePerson(@PathParam("id") int id) {
        personMap.remove(id);
        return "{\"status\":\"person is deleted\"}";            
    }
    
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String addPerson(String person) {
        Person p = GSON.fromJson(person, Person.class);
        Set<Integer> keySet = personMap.keySet();
        p.setId(Collections.max(keySet) + 1);
        personMap.put(personMap.size() + 1, p);
        return GSON.toJson(p);
    }
    
    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public String updatePerson(@PathParam("id") int id,  String person) {
        Person p = GSON.fromJson(person, Person.class);
        personMap.remove(id);
        personMap.put(id, p);
        return GSON.toJson(p);
    }
    
}
