# reactivelabs
Trying out spring reactive support. Both REST and websocket. An underlying MongoDB instance is needed.

The idea for the websocket in this case is to push new documents added to MongoDB directly to the web client.
To do this a capped collection is created in order to make it tailable.

To try out the websocket:
1. Set up MongoDB as described below
2. Run the application
3. Open http://localhost:8080/websocket.html
4. POST a new customer to http://localhost:8080/rest/customer
5. Verify that the newly posted customer appears in the table on the websocket page.

# MongoDB details
A database named reactivelabs with a collection named "customer" is needed. Configured username and password are 'reactivelabs'. Could be run in a docker like this which makes it accessible on localhost port 27017:

`docker run -d -p 27017:27017 -p 28017:28017 -e MONGODB_USER="reactivelabs" -e MONGODB_DATABASE="reactivelabs" -e MONGODB_PASS="reactivelabs" tutum/mongodb`

Set up the user:
`db.grantRolesToUser( "reactivelabs", [ { role: "readWrite", db: "reactivelabs" } ] )`

The collection needs to be capped so it can be tailed. It also needs one initial document to work with the tail query.
Can be setup like this in the MongoDB command line:

`db.createCollection('customer', {capped:true, size: 10000})`

`db.customer.insert({"type":"init"})`
