package shortPath;

import java.io.IOException;

public interface Server {
    /**
     * This method run the server.
     * @param port
     */
    void start(int port) throws IOException;

    /**
     * stops the server
     */
    void stop();
}