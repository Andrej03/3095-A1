print('START');

db = db.getSiblingDB('booking-service');

db.createUser({
    user: 'admin',
    pwd: 'password',
    roles: [{ role: 'readWrite', db: 'booking-service' }]
});
db.createCollection('user');

db = db.getSiblingDB('event-service');

db.createUser({
    user: 'admin',
    pwd: 'password',
    roles: [{ role: 'readWrite', db: 'event-service' }]
});
db.createCollection('event');

db = db.getSiblingDB('approval-service');

db.createUser({
    user: 'admin',
    pwd: 'password',
    roles: [{ role: 'readWrite', db: 'approval-service' }]
});
db.createCollection('approval');

print('END');
