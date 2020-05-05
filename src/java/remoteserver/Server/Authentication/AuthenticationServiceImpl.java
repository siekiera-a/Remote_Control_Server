package remoteserver.Server.Authentication;

import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final SecureRandom random;

    public AuthenticationServiceImpl() throws NoSuchProviderException, NoSuchAlgorithmException {
        random = SecureRandom.getInstance("SHA1PRNG", "SUN");
    }

    @Override
    public Token createToken() {
        byte[] bytes = new byte[Integer.BYTES];
        random.nextBytes(bytes);
        return new SimpleTokenImpl(Integer.toUnsignedString(ByteBuffer.wrap(bytes).getInt(), 10));
    }
}
