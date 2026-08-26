docker network create vetclinic-network &>/dev/null

docker run -d --rm \
  -v ./data:/var/lib/postgresql \
  --env-file postgres.env \
  --network vetclinic-network \
  -p 5432:5432 \
  --name vetclinic-db postgres:latest &>/dev/null
#^^^ запуск потсгри в фоне с флагом --rm для удаления контейнера сразу после его остановки

echo "Running Database Container.."
sleep 3

docker logs -f vetclinic-db > postgres-log.txt 2>&1 &
#^^^ логирование постгри

echo "Running App Container.."

(docker build -t jdbc-showcase-image . && clear) \
&& docker run -it --rm \
  --network vetclinic-network \
  --env-file postgres.env \
  jdbc-showcase-image:latest \
#^^^ билд и запуск основного приложения 

docker stop vetclinic-db &>/dev/null
#^^^ остановка постгри (и соответственно удаление контейнера) 
#после завершения выполнения основной программы
