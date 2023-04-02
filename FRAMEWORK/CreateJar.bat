#compilation
cd source
javac -d classes *.java

#to .jar
cd classes
jar cvf ../../../framework.jar *
