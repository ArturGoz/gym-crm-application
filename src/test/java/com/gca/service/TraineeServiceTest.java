package com.gca.service;

import com.gca.dao.TraineeDAO;
import com.gca.mapper.TraineeMapper;
import com.gca.service.common.UserProfileService;
import com.gca.service.impl.TraineeServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeDAO dao;
    @Mock
    private UserProfileService userProfileService;
    @Mock
    private TraineeMapper mapper;

    @InjectMocks
    private TraineeServiceImpl service;

}