
/*
 * main service file which serve RESTful APIs
 */
package com.codepath.urlshortener.resources;

/**
 *
 * @author yingbwan
 */
import com.codahale.metrics.annotation.Timed;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class URLShortenerResources {
    private final String template;
    private final String defaultURL;
    private final AtomicLong counter;
    DB db;
    HTreeMap<String, String> map;
    final String targets = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    public URLShortenerResources(String template, String defaultURL) {
        this.template = template;
        this.defaultURL = defaultURL;
        this.counter = new AtomicLong();
        //DBMaker.closeOnJvmShutdown();
        db = DBMaker.fileDB("mydb.db").closeOnJvmShutdown().transactionEnable().make();
        map= db.hashMap("map", Serializer.STRING, Serializer.STRING).createOrOpen();
        
    }

    @GET
    @Path("/{id}")
    @Timed
    public Response getURL(@PathParam("id") String  id) {
        if(map.containsKey(id)){
            String url = map.get(id);
            URI uri=null;
            try {
                uri = new URI(url);
            } catch (URISyntaxException ex) {
                Logger.getLogger(URLShortenerResources.class.getName()).log(Level.SEVERE, null, ex);
            }
            return Response.status(Response.Status.FOUND).location(uri).build();
        }
       
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    @Path("/")
    public Response postData(@FormParam("url") String url, @FormParam("id") String id){
        
        if(id==null || id.isEmpty()){
          //create new shortllink with random 
          Random rand = new Random();
          StringBuffer sb = new StringBuffer();
          for(int i=0;i<6;i++){
             int idx =rand.nextInt(targets.length());
             sb.append(targets.charAt(idx));
          }
          id = sb.toString();
          if(map.containsKey(id)){
              return Response.status(Response.Status.CONFLICT).build();
          }
          map.put(id,url);
          db.commit();
          Logger.getLogger(URLShortenerResources.class.getName()).log(Level.INFO, id);
        }else{
            if(map.containsKey(id)){
                return Response.status(Response.Status.CONFLICT).build();
            }else{
                map.put(id,url);
                db.commit();
            }
            Logger.getLogger(URLShortenerResources.class.getName()).log(Level.INFO, id);
        }
        String surl = defaultURL+id;
        URI uri =null;
        try {
            uri = new URI(surl);
        } catch (URISyntaxException ex) {
            Logger.getLogger(URLShortenerResources.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Response.created(uri).build();
    }
    
}
