package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by mj104603 on 1/23/2018.
 */
@Repository
public interface URLMappingRepository extends JpaRepository<URLMappingEntity, Long> {

}
