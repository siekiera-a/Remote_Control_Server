package remoteserver.Server;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.NonFinal;
import remoteserver.Server.Authentication.AuthenticationService;

import java.util.HashSet;
import java.util.Set;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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
        // TODO
    }

    public void stop() {
        // TODO
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

}
