
import java.util.Scanner;
import javax.crypto.SecretKey;

public class User {

    //creates all variables needed for a user
    private final String userId;
    private final SecretKey userKey;
    private final String userName;
    private final String userPassword;

    //method to generate a new user
    public User() throws Exception {
        Scanner scanner = new Scanner(System.in);

        //gathers username and password
        System.out.print("Enter username: ");
        this.userName = scanner.nextLine();

        //gathers password
        System.out.print("Enter password: ");
        String tempPassword = scanner.nextLine();

        //generates user key and hashes password
        this.userKey = EncryptionUtil.generateSecretKey();
        this.userPassword = EncryptionUtil.hashPassword(tempPassword, userKey);
        this.userId = java.util.UUID.randomUUID().toString();
    }

    //getters for user class
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

    //should be static later
    public void UserAuth() throws Exception {
        Scanner scanner = new Scanner(System.in);

        //gathers username and password
        System.out.print("Enter username: ");
        String inputUsername = scanner.nextLine();

        System.out.print("Enter password: ");
        String inputPassword = scanner.nextLine();

        //hashes input password and compares to stored username and password
        String hashedInputPassword = EncryptionUtil.hashPassword(inputPassword, userKey);
        if (!inputUsername.equals(getUserName()) || !hashedInputPassword.equals(getUserPassword())) {
            throw new Exception("Authentication failed: Incorrect password or username");
        }
    }
}
