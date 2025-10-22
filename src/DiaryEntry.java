// DiaryEntry.java

import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.UUID;

public class DiaryEntry {

    //creates all variables needed for a diary entry
    private final String id;
    private final String author;
    private final String title;
    private final LocalDateTime date;
    private final String mood;
    private final String location;
    private final byte[] encryptedContent;

    //method to create a diary entry
    public DiaryEntry(User user) throws Exception {
        Scanner scanner = new Scanner(System.in);

        //gathers all information needed for a diary entry
        System.out.print("Enter diary title: ");
        this.title = scanner.nextLine();

        //gathers mood
        System.out.print("Enter mood: ");
        this.mood = scanner.nextLine();

        //gathers location
        System.out.print("Enter current location: ");
        this.location = scanner.nextLine();

        //gathers diary content
        System.out.print("Enter diary content: ");
        String content = scanner.nextLine();

        //encrypts the diary content using the user's key
        this.encryptedContent = EncryptionUtil.encrypt(content, user.getUserKey());

        //sets date, id, and author
        this.date = LocalDateTime.now();
        String User = 
        this.id = User;User.getUserId();
        this.author = user.getUserName();
    }
    public String getId() {return id;}
    public String getAuthor() {return author;}
    public String getTitle() {return title;}
    public LocalDateTime getDate() {return date;}
    public String getUMood() {return mood;}
    public String getLocation() {return location;}
    public byte[] getEncryptedContent() {return encryptedContent;}

}
