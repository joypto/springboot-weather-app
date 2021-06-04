# weather_level

![](https://images.velog.io/images/hyundong_kk/post/9137bee2-5852-456d-9dbb-611498f91292/logo%20(1).png)

사이트 링크: [외출 난이도](https://theweatherlevel.com/)

프론트 Repository: [프론트 Github](https://github.com/greedysiru/weather_level)

팀노션: [외출 난이도팀 노션](https://www.notion.so/2004f97193f04be080e06e08898dfa9b)

<br>

# 프로젝트 취지

외출하려면 확인하게 많아진 오늘 정보가 너무 분산되어 있습니다. 

분산된 정보를 모으고 중요도를 설정해 사용자에게 맞는 조건으로 

오늘의 날씨에 대한 큐레이션을 받기를 윈해서 만들었습니다 

<br>

# 프로젝트  개요

## 기술 스택

- FrontEnd: TypeScript, React
- BackEnd: Spring Boot
- DB: Redis, MySQL
- AWS: EC2, S3, Rout53, ELB, CloudFront, CodeDeploy
- Github, github action

## 특징

- Github action을 통한 배포 자동화
- S3 버킷을 통한 버전 관리
- 사용자 위치 정보 보호를 위한 HTTPS 적용
- 데이터 관리를 위한 정규화
- 성능 개선을 위한 캐시 디비 운영
- 서버 로그 분석을 위한 로그저장 (spring logback)

### 정보 출처
- 기상청
- openweatherApi
- 보건복지부
- 한국환경공단

<br>

# 주요기능

- 기상 정보 큐레이션
    - 
    - 각종 기상 정보 제공

        - 산발적으로 흩어져 있던 각종 기상 정보 및 코로나 관련 정보 수합 및 제공
    - 지역별 정보 제공

        - 현재 위치 기준으로 기상 정보를 제공, 자주 보는 지역을 추가 하여 지역 전환 가능

- 기상 정보 커스터마이징
    -
    - 개인별 날씨 점수 제공
        - 직접 개발한 알고리즘을 이용해 개인별 주간 날씨 점수 제공, 외출 시 참고 가능한 지표

<br>

# 트러블 이슈

## [백엔드 트러블 이슈](https://www.notion.so/4ac6c2e5a6224137a2cbec4dd6544780)


<br>

# 업데이트

### 1차 업데이트 21.05.01

- 변수명 기능과 목적에 맞게 수정
- 각각의 날씨정보 받아오는 기능의 명칭 통일
- 각각의 날씨정보 받아오는 구조 통일

![](https://images.velog.io/images/hyundong_kk/post/93635822-178b-44d1-9320-0a07aa45bb61/Untitled.png)

setInfoAndScore 안의 convertInfoToScore, setInfoAndScore에서 각각의 정보 받아오기

- 각각의 작업(coronaService,livingHealthService,weatherService,airPollutionService)에서 발생하는 오류 try-catch로 처리
- Region 분리
- 데이터의 관리를 위한 정규화 작업

![](https://images.velog.io/images/hyundong_kk/post/4f34f0a3-7452-4304-a756-679e497be877/Untitled%20(1).png)


### 2차 업데이트 21.05.04

#### 성능 향상을 위한 캐시 디비 도입
- 요청처리 속도: 2.2s -> 50ms 까지 성능 향상


### 3차 업데이트 21.05.11

#### 수집데이터 예외처리 작업
- 날씨 정보 예외처리 완료
- 미세먼지, 초미세먼지 예외처리 완료
- 생활건강지수 예외처리 완료

#### 에러 추적을 위한 로그 삽입
- log4dbc 설정완료

#### 지역정보 오류 보고 처리완료
- 세종시 맵핑오류 수정 완료

### 4차 업데이트 21.05.21

- 로그인 없이 사용자 설정값 유지를 위한 UserToken 기능 추가
- 카카오톡 공유기능 추가
- 카카오톡 플러스 친구 등록

### 5차 업데이트 21.05.24

- DB엔드 포인트 노출로인한 DB이전 및 암호화
- GitHub Flow 전략 적용
- 코드 가독성 증가를 위한 리팩토링

### 6차 업데이트 21.06.04

- EC2 인스턴스 과부화 문제인한 서버 오류
    - EC2 재부팅 및 프로세스 관리를 통해서 


