# StudyCafe_R

### 기동하는 방법
1. mysql docker 기동 (docker-compose)
2. profile = dev , -Djasypt.encryptor.password=my_jasypt_key
3. run npmInstall in gradle task to load css

---

### api flow

1. **sign-up </br>**
    + signUp Form으로 들어가서 입력된 값의 validation을 진행 </br>
    + 입력받은 정보들을 기반으로 Account 객체를 만든후에 db에 저장을 함 (이때 email validation token 생성)
    + html기반 메일을 만들어 /check-email-token url 이 담긴 링크를 보내서 인증을 진행할 수 있도록 함
      + token 검증 (기한, 일치여부) 통과하면 account/checked-email view를 랜더링함

2. **login</br>**
    + UsernamePasswordAuthenticationFilter를 통해 인증이 되면 SavedRequestAwareAuthenticationSuccessHandler에서 
      이미 ExceptionTranslationFilter에서 한번 실패했던 request url이 있으면 여기로 reidrect하고 
      아니면 default url인 / 로 redirect하게 된다.

3. **create study </br>**
    + /check-eamil-token url을 눌러 인증을 성공적으로 한 유저만 만들 수 있음
      + 아니면 403 error => TODO(3)
    + 


---

### TODO

1. **인증 </br>**
   + AccountService#login : signup 할시에 받은 id, password로  <br/>
     UsernamePasswordAuthentication token을 자체 인증해서 security context에 넣어줌
2. **Thymeleaf </br>**
   + AccountService#sendsSignupConfirmEmail : Context , templateEngine dependency를 해결하거나
   + 아니면 새로운 버전에 맞춰 업그레이드 하거나
3. **Exception Handling</br>**
   + https://tonylim.tistory.com/257 를 참고해서 추가하자