package authapi.servicea.repository;

import authapi.servicea.model.ProcessingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProcessingLogRepository extends JpaRepository<ProcessingLog, UUID> {
}
