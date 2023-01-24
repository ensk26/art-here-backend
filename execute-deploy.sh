#!/bin/bash
/home/ec2-user/app/nonstop/deploy.sh > /dev/null 2> /dev/null < /dev/null &
# deploy.sh를 백그라운드로 실행한뒤, 로그나 기타 내용을 남기지 않도록 처리