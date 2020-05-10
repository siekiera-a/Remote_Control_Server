package remoteserver.Server.Security;

@FunctionalInterface
public interface Token {

    boolean validate(String secret);

}
