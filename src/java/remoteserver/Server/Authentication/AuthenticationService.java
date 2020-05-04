package remoteserver.Server.Authentication;

@FunctionalInterface
public interface AuthenticationService {

    Token createToken();

}
