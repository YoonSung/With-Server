java -cp ./sql/h2-1.3.167.jar org.h2.tools.Script -url jdbc:h2:tcp://localhost/~/gaongil -user yoon -script ./sql/dump.zip -options compression zip