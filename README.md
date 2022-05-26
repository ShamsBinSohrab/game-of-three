# Game of Three

### Run Locally

To run the project locally, the easiest way would be
using [Docker](https://docs.docker.com/get-docker/) and the `docker-compose.yml`. 

From the root directory of the project run the script `build_local_images.sh` to build docker images locally.
After the images are built, use the `docker-compose.yml` file to deploy the services locally.

### Gameplay

The game has a `POST` endpoint that is responsible for starting a game between two players.
Use the following `cURL` command to start the game:

```cURL
curl -X POST --location "http://localhost:9091/game/start" \
    -H "Content-Type: application/json" \
    -d "{
          \"number\": <any number | null>
        }"
```