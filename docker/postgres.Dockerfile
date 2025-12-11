FROM postgres:15
COPY ../sql/scripts/ /docker-entrypoint-initdb.d/