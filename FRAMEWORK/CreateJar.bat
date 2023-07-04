
cd source
javac -cp ../lib/servlet-api.jar -d classes *.java

#to .jar
cd classes
jar cvf ../../../framework.jar *
cd ../..

