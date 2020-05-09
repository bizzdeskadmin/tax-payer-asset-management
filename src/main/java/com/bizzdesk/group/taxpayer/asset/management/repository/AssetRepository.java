package com.bizzdesk.group.taxpayer.asset.management.repository;

import com.bizzdesk.group.taxpayer.asset.management.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
}
