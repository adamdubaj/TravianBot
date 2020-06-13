package travianAPI;

import java.net.URL;

public interface TravianConnection {
    int sslConnection(URL address, String login, String password);
    //void getRequest();
    //void postRequest();
}
