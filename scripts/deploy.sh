#!/bin/bash

REPOSITORY=/home/ec2-user/app
PROJECT_NAME=backend
BUILD_JAR=$(ls /home/ec2-user/app/zip/backend/build/libs/*.jar | grep -v 'plain')

echo "> Build 파일 복사"
cp $BUILD_JAR $REPOSITORY/

echo ">>> 현재 실행중인 애플리케이션 pid 확인 후 일괄 종료"
sudo ps -ef | grep java | awk '{print $2}' | xargs kill -15

echo "> 새 어플리케이션 배포"
cd $REPOSITORY
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> $JAR_NAME 에 실행 권한 추가"
chmod +x $JAR_NAME

echo "> JAR Name: $JAR_NAME"
nohup java -jar $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
