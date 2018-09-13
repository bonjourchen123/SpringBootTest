package com.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.core.entity.Test;

/**
 * 
 * @author Bonjour
 *
 */
@Repository
public interface TestRepository extends JpaRepository<Test, Integer>{

}
