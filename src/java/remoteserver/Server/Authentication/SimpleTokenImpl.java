package remoteserver.Server.Authentication;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.swing.JOptionPane;
import java.util.function.Function;

@AllArgsConstructor
@RequiredArgsConstructor
public class SimpleTokenImpl implements Token {

    @NonNull
    private final String secret;

    private Function<String, String> decrypter;

    private volatile boolean displayed;

    @Override
    public boolean validate(String secret) {
        if (decrypter != null) {
            secret = decrypter.apply(secret);
        }

        return this.secret.equals(secret);
    }

    /**
     * Show auth popup if not displayed yet
     */
    @Override
    public void showAuthPopup() {
        new Thread(() -> {
            if (!displayed) {
                displayed = true;
                JOptionPane.showMessageDialog(null,
                    "Code: " + secret, "Auth Popup",
                    JOptionPane.INFORMATION_MESSAGE);
                displayed = false;
            }
        }).start();
    }

}
