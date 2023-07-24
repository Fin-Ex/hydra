# Hydra Auth Server
## Create account
Account test/test
```sql
insert into users(login, password, hash, create_date, modify_date, country, email) values
 ('test', 'CY9rzUYh03PK3k6DJie09g==', 'MD5', now(), now(), 'RU', 'test@m0nster.io');		
```