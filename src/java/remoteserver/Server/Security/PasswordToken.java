package remoteserver.Server.Security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordToken implements Token {

    private final String hashedPassword;

    public PasswordToken(String hashedPassword) {
        if (hashedPassword == null) {
            throw new IllegalArgumentException("Hashed password can't be null!");
        }
        this.hashedPassword = hashedPassword;
    }

    @Override
    public boolean validate(String plaintextPassword) {
        return BCrypt.checkpw(plaintextPassword, hashedPassword);
    }
}
