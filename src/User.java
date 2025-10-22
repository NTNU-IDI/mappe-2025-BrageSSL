
import java.util.Arrays;
import java.util.Scanner;
import javax.crypto.SecretKey;


public class User {

    // creates all variables needed for a user
    private final String userId;
    private final SecretKey userKey;
    private final String userName;
    private final String userPassword;

    // method to generate a new user
    public User() {
        Scanner scanner = new Scanner(System.in);

        // gathers username and id
        System.out.print("Enter username: ");
        this.userName = scanner.nextLine();
        this.userId = java.util.UUID.randomUUID().toString();

        //initializes the passwords before the loop
        char[] tempPassword;
        char[] tempPassword2;

        // gathers password
        // makes sure user inputs the wanted password 
        while (true) { 
            System.out.print("Enter password: ");
            tempPassword = scanner.nextLine().toCharArray();
            System.out.print("Repeat password: ");
            tempPassword2 = scanner.nextLine().toCharArray();
            if (Arrays.equals(tempPassword, tempPassword2)) {
                Arrays.fill(tempPassword2, ' '); // clear from memory
                break;
            }
            System.out.println("Passwords do not match. Please try again.");
            Arrays.fill(tempPassword, ' '); // clear from memory
            Arrays.fill(tempPassword2, ' '); // clear from memory
        }

        // generates user key and hashes password
        try {
            this.userKey = EncryptionUtil.generateSecretKey();
            this.userPassword = EncryptionUtil.hashPassword(new String(tempPassword), userKey);
            Arrays.fill(tempPassword, ' '); // clear from memory
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to create user: algorithm", e);
        }
        
        System.out.println("Successfully created User: " + userName);
    }

    public String getUserId() {return userId;}
    public SecretKey getUserKey() {return userKey;}
    public String getUserName() {return userName;}
    public String getUserPassword() {return userPassword;}
    // should be static later
    public void UserAuth() throws Exception {
        Scanner scanner = new Scanner(System.in);

        // gathers username and password
        System.out.print("Enter username: ");
        String inputUsername = scanner.nextLine();

        System.out.print("Enter password: ");
        String inputPassword = scanner.nextLine();

        // hashes input password and compares to stored username and password
        // improve later so that it searches for username in a database
        // and gets the corresponding password hash to compare with
        String hashedInputPassword = EncryptionUtil.hashPassword(inputPassword, userKey);
        if (!inputUsername.equals(getUserName()) || !hashedInputPassword.equals(getUserPassword())) {
            throw new Exception("Authentication failed: Incorrect password or username");
        }
    
    }

}
