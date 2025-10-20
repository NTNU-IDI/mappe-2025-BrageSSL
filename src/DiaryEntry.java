
import java.time.LocalDateTime;
import java.util.Scanner;
import javax.crypto.SecretKey;
import java.util.UUID;

public class DiaryEntry {

    private String id;
    private String authorId;
    private String title;
    private LocalDateTime date;
    private String mood;
    private byte[] encryptedContent;

    private encryptionUtil encryptionUtil;
    private userAuth userAuth;

    public diaryEntry(String id, String authorId, String title, LocalDateTime date, String mood, byte[] encryptedContent) {
        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.date = date;
        this.mood = mood;
        this.encryptedContent = encryptedContent;

        this.encryptionUtil = new encryptionUtil();
        this.userAuth = new userAuth();
    }

    public void createDiaryEntry() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter diary title: ");
        String title = scanner.nextLine();

        System.out.print("Enter mood: ");
        String mood = scanner.nextLine();

        LocalDateTime date = LocalDateTime.now();

        System.out.print("Enter diary content: ");
        String content = scanner.nextLine();

        encryptedContent = EncryptionUtil.encrypt(content, EncryptionUtil.getSecretKey());

        String id = UUID.randomUUID().toString();

        authorId = userAuth.getUserId();

        diaryEntry entry = new diaryEntry(id, authorId, title, date, mood, encryptedContent);
        // Save the entry using DiaryManager (not shown here)
    }

}
