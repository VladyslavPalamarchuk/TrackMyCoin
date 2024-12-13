package com.vladyslavpalamarchuk.trackmycoin.adaptors.persistence;

import com.vladyslavpalamarchuk.trackmycoin.domain.Monitoring;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonitoringRepository extends JpaRepository<Monitoring, Long> {

  List<Monitoring> findByUserId(Long userId);
}
