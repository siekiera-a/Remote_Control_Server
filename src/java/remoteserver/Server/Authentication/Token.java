package remoteserver.Server.Authentication;

public interface Token {

    boolean validate(String secret);

    void showAuthPopup();

}
