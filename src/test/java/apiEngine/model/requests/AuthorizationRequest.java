package apiEngine.model.requests;

public class AuthorizationRequest {
    public String userName;
    public String password;

    public AuthorizationRequest(String username, String password) {
        this.userName = username;
        this.password = password;
    }
}
