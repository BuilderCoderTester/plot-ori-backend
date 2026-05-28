package com.plotori.develop.repository;
import com.plotori.develop.domain.entity.ArchiveEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArchiveEntryRepository extends JpaRepository<ArchiveEntry, Long>{
    List<ArchiveEntry> findByFeaturedTrue();
}
