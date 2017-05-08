
function deployOneSite() {
    echo -e "\n------ Transfer package to $1  ... ------\n"
    sshpass -p $2 scp -o StrictHostKeyChecking=no MaoCloud.tar.gz ./MaoCloudDeploy.sh $1:

    echo -e "\n------ Login $1 ... ------\n"
    sshpass -p $2 ssh -o StrictHostKeyChecking=no $1
}

function deployLocal() {

    echo -e "\n------ Stop old version MaoCloud ... ------\n"
    ./MaoCloud/apache-karaf-*/bin/stop
    sleep 3
    ./MaoCloud/apache-karaf-*/bin/status

    echo -e "\n------ Clean old version MaoCloud ... ------\n"
    rm -rf ./MaoCloud/
    mkdir MaoCloud

    echo -e "\n------ Extract new version MaoCloud ... ------\n"
    tar -zxf ./MaoCloud.tar.gz -C ./MaoCloud/

    echo -e "\n------ Start new version MaoCloud ... ------\n"
    ./MaoCloud/apache-karaf-*/bin/start
    sleep 3
    ./MaoCloud/apache-karaf-*/bin/status
}

function usage() {
    echo ""
    echo "    Usage: MaoCloudDeploy.sh <-a|-l> <password file>"
    echo ""
    echo "        -a    Deploy to all definded sites"
    echo ""
    echo "              Transfer .tar.gz to remote site, and extract it"
    echo ""
    echo "        -l    Deploy locally"
    echo ""
    echo "              Run karaf's start script:"
    echo ""
    echo "              ./MaoCloud/apache-karaf-*/bin/start"
    echo ""
}

if [ $# != 2 ]
then
	usage
	exit 1
fi

case $1 in
    -l)
        deployLocal 
        ;;
    -a)
        
        deployOneSite pi@pi-sky.maojianwei.com $(awk -F ',' '{printf $1}' $2)
        
        ;;
    *)
		usage
        ;;
esac

