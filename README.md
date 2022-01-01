### Developer Resource

* [Contract ABI Specification](https://docs.soliditylang.org/en/latest/abi-spec.html)
* [Schema Description: Truffle Contract Object](https://github.com/trufflesuite/truffle-contract-schema)
* [Spring Data Commons](https://github.com/spring-projects/spring-data-commons)
* [Spring Data Commons - Reference Documentation](https://docs.spring.io/spring-data/commons/docs/current/reference/html/#reference)


### A Few Lies from ABI Spec

> The first four bytes of the call data for a function call specifies the function to be called.
> The return type of a function is not part of this signature. In Solidity’s function overloading return types are not considered.
> The JSON description of the ABI however contains both inputs and outputs.


### Signing Workflow

* [`org.web3j.crypto.TransactionEncoder.encode(RawTransaction tx, SignatureData.sign)`](https://github.com/web3j/web3j/blob/v4.8.8/crypto/src/main/java/org/web3j/crypto/TransactionEncoder.java#L90)
* [`org.web3j.crypto.TransactionEncoder.asRlpValues(RawTransaction tx, SignatureData.sign)`](https://github.com/web3j/web3j/blob/v4.8.8/crypto/src/main/java/org/web3j/crypto/TransactionEncoder.java#L113)

