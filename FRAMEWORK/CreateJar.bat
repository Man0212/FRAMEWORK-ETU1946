
cd source
javac -cp ../lib/gson-2.8.9.jar;../lib/servlet-api.jar -d classes *.java

#to .jar
cd classes
jar cvf ../../../framework.jar *
cd ../..

