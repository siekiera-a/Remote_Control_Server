package remoteserver.Server.Security;

@FunctionalInterface
public interface AuthenticationService {

    Token createToken();

}
