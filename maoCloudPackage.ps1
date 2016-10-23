mvn clean package
mkdir .\deploy\Target
tar -xf .\deploy\apache-karaf-3.0.8.tar.gz -C .\deploy\Target\
xcopy /Y/V .\branding\target\mao-cloud-branding-*.jar .\deploy\Target\apache-karaf-3.0.8\lib\
xcopy /Y/V .\deploy\karaf\etc\* .\deploy\Target\apache-karaf-3.0.8\etc\
xcopy /S/Y/V .\deploy\karaf\system\* .\deploy\Target\apache-karaf-3.0.8\system\
xcopy /S/Y/V .\deploy\karaf\deploy\* .\deploy\Target\apache-karaf-3.0.8\deploy\
xcopy /Y/V .\core\target\mao-cloud-core-*.jar .\deploy\Target\apache-karaf-3.0.8\deploy\
cd ./deploy/Target/
tar -cf ./MaoCloud.tar.gz apache-karaf-3.0.8/
cd ../../
