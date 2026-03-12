./gradlew clean bootJar
scp -i ~/.ssh/bevelander_it_hetzner ./build/libs/telemax-0.1.jar root@178.104.56.119:/home/bevelander
ssh -i ~/.ssh/bevelander_it_hetzner root@178.104.56.119 sudo systemctl restart telemax