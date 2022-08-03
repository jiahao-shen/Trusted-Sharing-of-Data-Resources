# coding=utf-8
import asyncio
from hfc.fabric import Client

# https://fabric-sdk-py.readthedocs.io/en/latest/tutorial.html
# https://github.com/hyperledger/fabric-sdk-py
loop = asyncio.get_event_loop()
cli = Client(net_profile="network.json")
orgB_admin = cli.get_user('peer0.BHospital.trustchain.com', 'Admin')


cli.new_channel('medicinechannel')


# 查询peer加入的通道 
response = loop.run_until_complete(cli.query_channels(
               requestor=orgB_admin,
               peers=['peer0.BHospital.trustchain.com'],
               decode=True
               ))

"""
# An example response:

channels {
  channel_id: "medicinechannel"
}
"""


#

import os
gopath_bak = os.environ.get('GOPATH', '')
gopath = os.path.normpath(os.path.join(
                      os.path.dirname(os.path.realpath('__file__')),
                      'test/fixtures/chaincode'
                     ))
os.environ['GOPATH'] = os.path.abspath(gopath)

# The response should be true if succeed
# responses = loop.run_until_complete(cli.chaincode_install(
#                requestor=orgB_admin,
#                peers=['peer0.BHospital.trustchain.com'],
#                cc_path='github.com/example_cc',
#                cc_name='example_cc',
#                cc_version='v1.0'
#                ))
# print(responses)

# Instantiate Chaincode in Channel, the response should be true if succeed
# args = ['a', '200', 'b', '300']

# # policy, see https://hyperledger-fabric.readthedocs.io/en/release-1.4/endorsement-policies.html
# policy = {
#     'identities': [
#         {'role': {'name': 'member', 'mspId': 'BHospitalMSP'}},
#     ],
#     'policy': {
#         '1-of': [
#             {'signed-by': 0},
#         ]
#     }
# }
# response = loop.run_until_complete(cli.chaincode_instantiate(
#                requestor=orgB_admin,
#                channel_name='medicinechannel',
#                peers=['peer0.BHospital.trustchain.com'],
#                args=args,
#                cc_name='example_cc',
#                cc_version='v1.0',
#                cc_endorsement_policy=policy, # optional, but recommended
#                collections_config=None, # optional, for private data policy
#                transient_map=None, # optional, for private data
#                wait_for_event=True # optional, for being sure chaincode is instantiated
#                ))

# print(response)

# Invoke a chaincode
# args = ['a', 'b', '100']
# # The response should be true if succeed
# response = loop.run_until_complete(cli.chaincode_invoke(
#                requestor=orgB_admin,
#                channel_name='medicinechannel',
#                peers=['peer0.BHospital.trustchain.com'],
#                args=args,
#                cc_name='example_cc',
#                transient_map=None, # optional, for private data
#                wait_for_event=True, # for being sure chaincode invocation has been commited in the ledger, default is on tx event
#                #cc_pattern='^invoked*' # if you want to wait for chaincode event and you have a `stub.SetEvent("invoked", value)` in your chaincode
#                ))


# print(response)

#
# Query a chaincode

# chaincode_query(self, requestor, channel_name, peers, args, cc_name, cc_type=CC_TYPE_GOLANG, fcn='query', transient_map=None)

# args = ['b']
# # The response should be true if succeed
# response = loop.run_until_complete(cli.chaincode_query(
#                requestor=orgB_admin,
#                channel_name='medicinechannel',
#                peers=['peer0.BHospital.trustchain.com'],
#                args=args,
#                cc_name='example_cc'
#                ))

# print(response)






# # Invoke a chaincode
args = ['a', 'b', '10']
# # The response should be true if succeed
response = loop.run_until_complete(cli.chaincode_invoke(
               requestor=orgB_admin,
               channel_name='medicinechannel',
               peers=['peer0.BHospital.trustchain.com'],
               args=args,
               cc_name='mycc',
               transient_map=None, # optional, for private data
               wait_for_event=True, # for being sure chaincode invocation has been commited in the ledger, default is on tx event
               #cc_pattern='^invoked*' # if you want to wait for chaincode event and you have a `stub.SetEvent("invoked", value)` in your chaincode
               ))
print("转账成功 a->b: 10")

# Query a chaincode

args = ['a']

# The response should be true if succeed
response = loop.run_until_complete(cli.chaincode_query(
               requestor=orgB_admin,
               channel_name='medicinechannel',
               peers=['peer0.BHospital.trustchain.com'],
               args=args,
               cc_name='mycc',
              #  decode=False
               ))
print(response)




# Query a chaincode

args = []

# # The response should be true if succeed
response = loop.run_until_complete(cli.chaincode_query(
               requestor=orgB_admin,
               channel_name='medicinechannel',
               peers=['peer0.BHospital.trustchain.com'],
               fcn='queryOrganizationList',
               args=args,
               cc_name='chaincode'
               ))
print(response)