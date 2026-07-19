docker run -d --rm \
  -v ./data:/var/lib/postgresql \
  --env-file postgre.env \
  -p 5432:5432 \
  --name jdbc-postgres postgres:latest &>/dev/null
#^^^ запуск потсгри в фоне с флагом --rm для удаления контейнера сразу после его остановки

docker logs -f jdbc-postgres > postgres-log.txt 2>&1 &
#^^^ логирование постгри

(docker build -t jdbc-showcase-image . && clear) && docker run -it --rm jdbc-showcase-image:latest
#^^^ билд и запуск основного приложения 

docker stop jdbc-postgres &>/dev/null
#^^^ остановка постгри (и соответственно удаление контейнера) 
#после завершения выполнения основной программы
