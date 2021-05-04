package com.weather.weatherdataapi.service;

import com.weather.weatherdataapi.model.dto.requestdto.ScoreRequestDto;
import com.weather.weatherdataapi.model.entity.UserPreference;
import com.weather.weatherdataapi.repository.UserPreferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPreferenceService {

    public UserPreference getUserPreferenceForClient(UserPreference userPreference) {
        UserPreference userPreferenceForClient = new UserPreference().builder()
                .identification(userPreference.getIdentification())
                .temp(userPreference.getTemp()/10)
                .rainPer(userPreference.getRainPer()/10)
                .weather(userPreference.getWeather()/10)
                .humidity(userPreference.getHumidity()/10)
                .wind(userPreference.getWind()/10)
                .pm10(userPreference.getPm10()/10)
                .pm25(userPreference.getPm25()/10)
                .corona(userPreference.getCorona()/10)
                .uv(userPreference.getUv()/10)
                .pollenRisk(userPreference.getPollenRisk()/10)
                .asthma(userPreference.getAsthma()/10)
                .foodPoison(userPreference.getFoodPoison()/10)
                .build();
        return userPreferenceForClient;
    }

}
