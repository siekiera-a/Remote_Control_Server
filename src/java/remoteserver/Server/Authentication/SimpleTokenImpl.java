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

    @Override
    public boolean validate(String secret) {
        if (decrypter != null) {
            secret = decrypter.apply(secret);
        }

        return this.secret.equals(secret);
    }

    @Override
    public void showAuthPopup() {
        JOptionPane.showMessageDialog(null,
            "Code: " + secret, "Auth Popup",
            JOptionPane.INFORMATION_MESSAGE);
    }

}
