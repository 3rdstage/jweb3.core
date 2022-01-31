### Developer Resources

* [Contract ABI Specification](https://docs.soliditylang.org/en/latest/abi-spec.html)
* [Schema Description: Truffle Contract Object](https://github.com/trufflesuite/truffle-contract-schema)
* [Spring Data Commons](https://github.com/spring-projects/spring-data-commons)
* [Spring Data Commons - Reference Documentation](https://docs.spring.io/spring-data/commons/docs/current/reference/html/#reference)

----

### ABI

> The first four bytes of the call data for a function call specifies the function to be called.
> The return type of a function is not part of this signature. In Solidity’s function overloading return types are not considered.
> The JSON description of the ABI however contains both inputs and outputs.


----

### Ethereum Signing

* [Ethereum Signing](https://github.com/3rdstage/models/tree/master/blockchain/signing)

| ![`web3.js` Signing Workflow](https://raw.githubusercontent.com/3rdstage/models/master/blockchain/signing/transaction-signing-workflow-web3js.svg) |
| ------ |
| ![`web3j` Signing Workflow](https://raw.githubusercontent.com/3rdstage/models/master/blockchain/signing/transaction-signing-workflow-web3j-4.8.svg) |


----

### ECDSA Signing

* [`org.bouncycastle.crypto.signers.ECDSASigner.generateSignature(byte[])`](https://github.com/bcgit/bc-java/blob/r1rv68/core/src/main/java/org/bouncycastle/crypto/signers/ECDSASigner.java#L93)
* [`elliptic.EC.prototype.sign(msg, key, enc, options)`](https://github.com/indutny/elliptic/blob/43ac7f230069bd1575e1e4a58394a512303ba803/lib/elliptic/ec/index.js#L91)


----
