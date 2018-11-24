/**
Pre-Work: Please perform these steps before solving the problem
1. Login to MySQL using below commands on a fresh terminal window
    mysql -u retail_dba -p
    Password = cloudera
2. Create a replica product table and name it products_replica
    create table products_replica as select * from products
3. Add primary key to the newly created table
    alter table products_replica add primary key (product_id);
4. Add two more columns
    alter table products_replica add column (product_grade int, product_sentiment varchar(100))
5. Run below two update statements to modify the data
    update products_replica set product_grade = 1  where product_price > 500;
    update products_replica set product_sentiment  = 'WEAK'  where product_price between 300 and  500;

Problem 5: Above steps are important so please complete them successfully before attempting to solve the problem
1. Using sqoop, import products_replica table from MYSQL into hdfs such that fields are separated by a '|' and lines are separated by '\n'. 
   Null values are represented as -1 for numbers and "NOT-AVAILABLE" for strings. 
   Only records with product id greater than or equal to 1 and less than or equal to 1000 should be imported and use 3 mappers for importing. 
   The destination file should be stored as a text file to directory  /user/cloudera/problem5/products-text. 
2. Using sqoop, import products_replica table from MYSQL into hdfs such that fields are separated by a '*' and lines are separated by '\n'. 
   Null values are represented as -1000 for numbers and "NA" for strings. Only records with product id less than or equal to 1111 should be imported and use 2 mappers for importing. 
   The destination file should be stored as a text file to directory  /user/cloudera/problem5/products-text-part1. 
3. Using sqoop, import products_replica table from MYSQL into hdfs such that fields are separated by a '*' and lines are separated by '\n'. 
   Null values are represented as -1000 for numbers and "NA" for strings. Only records with product id greater than 1111 should be imported and use 5 mappers for importing. 
   The destination file should be stored as a text file to directory  /user/cloudera/problem5/products-text-part2.
4. Using sqoop merge data available in /user/cloudera/problem5/products-text-part1 and /user/cloudera/problem5/products-text-part2 to produce a new set of files in /user/cloudera/problem5/products-text-both-parts
5. Using sqoop do the following. Read the entire steps before you create the sqoop job.
	-create a sqoop job Import Products_replica table as text file to directory /user/cloudera/problem5/products-incremental. Import all the records.
	-insert three more records to Products_replica from mysql
	-run the sqoop job again so that only newly added records can be pulled from mysql
	-insert 2 more records to Products_replica from mysql
	-run the sqoop job again so that only newly added records can be pulled from mysql
	-Validate to make sure the records have not be duplicated in HDFS
6. Using sqoop do the following. Read the entire steps before you create the sqoop job.
	-create a hive table in database named problem5 using below command 
	-create table products_hive  (product_id int, product_category_id int, product_name string, product_description string, product_price float, product_imaage string,product_grade int,  product_sentiment string);
	-create a sqoop job Import Products_replica table as hive table to database named problem5. name the table as products_hive. 
	-insert three more records to Products_replica from mysql
	-run the sqoop job again so that only newly added records can be pulled from mysql
	-insert 2 more records to Products_replica from mysql
	-run the sqoop job again so that only newly added records can be pulled from mysql
	-Validate to make sure the records have not been duplicated in Hive table
7. Using sqoop do the following. .
	-insert 2 more records into products_hive table using hive. 
	-create table in mysql using below command   
	-create table products_external  (product_id int(11) primary Key, product_grade int(11), product_category_id int(11), product_name varchar(100), product_description varchar(100), 
		product_price float, product_impage varchar(500), product_sentiment varchar(100));
	-export data from products_hive (hive) table to (mysql) products_external table. 
	-insert 2 more records to Products_hive table from hive
	-export data from products_hive table to products_external table. 
	-Validate to make sure the records have not be duplicated in mysql table

*/
//1. Using sqoop, import products_replica table from MYSQL into hdfs such that fields are separated by a '|' and lines are separated by '\n'. 
   //Null values are represented as -1 for numbers and "NOT-AVAILABLE" for strings. 
   //Only records with product id greater than or equal to 1 and less than or equal to 1000 should be imported and use 3 mappers for importing. 
   //The destination file should be stored as a text file to directory  /user/cloudera/problem5/products-text. 
$ sqoop import \
--connect jdbc:mysql://localhost/retail_db \
--username training \
--password training \
--table products_replica \
--where "product_id between 1 and 1000" \
--delete-target-dir \
--target-dir /user/cloudera/problem5/products-text \
--fields-terminated-by '|' \
--lines-terminated-by '\n' \
--null-non-string -1 \
--null-string "NOT-AVAILABLE" \
--outdir /home/training/Desktop/outdir \
--bindir /home/training/Desktop/bindir \
--num-mappers 3

$ hdfs dfs -ls /user/cloudera/problem5/products-text 
$ hdfs dfs -cat /user/cloudera/problem5/products-text/p*

//2. Using sqoop, import products_replica table from MYSQL into hdfs such that fields are separated by a '*' and lines are separated by '\n'. 
   //Null values are represented as -1000 for numbers and "NA" for strings. Only records with product id less than or equal to 1111 should be imported and use 2 mappers for importing. 
   //The destination file should be stored as a text file to directory  /user/cloudera/problem5/products-text-part1.

$ sqoop import \
--connect jdbc:mysql://localhost/retail_db \
--username training \
--password training \
--table products_replica \
--where "product_id <= 1111" \
--delete-target-dir \
--target-dir /user/cloudera/problem5/products-text-part1 \
--fields-terminated-by '*' \
--lines-terminated-by '\n' \
--null-non-string -1000 \
--null-string "NA" \
--outdir /home/training/Desktop/outdir \
--bindir /home/training/Desktop/bindir \
--num-mappers 2

$ hdfs dfs -ls /user/cloudera/problem5/products-text-part1 
$ hdfs dfs -cat /user/cloudera/problem5/products-text-part1/p*

//3. Using sqoop, import products_replica table from MYSQL into hdfs such that fields are separated by a '*' and lines are separated by '\n'. 
   //Null values are represented as -1000 for numbers and "NA" for strings. Only records with product id greater than 1111 should be imported and use 5 mappers for importing. 
   //The destination file should be stored as a text file to directory  /user/cloudera/problem5/products-text-part2.

$ sqoop import \
--connect jdbc:mysql://localhost/retail_db \
--username training \
--password training \
--table products_replica \
--where "product_id > 1111" \
--delete-target-dir \
--target-dir /user/cloudera/problem5/products-text-part2 \
--fields-terminated-by '*' \
--lines-terminated-by '\n' \
--null-non-string -1000 \
--null-string "NA" \
--outdir /home/training/Desktop/outdir \
--bindir /home/training/Desktop/bindir \
--num-mappers 5

$ hdfs dfs -ls /user/cloudera/problem5/products-text-part2 
$ hdfs dfs -cat /user/cloudera/problem5/products-text-part2/p*

//4. Using sqoop merge data available in /user/cloudera/problem5/products-text-part1 and /user/cloudera/problem5/products-text-part2 to produce a new set of files in /user/cloudera/problem5/products-text-both-parts
$ sqoop merge \
--class-name products_replica \
--jar-file /home/training/Desktop/bindir/products_replica.jar \
--new-data /user/cloudera/problem5/products-text-part1/ \
--onto /user/cloudera/problem5/products-text-part2/ \
--target-dir /user/cloudera/problem5/products-text-both-parts \
--merge-key product_id

$ hdfs dfs -ls /user/cloudera/problem5/products-text-both-parts 
$ hdfs dfs -cat /user/cloudera/problem5/products-text-both-parts/p*

//5. Using sqoop do the following. Read the entire steps before you create the sqoop job.
	//-create a sqoop job Import Products_replica table as text file to directory /user/cloudera/problem5/products-incremental. Import all the records.
	//-insert three more records to Products_replica from mysql
	//-run the sqoop job again so that only newly added records can be pulled from mysql
	//-insert 2 more records to Products_replica from mysql
	//-run the sqoop job again so that only newly added records can be pulled from mysql
	//-Validate to make sure the records have not be duplicated in HDFS

$ sqoop job \
--create myjob_1 \
-- import \
--connect jdbc:mysql://localhost/retail_db \
--username training \
--password training \
--table products_replica \
--delete-target-dir \
--target-dir /user/cloudera/problem5/products-incremental \
--outdir /home/training/Desktop/outdir \
--bindir /home/training/Desktop/bindir \
--num-mappers 1

$ sqoop job --exec myjob_1

$ hdfs dfs -ls /user/cloudera/problem5/products-incremental/ 
$ hdfs dfs -cat /user/cloudera/problem5/products-incremental/p*

$ sqoop job --create first_sqoop_job \
-- import \
--connect jdbc:mysql://localhost/retail_db \
--username training \
--password training \
--table products_replica \
--target-dir /user/cloudera/problem5/products-incremental \
--check-column product_id \
--incremental append \
--last-value 0 \
--outdir /home/training/Desktop/outdir \
--bindir /home/training/Desktop/bindir \
--num-mappers 1

$ sqoop job --exec first_sqoop_job

mysql> insert into products_replica values (56,2,"Dell Computer","A very good computer",300.00,"not avaialble",3,"STRONG");
mysql> insert into products_replica values (57,5,"Headphones Cool","Noisy headphones",356.00,"not avaialble",3,"STRONG");

sqoop job --exec first_sqoop_job

$ hdfs dfs -ls /user/cloudera/problem5/products-incremental/ 
$ hdfs dfs -cat /user/cloudera/problem5/products-incremental/p*

mysql> insert into products_replica values (58,2,"HP Computer","A regular computer",150.00,"not avaialble",1,"WEAK");
mysql> insert into products_replica values (59,5,"Microphone Cool","Good Microphone",25.00,"not avaialble",3,"STRONG");

sqoop job --exec first_sqoop_job

$ hdfs dfs -ls /user/cloudera/problem5/products-incremental/ 
$ hdfs dfs -cat /user/cloudera/problem5/products-incremental/p*

//6. Using sqoop do the following. Read the entire steps before you create the sqoop job.
	//-create a hive table in database named problem5 using below command 
	//-create table products_hive  (product_id int, product_category_id int, product_name string, product_description string, product_price float, product_imaage string,product_grade int,  product_sentiment string);
		$ beeline -n training -p training -u jdbc:hive2://localhost/default 
		hive> create table products_hive  (product_id int, product_category_id int, product_name string, product_description string, product_price float, product_imaage string,product_grade int,  product_sentiment string);
	//-create a sqoop job Import Products_replica table as hive table to database named problem5. name the table as products_hive. 
$ sqoop job \
--create hive_sqoop_job2 \
-- import \
--connect jdbc:mysql://localhost/retail_db \
--username training \
--password training \
--table products_replica \
--check-column product_id \
--incremental append \
--last-value 0 \
--hive-import \
--hive-table products_hive \
--hive-database hadoopexam \
--outdir /home/training/Desktop/outdir \
--bindir /home/training/Desktop/bindir \
--num-mappers 1

$ sqoop job --exec hive_sqoop_job2

	//-insert three more records to Products_replica from mysql
mysql> insert into products_replica values (60,2,"LG TV","A Smart TV",1500.00,"not avaialble",2,"MIDDLE");
mysql> insert into products_replica values (61,8,"Cooler","Good Cooler",25.00,"not avaialble",3,"STRONG");
mysql> insert into products_replica values (62,7,"Gold Bracelet","Beautiful",2500.00,"not avaialble",3,"STRONG");
	//-run the sqoop job again so that only newly added records can be pulled from mysql
$ sqoop job --exec hive_sqoop_job2
	//-insert 2 more records to Products_replica from mysql
mysql> insert into products_replica values (63,9,"Music Tower","Awesome",3210.00,"not avaialble",3,"STRONG");
mysql> insert into products_replica values (64,10,"Books","Harry Potter Series",55.00,"not avaialble",3,"STRONG");
	//-run the sqoop job again so that only newly added records can be pulled from mysql
$ sqoop job --exec hive_sqoop_job2
	//-Validate to make sure the records have not been duplicated in Hive table
hive> select * from products_hive;

//7. Using sqoop do the following. .
	//-insert 2 more records into products_hive table using hive. 
hive> insert into products_hive values (65,12,"Kitchen Furniture","Awesome",3210.00,"not avaialble",3,"STRONG");
hive> insert into products_hive values (67,11,"Books","It",52.00,"not avaialble",3,"STRONG");
	//-create table in mysql using below command   
mysql> create table products_external(product_id int(11) primary Key, product_grade int(11), product_category_id int(11), product_name varchar(100), product_description varchar(100), product_price float, product_impage varchar(500), product_sentiment varchar(100));
	//-export data from products_hive (hive) table to (mysql) products_external table. 
$ sqoop export \
--connect jdbc:mysql://localhost/retail_db \
--username training \
--password training \
--table products_external \
--export-dir /user/hive/warehouse/hadoopexam.db/products_hive \
--input-fields-terminated-by '\001' \
--input-lines-terminated-by '\n' \
--update-key product_id \
--update-mode allowinsert \
--columns "product_id,product_category_id,product_name,product_description,product_price,product_impage,product_grade,product_sentiment" \
--outdir /home/training/Desktop/outdir \
--bindir /home/training/Desktop/bindir \
--num-mappers 1

	//-insert 2 more records to Products_hive table from hive
hive> insert into products_hive values (68,12,"Underwear","Awesome",3210.00,"not avaialble",3,"STRONG");
hive> insert into products_hive values (69,11,"Sushi Food","It",52.00,"not avaialble",3,"STRONG");
	//-export data from products_hive table to products_external table. 
$ sqoop export \
--connect jdbc:mysql://localhost/retail_db \
--username training \
--password training \
--table products_external \
--export-dir /user/hive/warehouse/hadoopexam.db/products_hive \
--input-fields-terminated-by '\001' \
--input-lines-terminated-by '\n' \
--update-key product_id \
--update-mode allowinsert \
--columns "product_id,product_category_id,product_name,product_description,product_price,product_impage,product_grade,product_sentiment" \
--outdir /home/training/Desktop/outdir \
--bindir /home/training/Desktop/bindir \
--num-mappers 1

	//-Validate to make sure the records have not be duplicated in mysql table
mysql> select * from products_external;