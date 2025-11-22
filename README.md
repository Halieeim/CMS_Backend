## Deployments

* Build Docker Image 
```bash
docker build -t cms-backend-app .
```
* Create Docker Network so containers can communicate with each other (Create it only once)
```bash
docker network create clinic-net
```
* Run Container 
```bash
docker run -d --name cms-backend-app --network clinic-net -p 8778:8778 cms-backend-app
```
