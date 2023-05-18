This is a Ktor application that listens to messages on a Kafka topic and uses that data to send outgoing Telegram messages as a bot.

## Running the Service

Before running the telegram-response, you must provide certain environment variables that contain configuration data. These variables are as follows:

- `TELEGRAM_BOT_SECRET`: This environment variable should contain the API token of the Telegram bot that the service will interact with.
- `KAFKA_LISTEN_TOPIC`: This environment variable should contain the name of the Kafka topic that the service will be listening to.
- `BOOTSTRAP_SERVERS_CONFIG`: This environment variable should contain a comma-separated list of Kafka bootstrap server addresses that the service will use to connect to the Kafka cluster.

To run the Kafka consumer service, ensure that these environment variables are set and then run the following command:

```bash
docker run -p 8080:8080 bthode/telegram-response:latest
