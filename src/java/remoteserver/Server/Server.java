package remoteserver.Server;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.NonFinal;
import remoteserver.Server.Security.AuthenticationService;
import remoteserver.Server.Security.Token;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetSocketAddress;
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

    int maxAuthAttempts = 3;

    int bufferSize = 1024;

    @Builder
    private Server(@NonNull AuthenticationService auth, @Singular("ban") Set<String> blacklist,
                   int queueSize, int maxConnectedDevices, int port) {
        this.auth = auth;
        this.blacklist = new HashSet<>(blacklist);
        this.blacklist.remove(null);

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

    private boolean isOnBlackList(Socket socket) {
        return blacklist.contains(((InetSocketAddress) socket.getRemoteSocketAddress()).getHostString());
    }

    private String read(Reader reader) throws IOException {
        char[] buffer = new char[bufferSize];
        int read = reader.read(buffer);
        return new String(buffer, 0, read);
    }

    private void write(Writer writer, int c) throws IOException {
        writer.write(c);
        writer.flush();
    }

    private void authenticate(Socket socket) {
        if (isOnBlackList(socket)) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedReader reader = null;
        PrintWriter writer = null;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            Token token = auth.createToken();
            boolean authenticated = false;
            for (int i = 0; i < maxAuthAttempts && !authenticated; i++) {
                String secret = read(reader);
                if (token.validate(secret)) {
                    authenticated = true;
                } else {
                    // write 0 to the client if authentication fails
                    write(writer, 0);
                }
            }

            if (authenticated) {
                // write 1 to the client if authentication succeeds
                write(writer, 1);
                // TODO create user
            } else {
                reader.close();
                writer.close();
                socket.close();
            }
        } catch (IOException e) {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
                socket.close();
            } catch (IOException er) {
                er.printStackTrace();
            }
        }
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
