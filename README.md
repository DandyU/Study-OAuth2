# Study-LearningLab

> V0.4.1
 - Grant Type이 설정 안되는 문제 수정
 - Refresh Token DB에 저장 관리하도록 함, 1회용으로 관리

> V0.4.0
 - JWT 적용
 - Authorization Server 활성화되지 않았던 Grant Type 적용(authorization_code,password,client_credentials,implicit,refresh_token)

> V0.3.0
 - ClientDetails와 TokenStore를 InMemory에서 DB로 관리하도록 함

> V0.2.0
 - Authorization/Resource Server를 분리

> V0.1.0
 - Authorization/Resource Server가 monolithic함
