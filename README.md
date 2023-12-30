# 🌜 MOONSHOT 🌛
<img width="648" alt="MOONSHOT" src="https://github.com/MOONSHOT-Team/MOONSHOT-SERVER/assets/48898994/7db4826b-8b8a-4bef-a100-18efb5095a31">

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
│  │              │  └─🗂️ keyresult
│  │              │  └─🗂️ task
│  │              │  └─🗂️ log
│  │              │  └─🗂️ badge
│  │              │  └─🗂️ friend
│  │              ├─🗂️ global
│  │              │  └─🗂️ auth
│  │              │    └─🗂️ filter
│  │              │  └─🗂️ common
│  │              │    └─🗂️ exception
│  │              │    └─🗂️ model
│  │              └────🗂️ config
│  └─🗂️ resources
│      ├─static
│      └─templates
└─🗂️ test
```

<br/>

## 🔗 ERD
<img width="1408" alt="MOONSHOT_ERD" src="https://github.com/MOONSHOT-Team/MOONSHOT-SERVER/assets/48898994/3d546f9b-9c98-49a2-ad5a-dba6ea4b7617">

<br/>

## 📄 API 명세서

<br/>

## 🛠️ Tech
| 사용기술 | 정보 |
| --- | --- |
| Spring 버전 | 3.1.7 |
| 데이터베이스 | AWS RDS(MySQL) |
| Deply | AWS EC2(Ubuntu) |
| Build Tool | Gradle |
| ERD | DBDiagram, DataGrip |
| Java version | Java 17 |

<br/>

## 🔨 Architecture
<img width="1408" alt="Architecture" src="https://github.com/MOONSHOT-Team/MOONSHOT-SERVER/assets/48898994/4a51b7d3-3a6e-47fa-b097-41205892bf4f">

