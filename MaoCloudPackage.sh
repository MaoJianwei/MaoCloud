echo -e "\n---------- Building Mao Cloud Core ... ----------\n"
mvn clean package
mkdir ./deploy/target

echo -e "\n---------- Init karaf system ... ----------\n"
tar -zxf ./deploy/apache-karaf-3.0.8.tar.gz -C ./deploy/target/

echo -e "\n---------- Packaging Branding ... ----------\n"
cp -vrf ./branding/target/mao-cloud-branding-*.jar ./deploy/target/apache-karaf-3.0.8/lib/

echo -e "\n---------- Packaging Config ... ----------\n"
cp -vrf ./deploy/karaf/etc/* ./deploy/target/apache-karaf-3.0.8/etc/

echo -e "\n---------- Packaging Basement ... ----------\n"
cp -vrf ./deploy/karaf/system/* ./deploy/target/apache-karaf-3.0.8/system/

echo -e "\n---------- Packaging Dependency ... ----------\n"
cp -vrf ./deploy/karaf/deploy/* ./deploy/target/apache-karaf-3.0.8/deploy/

echo -e "\n---------- Packaging Mao Cloud Core ... ----------\n"
cp -vrf ./core/target/mao-cloud-core-*.jar ./deploy/target/apache-karaf-3.0.8/deploy/

echo -e "\n---------- Packaging Mao Cloud Platform ... ----------\n"
cd ./deploy/target/
tar -zcf ./MaoCloud.tar.gz apache-karaf-3.0.8/

echo -e "\n---------- Clear temp ... ----------\n"
rm -rf ./apache-karaf-3.0.8/
cd ../../
