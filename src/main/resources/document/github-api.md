## GITHUB API
커밋 알림 개발 및 테스트에 있어 참고해야 하는 사항 정리

출처 : [Rate limits for the REST API](https://docs.github.com/en/rest/using-the-rest-api/rate-limits-for-the-rest-api?apiVersion=2022-11-28)

### Rate limits for the REST API
- 인증되지 않은 사용자 : 시간 당 60 Reqeust
- 개인 엑세스 토큰 사용 : 시간 당 5000
- Github Enterprise Cloud : 시간 당 15000

### secondary rate limits
- 최대 100개의 동시 요청만 허용
- REST API 는 1분당 900개 제한

### 만약 rate limit 을 초과한다면
만약 rate limit 이 초과됐다면 
- 403, 429 응답
- x-ratelimit-remaining header will be 0.
