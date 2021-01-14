package domain.proof.hash.daggerhashimoto;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;



public class DaggerHashimoto {

//  public static int[] createDaggGraph ( int seed, int lenght) {
//  double P = Constants.P;
//  double init, picker;
//  picker = init = ((int) Math.pow(sha512(seed), Constants.w)) % P ;
//  // XOR here
//  int[] o = null;
//  return o;
//
//}

//  There could be a seperate class for the creation of the dag, TBD Later ...
	public static LinkedList<BigInteger> createDaggGraph(BigInteger seed, int lenght) {
		BigInteger P = Constants.P;
		BigInteger init, picker;
		// picker = init = (int) ((Math.pow(sha512(seed), Constants.w)) % P) ;
//        I think this should be sha512(seed).pow...
		picker = init = seed.pow(Constants.w).mod(P);
		LinkedList<BigInteger> o = new LinkedList<BigInteger>() {
			{
				add(init);
			}
		};

		// XOR here
		for (int i = 1; i < lenght; i++) {
			BigInteger x = picker = picker.multiply(init).mod(P);
			for (int j = 0; j < Constants.k; j++) {
				// x ^= o[(x % i)]
//    			I think this needs still the XOR
				x = x.modPow(o.getLast(), P);
			}
			o.addLast(x.pow(Constants.w).mod(P));
			// System.out.println("x is : " + x);
		}
		return o;
	}

////	Keep in mind to think about the places of the following methods (classes)
////	it might be logical to create other class
//	public BigInteger getPreviousHash(int n, Asset asset) throws NoSuchAlgorithmException, IOException {
////		Chain_manager
//		Chain chain = Chain.instance(asset);
//		BigInteger genesisPrevHash = chain.getGenesisBlock().getHashPrevious();
////		Assuming num is chain length for now...
//		int num = chain.size();
//		if (num <= 0) {
////			the implementation of hash_to_int goes here: TBD ...
//			return genesisPrevHash;
//		} else {
////			Think about the previous hash and the use of (n - 1), might need only n
////			as the getHashPrevious gives already the previous hash...
//			BigInteger previousHash = chain.getBlock(n - 1).getHashPrevious();
////			for now using this...Radix 16
////			return Logic.decode_integer(previousHash.toString(16));
//			return previousHash;
//		}
//	}
//
//	public Map<String, BigInteger> getSeedset(Block block, Asset asset) throws NoSuchAlgorithmException, IOException {
//		Map<String, BigInteger> seedset = new HashMap<String, BigInteger>();
//		Chain chain = Chain.instance(asset);
//		int blockNumber = chain.getBlockNumber(block);
//
////		think about these calculations afterwards...
//		seedset.put("back_number", BigInteger.valueOf(blockNumber - (blockNumber % Constants.epochtime)));
//		seedset.put("back_hash", getPreviousHash(seedset.get("back_number").intValue(), asset));
//		seedset.put("front_number",
//				seedset.get("back_number").subtract(BigInteger.valueOf(Constants.epochtime)).max(new BigInteger("0")));
//		seedset.put("front_hash", getPreviousHash(seedset.get("front_number").intValue(), asset));
//		return seedset;
//	}
//	
//	public int getDagSize(Block block, Asset asset) throws NoSuchAlgorithmException, IOException {
////		Think about using Try Catch insead of throwing the exception
////		also think about moving the chain to the constructure so that it will be created once
//		Chain chain = Chain.instance(asset);
//		int blockNumber = chain.getBlockNumber(block);
//		return (int) Constants.n + (Math.floorDiv(blockNumber, Constants.epochtime) * Constants.n_increment);
//	}
//	
//
////	Return type changed from int to BigInteger
//	public BigInteger origHashimoto(Block block, long nonce) {
//
//		BigInteger previousHash = block.getHashPrevious();
//		BigInteger merkleRoot = block.getMerkleRoot();
//		List<Transaction> listOfTransactions = block.getTransactions();
//
////		int hashOutputA = Logic.sha512(previousHash.intValue() + merkleRoot.intValue() + (int) nonce);
//		BigInteger hashOutputA = Logic.sha512(previousHash.add(merkleRoot).add(BigInteger.valueOf(nonce)));
////		int txidMix = 0;
//		BigInteger txidMix = BigInteger.valueOf(0);
//
//		for (int i = 0; i < 64; i++) {
////			int shiftedA = hashOutputA >> i;
//			BigInteger shiftedA = hashOutputA.shiftRight(i);
////			int transaction = shiftedA % listOfTransactions.size();
//			BigInteger transaction = shiftedA.mod(BigInteger.valueOf(listOfTransactions.size()));
//
////			txidMix ^= listOfTransactions.get(transaction).getOwnHash().intValue() << i;
////			One of these next 2 is correct: TBD ...
////			txidMix = txidMix.xor(listOfTransactions.get(transaction.intValue()).getOwnHash()).shiftLeft(i);
//			txidMix = txidMix.xor(listOfTransactions.get(transaction.intValue()).getOwnHash().shiftLeft(i));
//		}
////		return txidMix ^ ((int)nonce << 192);
//		return txidMix.xor(BigInteger.valueOf(nonce).shiftLeft(192));
//	}
	
    public static void main (String[] args) {
    	
    	System.out.println("decoder " + Logic.decode_integer("Hello there"));

        BigInteger big = new BigInteger("87521618088882658227876453");
        System.out.println("encoder " + Logic.encode_integer(big));
        
        System.out.println("SHA3: " + Logic.sha512(big));
        System.out.println("Double SHA3: "+ Logic.doubleSha512(big));

        System.out.println("Graph "+ createDaggGraph(big, 10));

        System.out.println();
        System.out.println(Constants.P);
        System.out.println((Constants.P).longValue());
        System.out.println((Constants.P).doubleValue());
       
//      Testing quickCalc() which is in Logic...  
        System.out.println("quickCalc() here: " + Logic.quickCalc(big, 2));

    }

}
