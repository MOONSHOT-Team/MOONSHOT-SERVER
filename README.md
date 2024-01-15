# 🌜 MOONSHOT 🌛
<img width="648" alt="MOONSHOT" src="https://github.com/MOONSHOT-Team/MOONSHOT-SERVER/assets/75068759/cfa9befc-2ed8-4ed6-a8a2-758da9ee7526">

```
유난한 도전을 이어가는 당신을 위한 personal OKR, moonshot
```

<br/>

## 🧑‍💻 MOONSHOT Server Developers

| 최영린 |                                                                    신민철                                                                     | 
| :---: |:------------------------------------------------------------------------------------------------------------------------------------------:| 
|<img width="250" alt="branch" src="https://github.com/MOONSHOT-Team/MOONSHOT-SERVER/assets/75068759/d3e9eaf3-098b-4f92-9fdd-413f54d7dcb2"> | <img width="250" alt="branch" src="https://github.com/MOONSHOT-Team/MOONSHOT-SERVER/assets/75068759/5ea7a44c-b3ca-4c85-8f4f-f678cd90d2f7"> | 
| [0lynny](https://github.com/0lynny) |                                                   [its-sky](https://github.com/its-sky)                                                    |
| Entity 초기 세팅 <br> ERD 및 DB 설계 <br> Swagger 세팅 <br> 인증 / 인가 구현 (Redis) |            AWS 서버 구축 <br> CI/CD 구축 <br> ERD 및 DB 설계 <br> Presigned Url(S3) 이미지 서비스 <br> Mybatis, Querydsl 동적 쿼리 <br> Discord 알림            |

<br/>

### 🌳 Git Flow 전략
<img width="672" alt="branch" src="https://github.com/MOONSHOT-Team/MOONSHOT-SERVER/assets/48898994/92c073a2-4415-4911-8d80-90530b9e41cb">
<img width="672" alt="branch" src="https://github.com/MOONSHOT-Team/MOONSHOT-SERVER/assets/48898994/c349d5d3-8c5d-489c-83b3-1f302c770d1d">

<br/>

### 💬 Code Convention
[Code Convention](https://naver.github.io/hackday-conventions-java/)

<br/>

### 🌳 Commit Convention

```swift
[prefix] #이슈번호 - 이슈 내용
```
```bash
[Feat]: 새로운 기능 구현
[Fix]: 버그, 오류 해결, 코드 수정
[Add]: Feat 이외의 부수적인 코드 추가
[Del]: 쓸모없는 코드, 주석 삭제
[Refactor]: 전면 수정이 있을 때 사용합니다
[Remove]: 파일 삭제
[Chore]: 그 이외의 잡일/ 버전 코드 수정, 패키지 구조 변경, 파일 이동, 파일이름 변경
[Docs]: README나 WIKI 등의 문서 개정
[Setting]: 프로젝트 관련 세팅
```

<br/>

### 📁 Foldering
```
├─🗂️ main
│  ├─🗂️ java
│  │  └─🗂️ org
│  │      └─🗂️ moonshot
│  │          └─🗂️ server
│  │              ├─🗂️ domain
│  │              │  └─🗂️ user
│  │              │    └─🗂️ controller
│  │              │    └─🗂️ service
│  │              │    └─🗂️ repository
│  │              │    └─🗂️ model
│  │              │    └─🗂️ exception
│  │              │    └─🗂️ dto
│  │              │  └─🗂️ objective
│  │              │  └─🗂️ keyResult
│  │              │  └─🗂️ task
│  │              │  └─🗂️ log
│  │              │  └─🗂️ badge
│  │              │  └─🗂️ heart
│  │              ├─🗂️ global
│  │              │  └─🗂️ auth
│  │              │    └─🗂️ feign
│  │              │      └─🗂️ kakao
│  │              │      └─🗂️ google
│  │              │    └─🗂️ filter
│  │              │    └─🗂️ jwt
│  │              │    └─🗂️ redis
│  │              │    └─🗂️ security
│  │              │  └─🗂️ common
│  │              │    └─🗂️ exception
│  │              │    └─🗂️ filter
│  │              │    └─🗂️ model
│  │              │      └─🗂️ validator
│  │              │    └─🗂️ response
│  │              │    └─🗂️ util
│  │              │  └─🗂️ config
│  │              │  └─🗂️ constants
│  │              │  └─🗂️ external
│  │              │    └─🗂️ discord
│  │              │      └─🗂️ exception
│  │              │      └─🗂️ model
│  │              │    └─🗂️ s3
│  │              │      └─🗂️ dto
│  │              │      └─🗂️ exception
│  │              │      └─🗂️ service
│  └─🗂️ resources
│      ├─🗂️ static
│      └─🗂️ templates
│      └─🗂️ mappers
└─🗂️ test
```

<br/>

## 🔗 ERD
<img width="1408" alt="MOONSHOT_ERD" src="https://github.com/MOONSHOT-Team/MOONSHOT-SERVER/assets/75068759/935de91d-f1e6-4e5d-a2d4-ee1ab55ad80c">

<br/>

## 📄 API 명세서
[Moonshot API 명세서](https://moonshot-.notion.site/API-b02466b274e44584a66660e329cfbe4b?pvs=4)

<img width="700" alt="MOONSHOT_ERD" src="https://github.com/MOONSHOT-Team/MOONSHOT-SERVER/assets/75068759/ea0c7725-ac26-44f6-b382-6f5fdb22dded">

<br/>

## 🛠️ Tech Stack
| 사용기술        | 정보                  |
|-------------|---------------------|
| Spring      | 3.1.7               |
| Database    | AWS RDS(MySQL)      |
| Instance    | AWS EC2(Ubuntu)     |
| CI/CD	| Github Actions, Docker, Nginx |
| Build Tool  | Gradle              |
| ERD         | DBDiagram, DataGrip |
| Java        | Java 17             |
| Discord Appender | 1.0.0               |
 | Swagger(Springdoc) | 2.2.0               |
| Redis       | 3.1.7               |
| MyBatis     | 3.0.0 |


<br/>

## 🔨 Architecture
<img width="1408" alt="Architecture" src="https://github.com/MOONSHOT-Team/MOONSHOT-SERVER/assets/75068759/80c1223e-e0c0-41fd-9f33-9b3720411ef8">