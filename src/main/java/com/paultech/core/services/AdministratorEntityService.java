package com.paultech.core.services;

import com.paultech.core.entities.AdministratorEntity;
import com.paultech.core.repo.AdministratorEntityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by paulzhang on 21/03/15.
 */
@Repository
public class AdministratorEntityService {

    @Autowired
    private AdministratorEntityRepo administratorEntityRepo;

    public AdministratorEntity findById(Long id) {
        return administratorEntityRepo.findOne(id);
    }

    public AdministratorEntity save(AdministratorEntity administratorEntity) {
        administratorEntityRepo.save(administratorEntity);
        return administratorEntity;
    }

    public void delete(AdministratorEntity administratorEntity) {
        administratorEntityRepo.delete(administratorEntity);
    }
}
