package beans;


import javax.websocket.Session;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by costas on 21/1/2018.
 */

public class MessageHandler implements javax.websocket.MessageHandler.Whole<String> {


    private Session userSession = null;

    public MessageHandler(Session userSession) {
        this.userSession = userSession;
    }

    @Override
    public void onMessage(String message) {
        try {
            String username = (String) userSession.getUserProperties().get("username");

            if (username == null) {
                userSession.getUserProperties().put("username", message);

                userSession.getBasicRemote()
                        .sendText(EndPointServer.buildJsonMessageData("System", "You are connected as : " + message));
                Iterator<Session> iterator = EndPointServer.chatRoomUsers.iterator();
                while (iterator.hasNext())
                    (iterator.next()).getBasicRemote().sendText(EndPointServer.buildJsonUserData());

            } else {
                Iterator<Session> iterator = EndPointServer.chatRoomUsers.iterator();
                while (iterator.hasNext())

                    (iterator.next()).getBasicRemote().sendText(EndPointServer.buildJsonMessageData(username, message));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

