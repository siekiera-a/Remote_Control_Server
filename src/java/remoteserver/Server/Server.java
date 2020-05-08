package remoteserver.Server;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.NonFinal;
import remoteserver.Server.Authentication.AuthenticationService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Value
@AllArgsConstructor(access = AccessLevel.NONE)
public class Server implements Runnable {

    public final static int MAX_QUEUE_SIZE = 50;
    public final static int MIN_QUEUE_SIZE = 10;

    @NonFinal
    int queueSize = 30;

    @NonFinal
    int maxConnectedDevices = 5;

    @NonNull AuthenticationService auth;

    @NonFinal
    int port = 21370;

    @NonFinal
    boolean running;

    @NonFinal
    @Getter(AccessLevel.NONE)
    ServerSocket server;

    // set of mac and ip addresses
    Set<String> blacklist;

    @Builder
    private Server(@NonNull AuthenticationService auth, @Singular("ban") Set<String> blacklist,
                   int queueSize, int maxConnectedDevices, int port) {
        this.auth = auth;
        this.blacklist = new HashSet<>(blacklist);

        if (queueSize >= MIN_QUEUE_SIZE && queueSize <= MAX_QUEUE_SIZE) {
            this.queueSize = queueSize;
        }

        if (maxConnectedDevices > 0) {
            this.maxConnectedDevices = maxConnectedDevices;
        }

        if (port > 0) {
            this.port = port;
        }
    }

    @Override
    public void run() {
        running = true;
        ExecutorService pool = Executors.newFixedThreadPool(queueSize);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            server = serverSocket;
            while (running) {
                Socket client = server.accept();
                pool.submit(() -> authenticate(client));
            }
        } catch (SocketException e) {
            System.err.println("Closing server!");
        } catch (IOException e) {
            System.err.println("Couldn't start server on port " + port);
        } finally {
            pool.shutdown();
        }
    }

    private void authenticate(Socket socket) {
        // TODO

    }

    public void stop() {
        running = false;

        if (server != null && !server.isClosed()) {
            try {
                server.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public void forbid(String address) {
        if (address != null) {
            blacklist.add(address);
        }
    }

    public void permit(String address) {
        if (address != null) {
            blacklist.remove(address);
        }
    }

    public Set<String> getBlacklist() {
        return new HashSet<>(blacklist);
    }
}
