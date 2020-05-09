package com.bizzdesk.group.taxpayer.asset.management.repository;

import com.bizzdesk.group.taxpayer.asset.management.entity.AssetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetTypeRepository extends JpaRepository<AssetType, Long> {

    boolean existsByAssetType(String assetType);
}
