package beans;

import org.primefaces.json.JSONObject;

import javax.json.*;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by costas on 21/1/2018.
 */

public class EndPointServer extends Endpoint {


    static Set<Session> chatRoomUsers = Collections.synchronizedSet(new HashSet<Session>());


    public void onOpen(Session session, EndpointConfig endpointConfig) {

        chatRoomUsers.add(session);

        session.addMessageHandler(new MessageHandler(session));
    }


    public void onClose(Session session, CloseReason closeReason) {
        chatRoomUsers.remove(session);

        try {
            Iterator<Session> iterator = chatRoomUsers.iterator();
            while( iterator.hasNext()) (iterator.next()).getBasicRemote().sendText(EndPointServer.buildJsonUserData());
        }catch (IOException e){
            e.printStackTrace();
        }

    }


    public void onError(Session session, Throwable thr) {
        super.onError(session, thr);
    }

    public static String buildJsonUserData(){
        Iterator<String> iterator = getUserNames().iterator();
        JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
        while (iterator.hasNext()) jsonArrayBuilder.add((String)iterator.next());
        return Json.createObjectBuilder().add("users", jsonArrayBuilder).build().toString();
    }

    public static String buildJsonMessageData (String username, String message){

        JsonObject jsonObject = Json.createObjectBuilder().add("message", username+": "+message).build();
        StringWriter stringWriter = new StringWriter();
        try(JsonWriter jsonWriter = Json.createWriter(stringWriter)) {jsonWriter.write(jsonObject);}
        return stringWriter.toString();
    }

    public static Set<String> getUserNames(){
        HashSet<String> returnSet = new HashSet<String>();
        Iterator<Session> iterator = chatRoomUsers.iterator();
        while (iterator.hasNext()) returnSet.add(iterator.next().getUserProperties().get("username").toString());
        return returnSet;
    }
}
