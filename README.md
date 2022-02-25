# attendance-android

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
│   ├── ui   
│   │   ├── login // 로그인 화면       
│   │   │    ├─── Login // Compose screen
│   │   │    ├─── LoginContract // Define login UiState, UiEvent, UiSideEffect
│   │   │    │     
│   │   ├─── member
│   │   │    ├─── main // 로그인 후 qr main 화면
│   │   │    ├─── signup // 이름 설정 및 팀 선택화면    
│   │   │    ├─── detail // 출결 확인 화면 및 도움말   
│   │   │    ├─── setting // 멤버 설정 화면
│   │   │    └─── AttendanceScreen, MainActivity //위 패키지의 부모 화면
│   │   │
│   │   │
│   │   ├── admin (wip)                 
│   │   │    ├─── main // 관리자용 메인 화면
│   │   │    ├─── detail // 누적 점수 확인 및  
│   │   │    └─── management // 출결 관리 화면   
│   │ 
│   └──────────────────── common  // 리소스, 폰트, 테마, 컴포넌트 정의
│                            ├── base
│                            ├── theme
domain (wip)                 ├── util		
├── ...                      └── yds  // YAPP design system components
├── src                                     
│   ├── main                                      
│   │   ├── entity                  
│   │   ├── repository                      
│   │   ├── usecase    
data (wip)
├── ...
├── src
│   ├── main
│   │   ├── api                 
│   │   ├── response                  
│   │   ├── repositoryImpl      

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




### Spec

* Clean Architecture with MVI
* Hilt
* Coroutine
* Jetpack Compose
* Coil 
* Firestore

