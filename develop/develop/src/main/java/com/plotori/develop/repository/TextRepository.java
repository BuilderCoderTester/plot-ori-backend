package com.plotori.develop.repository;
import com.plotori.develop.domain.entity.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextRepository extends JpaRepository<Text, Long>{
    List<Text> findByCanonicalStatus(String canonicalStatus);
}
