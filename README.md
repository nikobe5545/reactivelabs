# reactivelabs
Trying out spring reactive support. Both REST and websocket. An underlying MongoDB instance is needed.

# MongoDB details
A database named reactivelabs with a collection named "customer" is needed. Username and password should be 'reactivelabs'.
It has to be a capped collection so it can be tailable. It also needs at least one document to work with the tail query.
Can be setup like this in the command line:

`db.createCollection('customer', {capped:true, size: 10000})`
and then `db.customer.insert({"type":"init"})`
