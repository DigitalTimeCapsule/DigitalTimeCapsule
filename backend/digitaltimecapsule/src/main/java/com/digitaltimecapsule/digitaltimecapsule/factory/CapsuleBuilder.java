public class CapsuleBuilder {
    private final Capsule capsule = new Capsule();

    public static CapsuleBuilder builder() {
        return new CapsuleBuilder();
    }

    public CapsuleBuilder name(String name) {
        capsule.setName(name);
        return this;
    }

    public CapsuleBuilder type(String name) {
        capsule.setName(name);
        return this;
    }

    public CapsuleBuilder data(String data) {
        capsule.setData(data);
        return this;
    }

    public CapsuleBuilder expiryDate(LocalDateTime expiryDate) {
        capsule.setExpiryDate(expiryDate);
        return this;
    }

    public CapsuleBuilder opened(boolean opened) {
        capsule.setOpened(opened);
        return this;
    }

    public Capsule build() {
        return capsule;
    }
}