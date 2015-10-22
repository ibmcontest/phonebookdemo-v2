# phonebook
An example Java Liberty Application using DB2 to store Phone numbers.

Once deployed, the list of endpoints can be found in the following documentation:
* WADL definition: ```GET /api?_wadl&_type=xml```
* Swagger JSON definition: ```GET /api/swagger.json```
* Swagger YAML definition: ```GET /api/swagger.yaml```

## Deploying on Bluemix
You'll need a [Bluemix][1] account, and then [deploy this application to Bluemix][2]

## Deploying locally
* Set up a DB2 datasource
* Modify [persistence.xml](src/META-INF/persistence.xml) and follow the comments to connect to the datasource
* Copy [db2jcc4.jar](deploy/db2jcc4.jar) on your local machine (i.e. C:/db2)
* Copy [server.xml](deploy/server.xml) into the Liberty server instance.  And modify the DB2 jar path if neccessary.
* Publish the application to the Liberty server instance.

[1]: http://bluemix.net
[2]: https://hub.jazz.net/deploy/index.html?repository=https://github.com/ibmcontest/phonebookdemo
