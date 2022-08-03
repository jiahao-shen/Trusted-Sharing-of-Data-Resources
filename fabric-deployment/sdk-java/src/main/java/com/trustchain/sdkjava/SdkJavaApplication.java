package com.trustchain.sdkjava;


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

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;




// @SpringBootApplication
public class SdkJavaApplication {

    public static void main(String[] args) {
        try {
            String networkConfigPath = "src/main/resources/connections.json";
            String channelName = "medicinechannel";
            String contractName = "chaincode";
    
            String certificatePath = "src/main/resources/crypto-config/peerOrganizations/BHospital.trustchain.com/users/Admin@BHospital.trustchain.com/msp/signcerts/Admin@BHospital.trustchain.com-cert.pem";
            X509Certificate certificate = readX509Certificate(Paths.get(certificatePath));

            String privateKeyPath = "src/main/resources/crypto-config/peerOrganizations/BHospital.trustchain.com/users/Admin@BHospital.trustchain.com/msp/keystore/75214e9125760153e6c131983549207b7d26a2f17cc96ceeb50329abc402f777_sk";
            PrivateKey privateKey = getPrivateKey(Paths.get(privateKeyPath));

            Wallet wallet = Wallets.newInMemoryWallet();
            wallet.put("Admin", Identities.newX509Identity("BHospitalMSP",certificate,privateKey));

            // 根据connection.json 获取Fabric网络连接对象
            Gateway.Builder builder = Gateway.createBuilder()
                    .identity(wallet, "Admin")
                    .networkConfig(Paths.get(networkConfigPath));
             //连接网关
            Gateway gateway = builder.connect();
            //获取通道
            Network network = gateway.getNetwork(channelName);
            //获取合约对象
            Contract contract = network.getContract(contractName);

            byte[] queryAllAssetsAfter = contract.evaluateTransaction("hello");
            System.out.println(new String(queryAllAssetsAfter, StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
            
        // LocalUser user = new LocalUser("Admin", "BHospitalMSP", "/home/node4/projects/Trusted-Sharing-of-Data-Resources/fabric-deployment/sdk-java/src/main/resources/crypto-config/peerOrganizations/BHospital.trustchain.com/users/Admin@BHospital.trustchain.com/msp/keystore/75214e9125760153e6c131983549207b7d26a2f17cc96ceeb50329abc402f777_sk", "/home/node4/projects/Trusted-Sharing-of-Data-Resources/fabric-deployment/sdk-java/src/main/resources/crypto-config/peerOrganizations/BHospital.trustchain.com/users/Admin@BHospital.trustchain.com/msp/signcerts/Admin@BHospital.trustchain.com-cert.pem");

        // HFClient client = HFClient.createNewInstance();
        // client.setCryptoSuite(CryptoSuite.Factory.getCryptoSuite());
        // client.setUserContext(user);

        // Channel channel = client.newChannel("medicinechannel");
        // Peer peer = client.newPeer("peer0.BHospital.trustchain.com", "grpcs://192.168.8.139:7051");
        // channel.addPeer(peer);
        // Orderer orderer = client.newOrderer("orderer1.trustchain.com", "grpcs://192.168.8.112:7050");
        // channel.addOrderer(orderer);
        // channel.initialize();
        // // SpringApplication.run(SdkJavaApplication.class, args);

        // QueryByChaincodeRequest req = client.newQueryProposalRequest();
        // ChaincodeID cid = ChaincodeID.newBuilder().setName("mycc").build();
        // req.setChaincodeID(cid);
        // req.setFcn("query");
        // req.setArgs("a");

        // Collection<ProposalResponse> resp = channel.queryByChaincode(req);
        // System.out.println(resp);

    }

    private static X509Certificate readX509Certificate(final Path certificatePath) throws IOException, CertificateException {
        try (Reader certificateReader = Files.newBufferedReader(certificatePath, StandardCharsets.UTF_8)) {
            return Identities.readX509Certificate(certificateReader);
        }
    }

    private static PrivateKey getPrivateKey(final Path privateKeyPath) throws IOException, InvalidKeyException, IOException {
        try (Reader privateKeyReader = Files.newBufferedReader(privateKeyPath, StandardCharsets.UTF_8)) {
            return Identities.readPrivateKey(privateKeyReader);
        }
    }
    
}

// class LocalUser implements User {
//     private String name;
//     private String mspId;
//     private Enrollment enrollment;

//     LocalUser(String name, String mspId, String keyFile, String certFile) {
//         this.name = name;
//         this.mspId = mspId;
//         this.enrollment = loadFromPemFile(keyFile, certFile);
//     }

//     private Enrollment loadFromPemFile(String keyFile, String certFile) {
//         try {
            
//             byte[] keyPem = Files.readAllBytes(Paths.get(keyFile));
//             byte[] certPem = Files.readAllBytes(Paths.get(certFile));
//             CryptoPrimitives suite = new CryptoPrimitives();
//             PrivateKey privateKey = suite.bytesToPrivateKey(keyPem);
//             return new X509Enrollment(privateKey, new String(certPem));
//         } catch (Exception e) {
//             System.out.println("Fuck");
//             return null;
//         }
//     }

//     @Override
//     public String getName() {
//         return this.name;
//     }

//     @Override
//     public String getMspId() {
//         return this.mspId;
//     }

//     @Override
//     public Enrollment getEnrollment() {
//         return this.enrollment;
//     }

//     @Override
//     public String getAccount() {
//         return null;
//     }

//     @Override
//     public String getAffiliation() {
//         return null;
//     }

//     @Override
//     public Set<String> getRoles() {
//         return null;
//     }
// }