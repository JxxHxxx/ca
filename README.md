## 목차

- 애플리케이션 소개
- 애플리케이션 실행을 위한 구성
- 주요 API 설명 및 VIEW 


### 애플리케이션 소개

![image](https://github.com/JxxHxxx/ca/assets/87173870/79ea2de7-f0a7-4227-93cb-a9691f8306ed)

1일 1커밋 중인데 비어있는 잔디밭을 보면 왠지 모르게 기분이 썩 좋지는 않았다. 커밋을 하지 않았다면 알려주는 기능이 있으면 좋겠다고 생각해서 만들어보았다.   


      
정작 중요한 알림 기능은 아직 구현하지 않았다. 알림 플랫폼을 무엇으로 할지 고민중이다. 


### 애플리케이션 실행을 위한 구성

`application.yml` 에 관한 내용이다. 설정을 위한 `yml` 예시 전문은 하단에 작성해두었다.

*참고 사항*
1. 필자는 MySQL 환경에서 개발, 테스트를 진행했다.
2. 깃헙 엑세스 토큰을 발급 받아 하단의 `github.auth-header.token` 값을 설정할 것을 권장한다.
  - [GITHUB API LIMIT POLICY](https://github.com/JxxHxxx/ca/blob/master/src/main/resources/document/github-api.md)

application.yml 전문

```
spring:
  datasource:
    url: # (필수)
    username: # (필수)
    password: # (필수)
    driver-class-name: # (권장)

  batch:
    jdbc:
      initialize-schema: always
    job:
      enabled: false

  jpa:
    hibernate:
      ddl-auto: update
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl

batch:
  exec-time:
    cron:
      job:
        commit-check: "0 0 * * * ?" # commit-check job 실행 주기 크론 표현식, 미 설정 시 쿼츠 잡은 동작하지 않음

github:
  auth-header:
    token: Bearer_{GITHUB_ACCESS_TOKEN} # (권장) 깃헙 엑세스 토큰 값

front:
  origin:
    domain: http://localhost:63342 # CORS 예외를 적용하기 위한 설정
```


### 주요 API 설명 및 VIEW 

```
### 사용자 등록 API
POST http://localhost:8080/users
Content-Type: application/json

[
  {"githubName": "JxxHxxx"}, {"githubName": "TheDevLuffy"}
]
```
- GithubName 은 Github 닉네임을 의미한다. 존재하지 않는 닉네임을 입력하면 조금 슬플지도 모른다. 그래도 동작 자체에는 문제가 없다.
- 추후 무차별한 등록 방지를 위해 Github Oauth API 를 통해 사용자를 등록할 예정이다.


```
### 사용자 활성화 여부 & 요청일 커밋 여부 확인 JOB 실행 API
POST http://localhost:8080/batch/run
Content-Type: application/json

{
  "jobName" : "commit-check.job",
  "jobParameters" : {
    "executeSystem":"API",
    "id" : "ADMIN20240216-02",
    "sinceTime" : "2024-02-16T00:00:00Z"
  }
}
```
- `jobName` : 실행할 JOB 의 이름
- `executeSystem` : 배치 실행 System 을 명시한다. 스케줄링에 의해 호출됐는지 REST API 에 의해 호출됐는지 구분하기 위함이다.
- `id` : jon instance 를 식별하기 위한 값, 고유해야한다. 중복된 값을 입력할 시 job은 실행되지 않는다.
- `sinceTime` : 생각해보니 시스템 내부에서 결정하는게 더 적절한 것 같다. 우선은 "2024-02-16T00:00:00Z" 이런 포멧으로 날짜를 명시하면 된다. 


*UI로 기능 살펴보기*

1. WAS 실행
2. `src/main/resources/templates/html/index.html` 파일을 열면 아래와 같은 HTML 이 랜더링 될 것이다.

![image](https://github.com/JxxHxxx/ca/assets/87173870/d5597f33-faf1-4a54-86cd-de07bdeb636c)


