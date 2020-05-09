package com.bizzdesk.group.taxpayer.asset.management.repository;

import com.bizzdesk.group.taxpayer.asset.management.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    boolean existsByLocation(String location);
}
