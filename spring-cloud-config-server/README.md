Create a folder (git-localconfig-repo) -> git init 
View Project in Package Explorer Mode -> Build Path -> Link Source -> browse to git-localconfig-repo -> Finish create file limits-service.properties under git-localconfig-repo in eclipse (limits-service is application name)

Go to git-localconfig-repo and open Git Bash -> Run following commands 
git add -A 
git commit -m "first commit"

Right click on git-localconfig-repo -> properties -> Resource -> Copy location and put in spring-cloud-config-server.properties file 

http://localhost:8888/limits-service/default

Create two more files limits-service-dev.properties and limits-service-qa.properties 
Go to git-localconfig-repo and open Git Bash -> Run following commands 
git add -A 
git commit -m "first commit"

 http://localhost:8888/limits-service/qa 
 http://localhost:8888/limits-service/dev