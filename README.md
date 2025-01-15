## 서버는 비용 문제로 인해 2023.08.24 이후 비활성화 상태입니다.
![아키텍처](https://github.com/user-attachments/assets/fbfa6c4d-d0bd-46e3-9d5d-c92006c0fdb6)
<br></br>

## 개요
- 친환경 라이프스타일을 위한 스토어, 제품 등의 정보를 제공하는 REST API 서버입니다.
- '좋은 설계'란 무엇인지 고민하며 DB 테이블을 설계합니다.
- Spring Security 기반 JWT 인증/인가, 기본적인 CRUD를 포함한 여러 기능을 구현하여 학습합니다.
- CI/CD 파이프라인을 구축합니다.
- 코드 리뷰와 문제 해결 과정 문서화를 통해 활발하게 소통합니다.
<br>

## 기술 스택
Core
- Java, Spring Boot/Security, JPA
<br>

Database
- MySQL
<br>

Infrastructure
- AWS EC2/RDS/S3, GitHub Actions, Docker
<br>

## ERD
### [ERDCloud](https://www.erdcloud.com/d/uEFmXxf2dKe9PPtLw)
![ERD](https://github.com/user-attachments/assets/c582a1ae-15db-48ba-a19f-6b52b6aaaef9)
<br></br>

## CI/CD 파이프라인
![CI/CD 파이프라인](https://github.com/user-attachments/assets/60156817-df65-465a-ae88-d7b07be46ff7)
1. 애플리케이션 빌드
2. 도커 이미지 생성 및 Docker Hub에 저장
3. 실행 중인 컨테이너 중단 및 삭제
4. 앞서 저장한 이미지를 가져온 뒤 새로운 컨테이너 실행
<br>

## 컨벤션
### 코드 리뷰
- 각자 리뷰어 2명 지정
- 비즈니스 로직 의도, 변수 및 메서드 네이밍, 코드 구조 등을 기준으로 진행
- PR이 생성되면 코드에 대해 상세한 코멘트 작성
<br>

### 이슈
- 담당한 기능 구현, 문제 해결 과정 문서화 등을 주제로 생성
- 이슈 번호를 확인하여 브랜치, PR 생성 시 사용
<br>

### 브랜치
`규칙 : Type/#Issue.number-Title`
- master : 최종본
- env : 환경 설정
- feat : 기능 구현
- refact : 리팩토링
- fix : 오류 수정
<br>

### 커밋
`규칙 : Type: Title`
- env : 설정
- feat : 기능 구현
- refact : 리팩토링
- fix : 오류 수정
- chore : gradle 설정, package 관련 작업, 기타
- test : 테스트
<br>

### PR
`규칙 : [#Issue.number]Type: Title`
- 진행한 내용을 요약하여 작성
<br>

## 백엔드
|[미누/문민우](https://github.com/Minuooooo)|[모건/김태균](https://github.com/taegyuni)|[한/김지은](https://github.com/gol2580)|[시오/김현성](https://github.com/evgeniac10)|[준/이우성](https://github.com/dtd1614)|
|-----|-----|-----|-----|-----|
|![미누/문민우](https://avatars.githubusercontent.com/u/121410579?v=4)|![모건/김태균](https://avatars.githubusercontent.com/u/81752546?v=4)|![한/김지은](https://avatars.githubusercontent.com/u/86960201?v=4)|![시오/김현성](https://avatars.githubusercontent.com/u/122839143?v=4)|![준/이우성](https://avatars.githubusercontent.com/u/116648310?v=4)|
