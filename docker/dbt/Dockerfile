FROM python:3.11-slim

WORKDIR /usr/app

# installer git
RUN apt-get update && apt-get install -y git

# installer dbt
RUN pip install --no-cache-dir dbt-core dbt-postgres

CMD ["sleep", "infinity"]