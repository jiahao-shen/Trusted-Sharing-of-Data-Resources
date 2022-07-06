#!/bin/bash

docker-compose down -v

peer chaincode instantiate -o orderer1.trustchain.com:7050 -C medicinechannel -n chaincode -l golang -v 1.0 -c '{"Args":["init"]}' -P "OR ('AHospitalMSP.peer', 'BHospitalMSP.peer')" --cafile /opt/gopath/src/github.com/hyperledger/fabric/peer/crypto/ordererOrganizations/trustchain.com/orderers/orderer1.trustchain.com/msp/tlscacerts/tlsca.trustchain.com-cert.pem --tls