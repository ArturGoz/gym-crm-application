package com.gca.service;

import com.gca.dao.TrainingDAO;
import com.gca.mapper.TrainingMapper;
import com.gca.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingDAO dao;
    @Mock
    private TrainingMapper mapper;

    @InjectMocks
    private TrainingServiceImpl service;

}
