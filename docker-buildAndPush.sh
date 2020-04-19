#!/bin/sh

USER_NAME=$1
TAG=$2
IMG_NAME=covid
REGISTRY=docker.io/"$USER_NAME"/"$IMG_NAME"

./gradlew clean build --info

docker login --username="$USER_NAME"

docker rmi "$USER_NAME"/"$IMG_NAME"
docker build --no-cache -t "$USER_NAME"/"$IMG_NAME":"$TAG" -t "$USER_NAME"/"$IMG_NAME":latest .

docker tag "$USER_NAME"/"$IMG_NAME":"$TAG" "$REGISTRY"
docker tag "$USER_NAME"/"$IMG_NAME":latest "$REGISTRY"

docker push "$REGISTRY"
