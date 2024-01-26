package com.dao;

import com.model.Timestamps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MysqlTimestampsRepository extends JpaRepository<Timestamps, Long> {

}
