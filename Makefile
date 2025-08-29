lint/check:
	gradle spotlessCheck

lint/apply:
	gradle spotlessApply

app/build:
	gradle build

docker/up:
	docker compose up -d

docker/down:
	docker compose down