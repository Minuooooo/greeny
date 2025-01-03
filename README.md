## 서버는 비용 문제로 인해 2023.08.24 이후 비활성화 상태입니다.
![Architecture](https://github.com/user-attachments/assets/e4eb907b-cdf0-4b04-946e-bd2294edecf6)
<br></br>

## 개요
- 친환경적인 삶을 추구할 수 있도록 스토어, 제품 등의 정보를 제공하는 REST API 서버입니다.
- '좋은 설계'란 무엇인지 고민하며 DB 테이블을 설계합니다.
- Spring Security 기반 JWT 인증/인가, 기본적인 CRUD를 포함한 여러 기능을 구현하여 학습합니다.
- CI/CD 파이프라인을 구축합니다.
- 코드 리뷰 및 기술적 내용 공유를 통해 활발하게 소통합니다.
<br>

## 기술 스택
Core
- Java, Spring Boot/Security, JPA

Database
- MySQL

Infrastructure
- Docker, AWS (EC2/RDS/S3), GitHub Actions

<br>

## ERD
### [ERDCloud](https://www.erdcloud.com/d/uEFmXxf2dKe9PPtLw)
![ERD](https://github.com/user-attachments/assets/480dcf2a-f620-4cbb-8bca-1c89c7c33217)
<br></br>

## CI/CD
1. 애플리케이션 빌드
2. 도커 이미지 빌드 및 원격 저장소에 푸쉬
3. EC2 서버에서 실행 중인 애플리케이션 컨테이너 중단 후 이미지와 함께 삭제
4. 원격 저장소에서 이미지를 가져온 뒤 컨테이너 실행
<br></br>

## 컨벤션
### 코드 리뷰
- 인당 두 명의 리뷰자 선정
- 비즈니스 로직 의도, 변수 및 메서드 네이밍, 이외 궁금한 점 등을 기반으로 진행
- 리뷰가 종료되면 PR 생성한 팀원이 직접 병합
<br>

### 이슈
- 기능 구현 및 문제 해결 과정 문서화 등을 주제로 생성
- 이슈 번호 확인
<br>

### 브랜치
`규칙: Type/#Issue.number-Title`
- master: 최종본
- env: 환경 설정
- feat: 기능 구현
- refactor: 리팩토링
- fix: 오류 수정
<br>

### 커밋
`규칙: [Type] Title`
- env: 설정
- feat: 기능 구현
- refact: 리팩토링
- fix: 오류 수정
- chore: gradle 설정, package 관련 작업, 기타
- test: 테스트 코드
<br>

### PR
`규칙: [#Issue.number] Type: Title`
- 구체적인 내용 작성
<br>

## 백엔드
|[미누/문민우](https://github.com/Minuooooo)|[모건/김태균](https://github.com/taegyuni)|[한/김지은](https://github.com/gol2580)|[시오/김현성](https://github.com/evgeniac10)|[준/이우성](https://github.com/dtd1614)|
|-----|-----|-----|-----|-----|
|![미누/문민우](https://avatars.githubusercontent.com/u/121410579?v=4)|![모건/김태균](https://avatars.githubusercontent.com/u/81752546?v=4)|![한/김지은](https://avatars.githubusercontent.com/u/86960201?v=4)|![시오/김현성](https://avatars.githubusercontent.com/u/122839143?v=4)|![준/이우성](https://avatars.githubusercontent.com/u/116648310?v=4)|
