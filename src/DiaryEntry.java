
import java.time.LocalDateTime;
import java.util.Scanner;
import javax.crypto.SecretKey;
import java.util.UUID;

public class DiaryEntry {

    private String id;
    private String author;
    private String title;
    private LocalDateTime date;
    private String mood;
    private byte[] encryptedContent;

    public DiaryEntry(String id, String authorId, String title, LocalDateTime date, String mood, byte[] encryptedContent) {
        this.id = id;
        this.author = authorId;
        this.title = title;
        this.date = date;
        this.mood = mood;
        this.encryptedContent = encryptedContent;
    }

    public void createDiaryEntry(User user) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter diary title: ");
        String title = scanner.nextLine();

        System.out.print("Enter mood: ");
        String mood = scanner.nextLine();

        LocalDateTime date = LocalDateTime.now();

        System.out.print("Enter diary content: ");
        String content = scanner.nextLine();

        byte[] encryptedContent = EncryptionUtil.encrypt(content, user.getUserKey());

        String id = UUID.randomUUID().toString();

        String author = user.getUserName();

        DiaryEntry entry = new DiaryEntry(id, author, title, date, mood, encryptedContent);
        // Save the entry using DiaryManager (not shown here)
    }

}
