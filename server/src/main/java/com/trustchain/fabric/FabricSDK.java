package com.trustchain.fabric;

import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.identity.X509Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoPrimitives;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;

public class FabricSDK {
    private final String userName = "Admin";
    private final String mspID = "BHospitalMSP";
    private final String channelName = "medicinechannel";
    private final String contractName = "chaincode";
    private final String cryptoPath = "src/main/resources/crypto-config/";
    private final String keyFile = cryptoPath + "peerOrganizations/BHospital.trustchain.com/users/Admin@BHospital.trustchain.com/msp/keystore/75214e9125760153e6c131983549207b7d26a2f17cc96ceeb50329abc402f777_sk";
    private final String certFile = cryptoPath + "peerOrganizations/BHospital.trustchain.com/users/Admin@BHospital.trustchain.com/msp/signcerts/Admin@BHospital.trustchain.com-cert.pem";

    private HFClient client;
    private Channel channel;

    public FabricSDK() {
        try {
            client = HFClient.createNewInstance();
            client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            client.setUserContext(new LocalUser(userName, mspID, keyFile, certFile));

            channel = client.newChannel(channelName);
            Properties proper;
            proper = loadTLSFile(cryptoPath + "peerOrganizations/BHospital.trustchain.com/peers/peer0.BHospital.trustchain.com/msp/tlscacerts/tlsca.BHospital.trustchain.com-cert.pem", "peer0.BHospital.trustchain.com");
            Peer peer = client.newPeer("peer0.BHospital.trustchain.com", "grpcs://192.168.8.139:7051", proper);
            channel.addPeer(peer);

            proper = loadTLSFile(cryptoPath + "ordererOrganizations/trustchain.com/orderers/orderer1.trustchain.com/msp/tlscacerts/tlsca.trustchain.com-cert.pem", "orderer1.trustchain.com");
            Orderer orderer = client.newOrderer("orderer1.trustchain.com", "grpcs://192.168.8.112:7050", proper);
            channel.addOrderer(orderer);

            channel.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String query(String functionName, String... args) {
        try {
            QueryByChaincodeRequest req = client.newQueryProposalRequest();
            req.setChaincodeName(contractName);
            req.setFcn(functionName);
            req.setArgs(args);
            Collection<ProposalResponse> res = channel.queryByChaincode(req);
            return res.toArray(new ProposalResponse[0])[0].getProposalResponse().getResponse().getPayload().toStringUtf8();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public String invoke(String functionName, String... args) {
        try {
            TransactionProposalRequest req = client.newTransactionProposalRequest();
            req.setChaincodeName(contractName);
            req.setFcn(functionName);
            req.setArgs(args);
            Collection<ProposalResponse> res = channel.sendTransactionProposal(req);
            BlockEvent.TransactionEvent event = channel.sendTransaction(res).get();
            return res.toArray(new ProposalResponse[0])[0].getProposalResponse().getResponse().getPayload().toStringUtf8();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private Properties loadTLSFile(String tlsCertPath, String hostName) throws IOException {
        Properties properties = new Properties();
        properties.put("pemBytes", Files.readAllBytes(Paths.get(tlsCertPath)));
        properties.setProperty("sslProvider", "openSSL");
        properties.setProperty("negotiationType", "TLS");
        properties.setProperty("trustServerCertificate", "true");
        properties.setProperty("hostnameOverride", hostName);
        return properties;
    }
}

class LocalUser implements User {
    private String name;
    private String mspId;
    private Enrollment enrollment;

    LocalUser(String name, String mspId, String keyFile, String certFile) throws Exception {
        this.name = name;
        this.mspId = mspId;
        this.enrollment = loadFromPemFile(keyFile, certFile);
    }

    private Enrollment loadFromPemFile(String keyFile, String certFile) throws Exception {
        byte[] keyPem = Files.readAllBytes(Paths.get(keyFile));
        byte[] certPem = Files.readAllBytes(Paths.get(certFile));
        CryptoPrimitives suite = new CryptoPrimitives();
        PrivateKey privateKey = suite.bytesToPrivateKey(keyPem);
        return new X509Enrollment(privateKey, new String(certPem));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Set<String> getRoles() {
        return null;
    }

    @Override
    public String getAccount() {
        return null;
    }

    @Override
    public String getAffiliation() {
        return null;
    }

    @Override
    public Enrollment getEnrollment() {
        return enrollment;
    }

    @Override
    public String getMspId() {
        return mspId;
    }
}


