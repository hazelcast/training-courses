# Create user with full access to inventory database
create user 'hzuser'@'%' identified by 'hzpass';
create database if not exists inventoryDB;
grant all on inventoryDB.* to 'hzuser'@'%';
flush privileges;
