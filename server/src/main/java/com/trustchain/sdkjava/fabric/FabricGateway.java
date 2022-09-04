package com.trustchain.sdkjava.fabric;

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

public class FabricGateway {
    private final String networkConfigPath = "src/main/resources/connections.json";
    private final String channelName = "medicinechannel";
    private final String contractName = "chaincode";
    private final String cryptoPath = "src/main/resources/crypto-config/";
    private final String certificatePath = cryptoPath + "peerOrganizations/BHospital.trustchain.com/users/Admin@BHospital.trustchain.com/msp/signcerts/Admin@BHospital.trustchain.com-cert.pem";
    private final String privateKeyPath = cryptoPath + "peerOrganizations/BHospital.trustchain.com/users/Admin@BHospital.trustchain.com/msp/keystore/75214e9125760153e6c131983549207b7d26a2f17cc96ceeb50329abc402f777_sk";
    private X509Certificate certificate;
    private PrivateKey privateKey;

    public FabricGateway() {
        try {
            certificate = readX509Certificate(Paths.get(certificatePath));
            privateKey = getPrivateKey(Paths.get(privateKeyPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String query(String functionName, String... args) {
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

    public String invoke(String functionName, String... args) {
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