package com.gca.service;

import com.gca.dao.TrainerDAO;
import com.gca.mapper.TrainerMapper;
import com.gca.service.common.UserProfileService;
import com.gca.service.impl.TrainerServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerDAO dao;
    @Mock
    private UserProfileService userProfileService;
    @Mock
    private TrainerMapper mapper;

    @InjectMocks
    private TrainerServiceImpl service;

}
