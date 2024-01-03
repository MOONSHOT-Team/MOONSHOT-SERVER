# 🌜 MOONSHOT 🌛
<img width="648" alt="MOONSHOT" src="https://github.com/MOONSHOT-Team/MOONSHOT-SERVER/assets/75068759/cfa9befc-2ed8-4ed6-a8a2-758da9ee7526">

```
유난한 도전을 꿈꾸는 당신을 위한 프레임워크, Moonshot
ㄴ moonshot thinking을 바탕으로 목표를 설정하는 이들
	ㄴ objective를 설정하는 google의 기업 정신으로, 10%의 개선 보다는 10배의 혁신에 도전한다는 뜻
```

<br/>

## 🧑‍💻 MOONSHOT Server Developers

| 최영린 | 신민철 | 
| :---: | :---: | 
|<img width="250" alt="branch" src="https://github.com/MOONSHOT-Team/MOONSHOT-SERVER/assets/48898994/b337f737-0872-45cd-9ed6-52d9fab01202"> |<img width="250" alt="branch" src="https://github.com/MOONSHOT-Team/MOONSHOT-SERVER/assets/48898994/b337f737-0872-45cd-9ed6-52d9fab01202"> | 
| [0lynny](https://github.com/0lynny) | [its-sky](https://github.com/its-sky) |
|  |  | 

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
│  │              │  └─🗂️ userBadge
│  │              │  └─🗂️ heart
│  │              ├─🗂️ global
│  │              │  └─🗂️ auth
│  │              │    └─🗂️ fegin
│  │              │      └─🗂️ kakao
│  │              │      └─🗂️ google
│  │              │    └─🗂️ jwt
│  │              │    └─🗂️ redis
│  │              │    └─🗂️ security
│  │              │  └─🗂️ common
│  │              │    └─🗂️ exception
│  │              │    └─🗂️ filter
│  │              │    └─🗂️ model
│  │              │    └─🗂️ response
│  │              │    └─🗂️ util
│  │              │  └─🗂️ external
│  │              │    └─🗂️ s3
│  │              │    └─🗂️ discord
│  │              │  └─🗂️ constants
│  │              └────🗂️ config
│  └─🗂️ resources
│      ├─static
│      └─templates
└─🗂️ test
```

<br/>

## 🔗 ERD
<img width="1408" alt="MOONSHOT_ERD" src="https://github.com/MOONSHOT-Team/MOONSHOT-SERVER/assets/75068759/d7b9ae43-62c7-42d0-a363-9ca9e780d58b">

<br/>

## 📄 API 명세서

<br/>

## 🛠️ Tech
| 사용기술 | 정보 |
| --- | --- |
| Spring 버전 | 3.1.7 |
| 데이터베이스 | AWS RDS(MySQL) |
| Deploy | AWS EC2(Ubuntu) |
| Build Tool | Gradle |
| ERD | DBDiagram, DataGrip |
| Java version | Java 17 |

<br/>

## 🔨 Architecture
<img width="1408" alt="Architecture" src="https://github.com/MOONSHOT-Team/MOONSHOT-SERVER/assets/75068759/25ec0d5b-9b0c-429a-b0b8-b542f865c110">