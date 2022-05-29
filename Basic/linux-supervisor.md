# 리눅스의 supervisord

## supervisor란
* 프로세스를 모니터링하고 관리하기 위한 클라이언트/서버 프로그램
* 가동시키고 싶은 서비스를 /etc/supervisord.conf 파일에 기재해 관리



## 간단한 개념 정리
* supervisor : 제품의 이름
* supervisord : supervisor 백그라운드 데몬 프로세스
* supervisorctl : supervisor로 구동되는 프로세스를 관리하기 위한 명령어


```
    (ubuntu) sudo service supervisor start

    (homebrew로 설치한 경우) brew services start supervisor

```






