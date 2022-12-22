# YAPP 출석체크 앱 


---
🙌 **Contributors**
### 20th 운영진 (프로젝트 초기 생성/구현)
- 정세희, 한지희, 추지온, 김준영, 전영상
### 21st 운영진 (유지보수)
- 한지희, 김정호, 권대원, 윤태성, 전영상(운영진X)
---


![1](https://user-images.githubusercontent.com/39688690/162011524-7c47c51a-bd14-4b4f-a01d-083efe46a866.png)
![2](https://user-images.githubusercontent.com/39688690/162011557-b498f1bb-38b8-429f-8bd0-bf51ee2527af.png)
![3](https://user-images.githubusercontent.com/39688690/162011562-af5fe477-b280-466c-8fd3-ca1ae174b463.png)
![4](https://user-images.githubusercontent.com/39688690/162011565-d460eda0-752d-4ba1-a68e-b4c0c3c03343.png)
![5](https://user-images.githubusercontent.com/39688690/162011568-c8284332-4fe4-4660-8e57-e5d0843b93ee.png)
![6](https://user-images.githubusercontent.com/39688690/162011571-4a70181e-2f1a-45e8-b38c-7d6fea840fd3.png)

<br>

# Spec

* Clean Architecture with MVI (without Redux)
* Hilt
* Coroutine
* Jetpack Compose
* Coil 
* Firestore


### Package Structure

```
app
├── ...
├── src
│   ├── main           
│   │   ├── di  // Define hilt modules             
│   │   │              
presentation
├── ...
├── src
│   ├─── model // presentation entities
│   │ 
│   ├── ui   
│   │   ├── splash
│   │   ├── login // 로그인 화면       
│   │   │    ├─── Login // Compose screen
│   │   │    ├─── LoginContract // Define login UiState, UiEvent, UiSideEffect
│   │   │    │
│   │   ├─── member
│   │   │    ├─── main // 로그인 후 qr main 화면
│   │   │    ├─── signup // 이름 설정 및 팀 선택화면    
│   │   │    ├─── detail // 출결 확인 화면 및 도움말   
│   │   │    ├─── setting // 멤버 설정 화면
│   │   │    ├─── todaysession // 오늘의 세션 화면
│   │   │
│   │   ├── admin (wip)                 
│   │   │    ├─── main // 관리자용 메인 화면
│   │   │    ├─── detail // 누적 점수 확인 및  
│   │   │    └─── management // 출결 관리 화면 
│   │   │ 
│   │   └─── AttendanceScreen, MainActivity //위 패키지의 부모 화면
│   │   
│   │ 
├──────────────────────── common  // 리소스, 폰트, 테마, 컴포넌트 정의
│                            ├── base
│                            ├── theme
domain (wip)                 ├── util		
├── ...                      └── yds  // YAPP design system components
├── src                                     
│   ├── main                                      
│   │   ├── entity                  
│   │   ├── repository                      
│   │   └── usecase    
│   │  
data (wip)
├── ...
├── src
│   ├── main
│   │   ├── api                 
│   │   ├── response                  
│   │   └── repositoryImpl      

```



### Firebase Firestore

[회원 정보 설계도](https://www.figma.com/file/idqXFkMpDNg4to6bkOk0ou/YAPP-Attendance-FireStore?node-id=0%3A1)



### Convention

1. strings.xml

    `screen name_description_component name` 형식으로 네이밍 

   ex) 로그인 화면에 사용되는 텍스트 - "3초만에 끝나는 간편한 출석체크" : `login_attendance_introduce_text`

2. 패키지 내부는 `Screen - Contract - ViewModel` 형태로 구성

   ex) 로그인 패키지: Login(Compose Screen) - LoginContract(UiState, UiEvent, UiSideEffect) - LoginViewModel

3. branch naming - `feature/`

