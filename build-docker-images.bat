
pushd oauth-server
docker build ./ -t oauth-service-app
popd

pushd resource-server
docker build ./ -t resource-service-app
popd
