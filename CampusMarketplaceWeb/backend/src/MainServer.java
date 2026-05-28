import com.sun.net.httpserver.HttpServer;
import router.ApiRouter;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Main entry point for CampusMarketplaceWeb backend HTTP server.
 */
public class MainServer {
    /**
     * Starts HTTP server on port 8080.
     *
     * @param args cli args
     * @throws IOException when server cannot start
     */
    public static void main(String[] args) throws IOException {
        int port = 2026;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api", new ApiRouter());
        server.setExecutor(null);
        server.start();
        System.out.println("CampusMarketplaceWeb backend started at http://localhost:" + port);
    }
}
