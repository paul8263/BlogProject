package com.paultech.core.repo;

import com.paultech.core.entities.AdministratorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by paulzhang on 21/03/15.
 */
public interface AdministratorEntityRepo extends JpaRepository<AdministratorEntity,Long> {

}
