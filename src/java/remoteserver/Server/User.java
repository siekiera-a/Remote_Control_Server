package remoteserver.Server;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import remoteserver.Reactive.Event;
import remoteserver.SystemControl.SystemInput;
import remoteserver.SystemControl.SystemService;
import remoteserver.SystemControl.exceptions.RobotException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements Runnable {

    final Socket socket;

    final SystemInput systemInput;

    final SystemService systemService;

    @Getter
    @Setter
    Event disconnectEvent;

    ObjectInputStream reader;

    boolean running = true;

    public User(Socket socket) throws RobotException {
        this(socket, null);
    }

    public User(@NonNull Socket socket, Event disconnectEvent) throws RobotException {
        this.socket = socket;
        this.disconnectEvent = disconnectEvent;
        systemInput = SystemInput.getInstance();
        systemService = new SystemService();
    }

    @Override
    public void run() {
        running = true;
        try (socket; ObjectInputStream objectReader = new ObjectInputStream(socket.getInputStream())) {
            reader = objectReader;
            while (running) {
                // TODO Handle request with orders
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (disconnectEvent != null) {
                disconnectEvent.emit();
            }
        }
    }

    public void disconnect() {
        running = false;
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getIpAddress() {
        return ((InetSocketAddress) socket.getRemoteSocketAddress())
            .getHostString();
    }
}
