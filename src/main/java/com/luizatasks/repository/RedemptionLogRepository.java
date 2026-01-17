package com.luizatasks.repository;

import com.luizatasks.domain.entity.RedemptionLog;
import com.luizatasks.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RedemptionLogRepository extends JpaRepository<RedemptionLog, Long> {
    List<RedemptionLog> findByUserOrderByRedeemedAtDesc(User user);
}

