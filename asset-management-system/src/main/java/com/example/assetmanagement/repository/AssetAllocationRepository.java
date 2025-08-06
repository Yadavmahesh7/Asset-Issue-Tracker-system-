package com.example.assetmanagement.repository;

import com.example.assetmanagement.model.AssetAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssetAllocationRepository extends JpaRepository<AssetAllocation, Long> {
    List<AssetAllocation> findByDueDateBeforeAndReturnStatus(LocalDateTime now, boolean returnStatus);
}
