package com.gca.dao.impl;

import com.gca.dao.TrainerDAO;
import com.gca.model.Trainer;
import org.springframework.stereotype.Repository;

@Repository
public class TrainerDAOImpl extends AbstractUserDAOImpl<Trainer> implements TrainerDAO {
}