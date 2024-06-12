## Weign

    Weign

## Description

    Read Web3 Contract Like Feign    
    The project make it easier to access the contract

## Sample

```xml

<dependency>
    <groupId>io.github.renwairen</groupId>
    <artifactId>weign</artifactId>
    <version>0.0.1</version>
</dependency>
```

```java
/**
 *  ERC-20 Contract, the abi file locate in /resources/abi/ERC-20.json
 *  <p> define an interface mapping to the abi
 *  <p> the tuple type mapping to java class
 *  <p> if the function return multiple results, you need to wrap them to a java class 
 */
@Contract("/abi/ERC-20.json")
public interface ERC20 {

    String name();

    @Function(name = "totalSupply")
    BigInteger totalSupply();

    Integer decimals();

    BigInteger balanceOf(String address);

    String symbol();

    /**
     * Withdrawal event
     */
    @Data
    class Withdrawal {

        private String src;

        @JsonProperty("wad")
        private BigInteger wad;
    }
}

```

```java
/***
 * call method in ERC-20 contract and parse contract event
 */
class ContractBuilderTest {

    /**
     * WETH
     * <p>https://etherscan.io/token/0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2
     */
    @Test
    void erc20() throws IOException {
        //read contract
        ExecuteOption option = ExecuteOption.builder()
                //the web3j instance
                .web3j(Constant.WEB_3_J)
                //contract address
                .contract("0xc02aaa39b223fe8d0a0e5c4f27ead9083c756cc2")
                //we call the function in latest block by default, you can set a previous one
                //.blockParameter(...)
                .build();
        //instance the interface
        ERC20 erc20 = ContractBuilder.create(option, ERC20.class);
        //call the function
        Assertions.assertEquals("WETH", erc20.symbol());
        Assertions.assertEquals("Wrapped Ether", erc20.name());
        Assertions.assertEquals(18, erc20.decimals());
        Assertions.assertTrue(erc20.balanceOf("0x3AfBAE812F3C29b5926504250888415a01aaC57f").compareTo(BigInteger.ZERO) >= 0);

        //parse event
        //https://etherscan.io/tx/0xfae94b7f014b77cc4dc04d7b1ee523b88198c5cb03dc9e107ede83833c1e4908#eventlog
        TransactionReceipt receipt = Constant.WEB_3_J.ethGetTransactionReceipt("0xfae94b7f014b77cc4dc04d7b1ee523b88198c5cb03dc9e107ede83833c1e4908")
                .send().getTransactionReceipt().get();
        Log log = receipt.getLogs().stream().filter(l -> Objects.equals(BigInteger.valueOf(209), l.getLogIndex()))
                .findAny().get();
        ABI abi = ABIBuilder.clazz(ERC20.class);
        EventData eventData = abi.decodeEvent(log.getTopics(), log.getData());
        ERC20.Withdrawal withdrawal = JsonUtil.read(eventData.getAll(), ERC20.Withdrawal.class);
        Assertions.assertEquals("0x22F9dCF4647084d6C31b2765F6910cd85C178C18".toLowerCase(), withdrawal.getSrc());
        Assertions.assertEquals(new BigInteger("14706622945065745"), withdrawal.getWad());
    }
}
```


