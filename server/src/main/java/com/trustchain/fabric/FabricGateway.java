package com.trustchain.fabric;

import org.hyperledger.fabric.gateway.*;
import org.hyperledger.fabric.sdk.Peer;

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
import java.util.EnumSet;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class FabricGateway {
    private static final Logger logger = LogManager.getLogger(FabricGateway.class);

    private final String networkConfigPath = "src/main/resources/connections.json";
    private final String channelName = "medicinechannel";
    private final String contractName = "trustchain";
    private final String mspID = "BHospitalMSP";
    private final String userName = "Admin";
    private final String cryptoPath = "src/main/resources/crypto-config/";
    private final String certFile = cryptoPath + "peerOrganizations/BHospital.trustchain.com/users/Admin@BHospital.trustchain.com/msp/signcerts/Admin@BHospital.trustchain.com-cert.pem";
    private final String keyFile = cryptoPath + "peerOrganizations/BHospital.trustchain.com/users/Admin@BHospital.trustchain.com/msp/keystore/75214e9125760153e6c131983549207b7d26a2f17cc96ceeb50329abc402f777_sk";

//    private final String userName = "Admin";
//    private final String mspID = "JDMSP";
//    private final String channelName = "appchannel";
//    private final String contractName = "fabric-realty";
//    private final String networkConfigPath = "src/main/resources/test.json";
//    private final String cryptoPath = "/home/node9/Code/Trusted-Sharing-of-Data-Resources/fabric-realty/network/crypto-config/";
//    private final String certFile = cryptoPath + "peerOrganizations/jd.com/users/Admin@jd.com/msp/signcerts/Admin@jd.com-cert.pem";
//    private final String keyFile = cryptoPath + "peerOrganizations/jd.com/users/Admin@jd.com/msp/keystore/bc434b71d556d5dbfd926e84a34ff9f445010ca2ec430a07e444a8b7e0e0ff62_sk";

    private X509Certificate certificate;
    private PrivateKey privateKey;

    public FabricGateway() {
        try {
            certificate = readX509Certificate(Paths.get(certFile));
            privateKey = getPrivateKey(Paths.get(keyFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String query(String functionName, String... args) {
        try {
            Wallet wallet = Wallets.newInMemoryWallet();
            wallet.put(userName, Identities.newX509Identity(mspID, certificate, privateKey));

            Gateway.Builder builder = Gateway.createBuilder().identity(wallet, userName).networkConfig(Paths.get(networkConfigPath));
            Gateway gateway = builder.connect();
            Network network = gateway.getNetwork(channelName);
            Contract contract = network.getContract(contractName);

            return new String(contract.evaluateTransaction(functionName, args), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String invoke(String functionName, String... args) {
        try {
            Wallet wallet = Wallets.newInMemoryWallet();
            wallet.put(userName, Identities.newX509Identity(mspID, certificate, privateKey));

            Gateway.Builder builder = Gateway.createBuilder().identity(wallet, userName).networkConfig(Paths.get(networkConfigPath));
            Gateway gateway = builder.connect();
            Network network = gateway.getNetwork(channelName);
            Contract contract = network.getContract(contractName);

            // TODO: 背书节点选择
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