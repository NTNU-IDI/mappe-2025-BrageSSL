
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
        this.userId = java.util.UUID.randomUUID().toString();
    }

    public void generateUser() throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter username: ");
        String userName = scanner.nextLine();

        System.out.print("Enter password: ");
        String userPassword = scanner.nextLine();

        SecretKey userKey = EncryptionUtil.generateSecretKey();
        String encryptedPassword = EncryptionUtil.hashPassword(userPassword, userKey);

        User newUser = new User(userName, userKey, encryptedPassword);
    }

    public SecretKey getUserKey() {
        return userKey;
    }
}
