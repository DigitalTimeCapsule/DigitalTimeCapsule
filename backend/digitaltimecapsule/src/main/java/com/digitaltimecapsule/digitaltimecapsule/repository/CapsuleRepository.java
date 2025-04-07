@Repository
public interface CapsuleRepository extends JpaRepository<Capsule, Long> {
    List<Capsule> findByOpenedFalseAndExpiryDateBefore(LocalDateTime now);
}