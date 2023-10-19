# StudyCafe_R

### 기동하는 방법
1. mysql docker 기동 (docker-compose)
2. profile = dev , -Djasypt.encryptor.password=my_jasypt_key
3. run npmInstall in gradle task to load css



### api flow

1) sign-up </br>
    + signUp Form으로 들어가서 입력된 값의 validation을 진행 </br>
    + 입력받은 정보들을 기반으로 Account 객체를 만든후에 db에 저장을 함 (이때 email validation token 생성)
    + html기반 메일을 만들어 /check-email-token url 이 담긴 링크를 보내서 인증을 진행할 수 있도록 함
      + token 검증 (기한, 일치여부) 통과하면 account/checked-email view를 랜더링함
    + 