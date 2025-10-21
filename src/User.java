
import java.util.Scanner;
import javax.crypto.SecretKey;

public class User {

    private String userId;
    private SecretKey userKey;
    private String userName;
    private String userPassword;

    public User(String username, SecretKey userKey, String password) {
        this.userName = username;
        this.userKey = userKey;
        this.userPassword = password;

    }

    public void generateUser() throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        this.userName = scanner.nextLine();

        System.out.print("Enter password: ");
        String tempPassword = scanner.nextLine();

        this.userKey = EncryptionUtil.generateSecretKey();
        this.userPassword = EncryptionUtil.hashPassword(tempPassword, userKey);

        this.userId = java.util.UUID.randomUUID().toString();
    }

    public SecretKey getUserKey() {
        return userKey;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
