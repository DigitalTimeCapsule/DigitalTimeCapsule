@Component
public class CapsuleFactory {
    public Capsule createTextCapsule(String name, String data, LocalDateTime expiryDate) {
        return CapsuleBuilder.builder()
                .name(name)
                .type("Text")
                .data(data)
                .expiryDate(expiryDate)
                .opened(false)
                .build();
    }
}

public class CapsuleFactory {
    public Capsule createVideoCapsule(String name, String fileName, LocalDateTime expiryDate) {
        return CapsuleBuilder.builder()
                .name(name)
                .type("Video")
                .data(data)
                .expiryDate(expiryDate)
                .opened(false)
                .build();
    }
}

