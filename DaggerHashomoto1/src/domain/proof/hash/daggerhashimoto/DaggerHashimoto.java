package domain.proof.hash.daggerhashimoto;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

//import domain.Asset;
//import domain.Block;
//import domain.Chain;
//import domain.Transaction;

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
	
	
	public static BigInteger quickHashimoto(BigInteger seed, int dagSize, BigInteger header, long nonce) {
//		m = dagSize // 2
		int m = Math.floorDiv(dagSize, 2);
		BigInteger mix = Logic.sha512(BigInteger.valueOf(nonce).add(header));

		for (int i = 0; i < Constants.access; i++) {
//			Can make this formula work with all BigInteger by making Loig.quickCalc() accept a BigInteger as well instead of int.
//			Hence, we wont need to use the intValue of the BigInteger at the end
//			For now, we make it work with int as well by doing the following
//			(mix % 2**64):
			BigInteger value_1 = BigInteger.valueOf(2).pow(64); // 2**64
			BigInteger value_2 = mix.mod(value_1); // mix % 2**64
//			mix ^= quick_calc(params, seed, m + (mix % 2**64) % m)
			mix = mix.xor(
					Logic.quickCalc(seed, BigInteger.valueOf(m).add(value_2).mod(BigInteger.valueOf(m)).intValue()));
		}
		return Logic.doubleSha512(mix);
	}
	
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
        
//      Some calculation tests:
        System.out.println("expo: " + Math.pow(2, 64));
        System.out.println(BigInteger.valueOf(2).pow(64));
        System.out.println(BigInteger.valueOf(Double.valueOf(Math.pow(2, 64)).longValue()));
        
        System.out.println(Double.valueOf(Math.pow(10, 19)).longValue());
        System.out.println(Long.MAX_VALUE);
        
        System.out.println(Double.MAX_VALUE);
        
        System.out.println(Long.MAX_VALUE);
        
        System.out.println(new BigInteger("7").add(new BigInteger("875216180888826582278764536489264895208284347530920").mod(BigInteger.valueOf(2).pow(64)).mod(new BigInteger("7"))).intValue());
        
        System.out.println("quickHashimoto Here: " + quickHashimoto(new BigInteger("875216180888826582278764536489264895208284347530920"), 678, new BigInteger("8752161808888265822787645364892648952"), 273495));
    }

}
