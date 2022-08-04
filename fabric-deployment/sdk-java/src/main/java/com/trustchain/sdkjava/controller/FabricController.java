package com.trustchain.sdkjava.controller;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Properties;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hyperledger.fabric.gateway.*;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.identity.X509Enrollment;
import org.hyperledger.fabric.sdk.security.CryptoPrimitives;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/fabric")
public class FabricController {
    @PostMapping(value = "/test", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Person test(@RequestBody Person person) {
        System.out.println(person);

        person.setName("plus");
        person.setAge(20);
        return person;
    }

    @GetMapping(value = "/query")
    public String query() {
        FabricSDK fs = new FabricSDK();
        return fs.query("queryOrganizationList");

//        FabricGateWay fg = new FabricGateWay();
//        return fg.query("query", "a");
    }

    @GetMapping("/invoke")
    public String invoke() {
        FabricSDK fs = new FabricSDK();
        return fs.invoke("createOrganization", "test", "test", "test", "test", "", "1997-06-02 00:00:00");

//        FabricGateWay fg = new FabricGateWay();
//        return fg.invoke("invoke", "b", "a", "20");
    }

}

@Getter
@Setter
@ToString
class Person {
    // Getter和Setter必须实现!!
    private String name;
    private int age;
}

/**
 * Fabric-Gateway-Java
 */
class FabricGateWay {
    private final String networkConfigPath = "src/main/resources/connections.json";
    private final String channelName = "medicinechannel";
    private final String contractName = "mycc";
    private final String cryptoPath = "src/main/resources/crypto-config/";
    private final String certificatePath = cryptoPath + "peerOrganizations/BHospital.trustchain.com/users/Admin@BHospital.trustchain.com/msp/signcerts/Admin@BHospital.trustchain.com-cert.pem";
    private final String privateKeyPath = cryptoPath + "peerOrganizations/BHospital.trustchain.com/users/Admin@BHospital.trustchain.com/msp/keystore/75214e9125760153e6c131983549207b7d26a2f17cc96ceeb50329abc402f777_sk";
    private X509Certificate certificate;
    private PrivateKey privateKey;

    FabricGateWay() {
        try {
            certificate = readX509Certificate(Paths.get(certificatePath));
            privateKey = getPrivateKey(Paths.get(privateKeyPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String query(String functionName, String... args) {
        try {
            Wallet wallet = Wallets.newInMemoryWallet();
            wallet.put("Admin", Identities.newX509Identity("BHospitalMSP", certificate, privateKey));

            Gateway.Builder builder = Gateway.createBuilder().identity(wallet, "Admin").networkConfig(Paths.get(networkConfigPath));
            Gateway gateway = builder.connect();
            Network network = gateway.getNetwork(channelName);
            Contract contract = network.getContract(contractName);

            return new String(contract.evaluateTransaction(functionName, args), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    String invoke(String functionName, String... args) {
        try {
            Wallet wallet = Wallets.newInMemoryWallet();
            wallet.put("Admin", Identities.newX509Identity("BHospitalMSP", certificate, privateKey));

            Gateway.Builder builder = Gateway.createBuilder().identity(wallet, "Admin").networkConfig(Paths.get(networkConfigPath));
            Gateway gateway = builder.connect();
            Network network = gateway.getNetwork(channelName);
            Contract contract = network.getContract(contractName);

            // TODO: 背书节点
            return new String(contract.createTransaction(functionName).setEndorsingPeers(network.getChannel().getPeers(EnumSet.of(Peer.PeerRole.ENDORSING_PEER))).submit(args), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private X509Certificate readX509Certificate(final Path certificatePath) throws IOException, CertificateException {
        try (Reader certificateReader = Files.newBufferedReader(certificatePath, StandardCharsets.UTF_8)) {
            return Identities.readX509Certificate(certificateReader);
        }
    }

    private static PrivateKey getPrivateKey(final Path privateKeyPath) throws InvalidKeyException, IOException {
        try (Reader privateKeyReader = Files.newBufferedReader(privateKeyPath, StandardCharsets.UTF_8)) {
            return Identities.readPrivateKey(privateKeyReader);
        }
    }

}

/**
 * Fabric-SDK-Java
 */
class FabricSDK {
    private final String channelName = "medicinechannel";
    private final String contractName = "chaincode";
    private final String cryptoPath = "src/main/resources/crypto-config/";
    private final String keyFile = cryptoPath + "peerOrganizations/BHospital.trustchain.com/users/Admin@BHospital.trustchain.com/msp/keystore/75214e9125760153e6c131983549207b7d26a2f17cc96ceeb50329abc402f777_sk";
    private final String certFile = cryptoPath + "peerOrganizations/BHospital.trustchain.com/users/Admin@BHospital.trustchain.com/msp/signcerts/Admin@BHospital.trustchain.com-cert.pem";

    private HFClient client;
    private Channel channel;

    FabricSDK() {
        try {
            client = HFClient.createNewInstance();
            client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
            client.setUserContext(new LocalUser("Admin", "BHospitalMSP", keyFile, certFile));

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

    String query(String functionName, String... args) {
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

    String invoke(String functionName, String... args) {
        try {
            TransactionProposalRequest req = client.newTransactionProposalRequest();
            req.setChaincodeName(contractName);
            req.setFcn(functionName);
            req.setArgs(args);
            Collection<ProposalResponse> res = channel.sendTransactionProposal(req);
            TransactionEvent event = channel.sendTransaction(res).get();
            System.out.println(event.isValid());
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

