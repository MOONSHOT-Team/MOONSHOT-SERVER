#!/bin/bash

nginx_config_path="/etc/nginx"
all_port=("8080" "8081")
available_port=()
server_name=server

docker_ps_output=$(docker ps | grep $server_name)
running_container_name=$(echo "$docker_ps_output" | awk '{print $NF}')
blue_port=$(echo "$running_container_name" | awk -F'-' '{print $NF}')
web_health_check_url=/actuator/health

if [ -z "$blue_port" ]; then
    echo "> 실행 중인 서버의 포트: 없음"
else
    echo "> 실행 중인 서버의 포트: $blue_port"
fi

# 실행 가능한 포트 확인 ( all_port 중 blue_port를 제외한 port )
for item in "${all_port[@]}"; do
    if [ "$item" != "$blue_port" ]; then
        available_port+=("$item")
    fi
done

# 실행 가능한 포트 없으면 끝내기
if [ ${#available_port[@]} -eq 0 ]; then
    echo "> 실행 가능한 포트가 없습니다."
    exit 1
fi

green_port=${available_port[0]}

echo "----------------------------------------------------------------------"
# docker image pull
echo "> 도커 이미지 pull 받기"
docker pull teammoonshot/${server_name}

# green_port로 서버 실행
echo "> ${green_port} 포트로 서버 실행"
echo "> docker run -d --name ${server_name}-${green_port} -p ${green_port}:8080 -e TZ=Asia/Seoul teammoonshot/${server_name}"
docker run -d --name ${server_name}-${green_port} -p ${green_port}:8080 -e TZ=Asia/Seoul teammoonshot/${server_name}
echo "----------------------------------------------------------------------"

# green_port 서버 제대로 실행 중인지 확인
sleep 10
for retry_count in {1..10}
do
    echo "> 서버 상태 체크"
    echo "> curl -s http://localhost:${green_port}${web_health_check_url}"
		# http://localhost:{그린포트}{health check 주소} -> nginx
    response=$(curl -s http://localhost:${green_port}${web_health_check_url})
    up_count=$(echo $response | grep 'UP' | wc -l)

    if [ $up_count -ge 1 ]
    then
        echo "> 서버 실행 성공"
        break
    else
        echo "> 아직 서버 실행 안됨"
        echo "> 응답 결과: ${response}"
    fi
    if [ $retry_count -eq 10 ]
		then
        echo "> 서버 실행 실패"
        docker rm -f ${server_name}-${green_port}

        exit 1
    fi
    sleep 2
done
echo "----------------------------------------------------------------------"

# nginx switching
echo "> nginx 포트 스위칭"
echo "set \$service_url http://127.0.0.1:${green_port};" | sudo tee ${nginx_config_path}/conf.d/service-url.inc
sudo nginx -s reload

sleep 1

echo "----------------------------------------------------------------------"
# nginx를 통해서 서버 접근 가능한지 확인
response=$(curl -s http://localhost${web_health_check_url})
up_count=$(echo $response | grep 'UP' | wc -l)
if [ $up_count -ge 1 ]
then
    echo "> 서버 변경 성공"
else
    echo "> 서버 변경 실패"
    echo "> 서버 응답 결과: ${response}"
    exit 1
fi

# blue_port 서버 있다면 중단
if [ -n "$blue_port" ]; then
    echo "> 기존 ${blue_port}포트 서버 중단"
    echo "> docker rm -f ${server_name}-${blue_port}"
    sudo docker rm -f ${server_name}-${blue_port}
fi