package controllers;

import static org.zm.HTTPHelper.http_200;

import java.util.List;

import org.zm.model.Beam;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import controllers.security.Security;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

@Security.PasswordAuthenticated
public class Application extends Controller {

    public static Result index() {
    	String name = SecurityApp.getCurrentUser().uname;
    	return ok(index.render(name, getBeams().toString()));
    }
    
    public static Result beam() {
    	JsonNode json = request().body().asJson();
    	String content = json.findValue("content").textValue();
    	Beam b = Beam.create(content);
    	return http_200("id", b.getId());
    }
    
    public static Result deleteBeam() {
    	JsonNode json = request().body().asJson();
    	long id = json.findValue("id").asLong();
    	Beam.delete(id);
    	return http_200();
    }
    
    private static ArrayNode getBeams() {
    	List<Beam> list = Beam.getRange(0, 20);
    	ArrayNode result = Json.newObject().arrayNode();
    	for(Beam b : list) {
    		ObjectNode node = Json.newObject();
        	node.put("id", b.getId());
        	node.put("content", b.getContent());
        	result.add(node);
    	}
    	return result;
    }
    
}
