package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.requestdto.ComplainReqeustDto;
import com.weather.weatherdataapi.model.entity.Complain;
import com.weather.weatherdataapi.model.entity.User;
import com.weather.weatherdataapi.repository.ComplainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ComplainService {

    private final ComplainRepository complainRepository;

    private final UserService userService;

    public void postComplain(ComplainReqeustDto complainReqeustDto, String identification) {
        User user = userService.getOrCreateUserByIdentification(identification);

        Complain complain = new Complain(complainReqeustDto, user);
        complainRepository.save(complain);
    }

}
