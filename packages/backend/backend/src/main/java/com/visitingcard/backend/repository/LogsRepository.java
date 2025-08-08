package com.visitingcard.backend.repository;

import com.visitingcard.backend.entity.AdminLoginLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.logging.Logger;

public interface LogsRepository extends JpaRepository<AdminLoginLogs, String> {

    final Logger logger = Logger.getLogger(LogsRepository.class.getName());

    Optional<AdminLoginLogs> findById(Long id);
    default Optional<AdminLoginLogs> findByIdWithLog(Long id) {
        logger.info("[AdminLoginLogsRepository] Find logs by id: " + id);
        return findById(id);
    }

}
