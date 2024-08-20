#!/bin/bash

IS_GREEN=$(docker ps | grep green) # 현재 실행 중인 App이 green인지 확인합니다.

# Default configuration file path (currently not used)
DEFAULT_CONF="/etc/nginx/nginx.conf"

if [ -z "$IS_GREEN" ]; then # green이 없으면 blue를 실행합니다.
  echo "### BLUE => GREEN ###"

  echo "1. get green image"
  docker-compose pull green # green 이미지를 내려받습니다.

  echo "2. green container up"
  docker-compose -f docker-compose.yaml --env-file ./.env up -d green # green 컨테이너 실행

  attempts=0
  max_attempts=10

  while [ $attempts -lt $max_attempts ]; do
    echo "3. green health check... Attempt $((attempts + 1))"
    sleep 10
    REQUEST=$(curl --max-time 10 http://127.0.0.1:8082/health-check)

    if [ -n "$REQUEST" ]; then
      echo "health check success"
      break
    fi

    attempts=$((attempts + 1))
  done

  if [ $attempts -eq $max_attempts ]; then
    echo "Health check failed after $max_attempts attempts"
    exit 1
  fi

  echo "4. reload nginx"
  docker-compose stop nginx-blue
  docker-compose -f docker-compose.yaml up -d nginx-green

  # Uncomment these lines if you need to update nginx configuration
  # sudo cp /etc/nginx/nginx.green.conf /etc/nginx/nginx.conf
  # sudo nginx -s reload

  echo "5. blue container down"
  docker-compose stop blue
else
  echo "### GREEN => BLUE ###"

  echo "1. get blue image"
  docker-compose pull blue

  echo "2. blue container up"
  docker-compose -f docker-compose.yaml --env-file ./.env up -d blue

  attempts=0
  max_attempts=10

  while [ $attempts -lt $max_attempts ]; do
    echo "3. blue health check... Attempt $((attempts + 1))"
    sleep 10
    REQUEST=$(curl --max-time 10 http://127.0.0.1:8081/health-check)

    if [ -n "$REQUEST" ]; then
      echo "health check success"
      break
    fi

    attempts=$((attempts + 1))
  done

  if [ $attempts -eq $max_attempts ]; then
    echo "Health check failed after $max_attempts attempts"
    exit 1
  fi

  echo "4. reload nginx"
  docker-compose stop nginx-green
  docker-compose -f docker-compose.yaml up -d nginx-blue

  # Uncomment these lines if you need to update nginx configuration
  # sudo cp /etc/nginx/nginx.blue.conf /etc/nginx/nginx.conf
  # sudo nginx -s reload

  echo "5. green container down"
  docker-compose stop green
fi
