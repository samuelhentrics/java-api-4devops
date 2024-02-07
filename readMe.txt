docker build -t dev-toolbox:latest -f dev-toolbox.dockerfile .

docker-compose up

docker-compose -f deploy.yaml up

http://localhost:8080/api-0.0.1/swagger-ui/#/

