# Deployment

## Build docker image
docker login
mvn compile jib:build

## Deploy to Azure
az account list
terraform plan -var prefix=lager-melder-master -var location=northeurope -var docker_image=kordondev/lager-melder -var docker_image_tag=latest



### Just for me
docker run -e "SPRING_PROFILES_ACTIVE=prod" -p 8080:8080 -t springio/gs-spring-boot-docker