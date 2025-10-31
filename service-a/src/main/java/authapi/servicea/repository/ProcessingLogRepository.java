package authapi.servicea.repository;

import authapi.servicea.model.ProcessingLog;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessingLogRepository extends JpaRepository<ProcessingLog, UUID> {
}
